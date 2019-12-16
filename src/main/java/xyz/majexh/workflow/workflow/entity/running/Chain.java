package xyz.majexh.workflow.workflow.entity.running;

import lombok.Data;

import java.util.HashMap;

/**
 * topology被启动后形成chain
 */
@Data
public class Chain {

    private String id;
    private HashMap<String, Task> taskMap;

}
