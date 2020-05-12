package xyz.majexh.workflow.workflow.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.majexh.message.client.client.Client;
import xyz.majexh.message.client.entity.MessageEntity;
import xyz.majexh.workflow.service.AopService;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class ZmqMessageControllerImpl implements MessageController {

    private AopService aopService;
    private Client messageClient;

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
        HashMap<String, Object> sendMessageBody = new HashMap<>();
        sendMessageBody.put("taskId", task.getId());
        sendMessageBody.put("params", task.getInputParams());
        this.messageClient.sendMessage(task.getNode().getHandle(), new JSONObject(sendMessageBody).toString());
    }

    /**
     *
     * 这个地方实际上是system_barrier这个东西来调用的
     * 在task里面put一个barrier已经完成的消息
     * 这样才能在加入新的task到队列里面
     * 也就是说 这个地方应该是往 messageclient 里面的 inqueue 放入一个消息
     * @param message
     */
    @Override
    public void putState(MessageEntity message) {
        log.debug("put message {} to message client inQueue", message);
        this.messageClient.sendMessageToInQueue(message);
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
    public MessageEntity getRes() {

        MessageEntity entity = messageClient.recvMessage();
        log.debug("got message entity from sdk, {}", entity);
        return entity;
    }
}
