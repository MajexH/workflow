package xyz.majexh.workflow.workflow.message;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.majexh.message.client.client.Client;
import xyz.majexh.message.client.entity.MessageEntity;
import xyz.majexh.workflow.service.AopService;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class ZmqMessageControllerImpl implements MessageController {

    private AopService aopService;
    private Client messageClient;
    private LinkedBlockingQueue<Task> taskQueue;
    private LinkedBlockingQueue<MessageBody> resQueue;

    @Autowired
    public void setTaskQueue(LinkedBlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Autowired
    public void setResQueue(LinkedBlockingQueue<MessageBody> resQueue) {
        this.resQueue = resQueue;
    }

    @Autowired
    public void setAopService(AopService aopService) {
        this.aopService = aopService;
    }

    @Autowired
    public void setMessageClient(Client messageClient) {
        this.messageClient = messageClient;
    }

    /**
     *
     * @param task
     */
    @Override
    public void putTask(Task task) {
        // 重新提交任务 会造成问题
        if (task.getState().isSameSate(State.FINISHED)) {
            log.error(String.format("resubmit %s task to service after it finished", task.getId()));
            return;
        }
        // submit task到zmq中 task.changeState(State.IN_QUEUE);
        this.aopService.changeState(task, State.IN_QUEUE);
        try {
            this.taskQueue.put(task);
        } catch (InterruptedException interruptedException) {
            log.debug("put task {} to taskQueue has been interrupted, {}", task, interruptedException.getMessage());
        }

    }

    /**
     *
     * 这个地方实际上是system_barrier这个东西来调用的
     * 在task里面put一个barrier已经完成的消息
     * 这样才能在加入新的task到队列里面
     * @param message
     */
    @Override
    public void putState(MessageBody message) {
        log.debug("put message {} to resQueue", message);
        try {
            this.resQueue.put(message);
        } catch (InterruptedException exception) {
            log.debug("put message to resQueue has been interrupted, {}", exception.getMessage());
        }
    }

    /**
     * 留给本地测试的worker调用的
     * @return
     */
    @Override
    public Task getTask() {
        return null;
    }

    /**
     * 从messageSDK里面获取消息
     * @return
     */
    @Override
    public MessageBody getRes() {
        // 根据获取到的消息 构建messageBody
        MessageEntity entity = messageClient.recvMessage();
        return JSON.parseObject(entity.getBody(), MessageBody.class);
    }
}
