package xyz.majexh.workflow.workflow.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.utils.MessageUtils;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.executors.ChainExecutor;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.receiver.processor.Processor;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    @Qualifier("memoryMessage")
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
    private void process(Chain chain, Task task, MessageEntity entity) {
        // 根据res更新输出
        task.setOutputParams(entity.getRes());
        // 保存当前的输出
        chain.saveParams(entity.getRes());
        task.changeState(State.FINISHED);
        log.info(String.format("%s task success finish", task.getId()));
        List<Task> tasks = this.executor.getNextTasks(chain, task.getId());
        for (Task nextTask : tasks) {
            log.debug(String.format("new task %s generate", nextTask.getId()));
            Processor processor = this.processorMap.get(nextTask.getNodeType());
            if (processor == null) {
                // TODO: default process & throw Exception
            } else {
               processor.process(chain, nextTask, entity);
            }
        }
    }

    /**
     * 处理接受消息的逻辑
     */
    public void receiveMessage() {
        MessageEntity message = this.getRes();

        if (message.getTaskId() == null) {
            log.error("error: receive response without taskId filed");
            return;
        }
        Task task = getTaskFromChainMap(message.getTaskId());
        Chain chain = getChainFromChainMap(message.getTaskId());
        log.debug(String.format("receive %s task's response, status is %s", message.getTaskId(), message.getStatus()));
        if (MessageUtils.isPick(message)) {
            // 其他的通信的时候 应该要向我传递一个pick的消息 告诉我消息任务已经被正确的拿到
            if (task == null) {
                log.error(String.format("cannot find %s task in chain_map, please check the taskId", message.getTaskId()));
                // TODO: Exception
                return;
            }
            // 如果worker已经拿到了message,则说明任务已经开始运行
            task.changeState(State.RUNNING);
        } else if (MessageUtils.isSuccess(message)) {
            log.info(String.format("receive \"success\" status of task %s", message.getTaskId()));

            System.out.println(2 + "" + message);

            this.process(chain, task, message);
        } else if (MessageUtils.isFail(message)) {
            if (task.getRetry() < task.getRetryMax()) {
                task.setRetry(task.getRetry() + 1);
                // 重新上交任务
                this.submit(task);
                log.info(String.format("%s task resubmit, retry count = %d", task.getId(), task.getRetry()));
            } else {
                task.changeState(State.FAIL);
                // 传递失败消息
                chain.changeState(State.FAIL);
                chain.setMessage(message.getMessage());
                log.debug(String.format("chain %s fail because of %s task fail, fail message: %s", chain.getId(), task.getId(), message.getMessage()));
            }
        }
    }

}
