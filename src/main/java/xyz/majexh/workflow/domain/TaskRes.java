package xyz.majexh.workflow.domain;

import lombok.Data;

@Data
public class TaskRes {

    private String taskId;
    private String nodeName;

    public TaskRes(String taskId, String nodeName) {
        this.taskId = taskId;
        this.nodeName = nodeName;
    }
}
