package xyz.majexh.workflow.workflow.message;

import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Task;

public interface MessageController {

    void putTask(Task task);

    void putState(MessageBody message);

    Task getTask();

    MessageBody getRes();
}
