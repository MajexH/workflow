package xyz.majexh.workflow.workflow.workflowEnum;

import java.io.Serializable;

/**
 * 指示当前任务处于什么状态
 */
public enum State implements Serializable {
    CREATED("creat"),
    RUNNING("running"),
    FAIL("fail"),
    PICK("pick"),
    FINISHED("finish"),
    IN_QUEUE("inQueue");

    private String stateName;

    State(String stateName) {
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public boolean isSameSate(State state) {
        return this.stateName.equals(state.getStateName());
    }
}
