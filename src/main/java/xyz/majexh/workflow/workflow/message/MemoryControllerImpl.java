package xyz.majexh.workflow.workflow.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Task;

import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
@Qualifier("memoryMessage")
@Primary
public class MemoryControllerImpl implements MessageController {

    @Autowired
    private LinkedBlockingQueue<Task> taskQueue;

    @Autowired
    private LinkedBlockingQueue<MessageEntity> messageQueue;

    @Override
    public void putTask(Task task) {
        try {
            this.taskQueue.put(task);
        } catch (Exception e) {
            log.error("cannot get there");
        }
    }

    @Override
    public void putState(MessageEntity message) {
        try {
            this.messageQueue.put(message);
        } catch (Exception e) {
            log.error("cannot get there");
        }
    }

    @Override
    public Task getTask() {
        Task task = null;
        try {
            task = this.taskQueue.take();;
        } catch (Exception e) {
            log.error("cannot get there");
        }
        return task;
    }

    @Override
    public MessageEntity getRes() {
        MessageEntity res = null;
        try {
            res = this.messageQueue.take();;
        } catch (Exception e) {
            log.error("cannot get there");
        }
        return res;
    }
}
