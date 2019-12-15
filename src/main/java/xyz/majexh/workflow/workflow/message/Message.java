package xyz.majexh.workflow.workflow.message;

public interface Message {

    void putTask();

    void putState();

    void getTask();

    void getState();
}
