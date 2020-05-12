package xyz.majexh.workflow.workflow.receiver.processor;

import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;

/**
 * 提供Processor的接口 针对node的type实现不同的处理逻辑
 */
public interface Processor {
    /**
     *
     * @param chain 当前的执行链
     * @param task 执行的下一个任务
     * @param entity 执行的时候 当前任务拿到的消息数据
     */
    void process(Chain chain, Task task, MessageBody entity);
}
