package xyz.majexh.workflow.workflow.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.majexh.message.client.entity.MessageEntity;
import xyz.majexh.workflow.service.AopService;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.utils.MessageUtils;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.executors.ChainExecutor;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.receiver.processor.Processor;
import xyz.majexh.workflow.workflow.receiver.processor.UserProcessor;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理返回消息的基类
 * 提供一些abstract的方法
 * 需要在子类中实现abstract的所有方法
 * TODO: 如果存在性能问题话 可能需要改成 prototype 然后启动多个receiver 这个地方就要涉及到线程安全的问题
 */
@Slf4j
@Component
public class Receiver {

    private ConcurrentHashMap<String, Chain> chainMap;
    private MessageController messageController;
    private ConcurrentHashMap<Type, Processor> processorMap;
    private ChainExecutor executor;
    private AopService aopService;
    // 默认的处理器
    private UserProcessor defaultProcessor;

    @Autowired
    public void setUserProcessor(UserProcessor defaultProcessor) {
        this.defaultProcessor = defaultProcessor;
    }

    @Autowired
    public void setAopService(AopService aopService) {
        this.aopService = aopService;
    }

    @Autowired
    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    @Autowired
    public void setProcessorMap(ConcurrentHashMap<Type, Processor> processorMap) {
        this.processorMap = processorMap;
    }

    @Autowired
    public void setChainMap(ConcurrentHashMap<String, Chain> chainMap) {
        this.chainMap = chainMap;
    }

    @Autowired
    public void setExecutor(ChainExecutor executor) {
        this.executor = executor;
    }

    private Task getTaskFromChainMap(String taskId) {
       return this.chainMap.get(StringUtils.extractChainIdFromTaskId(taskId)).getTask(taskId);
    }

    private Chain getChainFromChainMap(String taskId) {
        return this.chainMap.get(StringUtils.extractChainIdFromTaskId(taskId));
    }


    private void submit(Task task) {
        log.debug("receiver resubmit task" + task.getId());
        this.messageController.putTask(task);
    }

    private MessageEntity getRes() {
        return this.messageController.getRes();
    }

    /**
     * 根据传入的task的node参数去执行指定的processor逻辑
     * @param task
     * @param entity
     */
    private void process(Chain chain, Task task, MessageBody entity) {
        // 根据res更新输出
        HashMap<String, Object> message = JSONUtils.json2HashMap(entity.getData());
        if (!message.containsKey("params")) {
            log.error("got wrong message without data part in params");
            this.aopService.changeState(task, State.FAIL);
            this.aopService.changeState(chain, State.FAIL);
            chain.setMessage("从worker处获取到不包含params字段的消息，请检查参数");
            return;
        }
//        JSON.parse() 出来的东西是通过 defaultParse 出来的 直接转型不会失败
        JSON outputs = (JSON) JSON.parse(message.get("params").toString());
        task.setOutputParams(outputs);
        // 保存当前的输出
        chain.saveParams(outputs);
        // task.changeState(State.FINISHED);
        this.aopService.changeState(task, State.FINISHED);
        log.info(String.format("%s task success finish", task.getId()));
        for (Task nextTask : this.executor.getNextTasks(chain, task.getId())) {
            log.debug(String.format("new task %s generate", nextTask.getId()));
            Processor processor = this.processorMap.get(nextTask.getNodeType());
            if (processor == null) {
                // default process
                defaultProcessor.process(chain, nextTask, entity);
                log.debug("task require {} processor not found, use default processor", task.getNodeType());
            } else {
               processor.process(chain, nextTask, entity);
            }
        }
    }

    /**
     * 处理接受消息的逻辑
     */
    public void receiveMessage() {
        MessageEntity entity = this.getRes();

        MessageBody res = JSON.parseObject(entity.getBody(), MessageBody.class);
        log.debug("got message body: {}", res);
        // 参数检查
        HashMap<String, Object> message = JSONUtils.json2HashMap(res.getData());
        if (!message.containsKey("taskId")) {
            log.error("error: receive response without taskId filed");
            return;
        }
        String taskId = (String) message.get("taskId");
        Task task = getTaskFromChainMap(taskId);
        Chain chain = getChainFromChainMap(taskId);
        log.debug(String.format("receive %s task's response, status is %s", taskId, entity.getCommand()));

        // 根据 entity 中的 Command来判断
        if (MessageUtils.isPick(entity)) {
            // 其他的通信的时候 应该要向我传递一个pick的消息 告诉我消息任务已经被正确的拿到
            if (task == null) {
                log.error(String.format("cannot find %s task in chain_map, please check the taskId", taskId));
                // TODO: Exception
                return;
            }
            // 如果worker已经拿到了message,则说明任务已经开始运行
            // task.changeState(State.RUNNING);
            this.aopService.changeState(task, State.RUNNING);
        } else if (MessageUtils.isSuccess(res)) {
            log.info(String.format("receive \"success\" status of task %s",taskId));
            this.process(chain, task, res);
        } else if (MessageUtils.isFail(res)) {
            if (task.getRetry() < task.getRetryMax()) {
                task.setRetry(task.getRetry() + 1);
                // 重新上交任务
                this.submit(task);
                log.info(String.format("%s task resubmit, retry count = %d", task.getId(), task.getRetry()));
            } else {
                // task.changeState(State.FAIL);
                this.aopService.changeState(task, State.FAIL);
                // 传递失败消息 chain.changeState(State.FAIL);
                this.aopService.changeState(chain, State.FAIL);
                chain.setMessage(res.getMsg());
                log.debug(String.format("chain %s fail because of %s task fail, fail message: %s", chain.getId(), task.getId(), res.getMsg()));
            }
        }
    }

}
