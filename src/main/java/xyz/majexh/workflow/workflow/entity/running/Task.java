package xyz.majexh.workflow.workflow.entity.running;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.HashMap;

/**
 * Node被启动后形成Task
 */
@Data
public class Task {

    private String id;

    private HashMap<String, JSON> inputParams;
    private HashMap<String, JSON> outputParams;
}
