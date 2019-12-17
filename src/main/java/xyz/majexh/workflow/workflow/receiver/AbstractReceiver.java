package xyz.majexh.workflow.workflow.receiver;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理返回消息的基类
 * 提供一些abstract的方法
 * 需要在子类中实现abstract的所有方法
 */
@Slf4j
public abstract class AbstractReceiver {

    public abstract void getTask();
    public abstract void putTask();

    public void doWork() {

    }
}
