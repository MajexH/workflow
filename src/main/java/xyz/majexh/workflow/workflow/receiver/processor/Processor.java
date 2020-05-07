package xyz.majexh.workflow.workflow.receiver.processor;

import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;

/**
 * 提供Processor的接口 针对node的type实现不同的处理逻辑
 */
public interface Processor {
    void process(Chain chain, Task task, MessageBody entity);
}
