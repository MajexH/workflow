package xyz.majexh.workflow.workflow.message;

import xyz.majexh.message.client.entity.MessageEntity;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Task;

public interface MessageController {

    void putTask(Task task);

    void putState(MessageEntity message);

    Task getTask();

    MessageEntity getRes();
}
