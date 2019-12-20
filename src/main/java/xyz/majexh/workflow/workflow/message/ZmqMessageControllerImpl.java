package xyz.majexh.workflow.workflow.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.workflowEnum.State;

@Component
@Slf4j
public class ZmqMessageControllerImpl implements MessageController {

    /**
     * TODO: 可能会有线程安全的问题
     * @param task
     */
    @Override
    public void putTask(Task task) {
        // 重新提交任务 会造成问题
        if (task.getState().isSameSate(State.FINISHED)) {
            log.error(String.format("resubmit %s task to service after it finished", task.getId()));
            return;
        }
        // submit task到zmq中
        task.changeState(State.IN_QUEUE);
    }

    /**
     * TODO: 可能会有线程安全的问题
     * 实际上是向 自己的getRes发送 message
     * @param message
     */
    @Override
    public void putState(MessageEntity message) {

    }

    @Override
    public Task getTask() {
        return null;
    }

    @Override
    public MessageEntity getRes() {
        return null;
    }
}
