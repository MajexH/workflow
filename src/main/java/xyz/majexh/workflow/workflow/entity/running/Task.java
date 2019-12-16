package xyz.majexh.workflow.workflow.entity.running;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.Date;
import java.util.HashMap;

/**
 * Node被启动后形成Task
 */
@Data
public class Task {

    private String id;
    private Node node;

    private HashMap<String, JSON> inputParams;
    private HashMap<String, JSON> outputParams;
    private HashMap<String, Long> tracing;

    private int retry;
    private State state;

    public Task(Node node, String chainId, HashMap<String, JSON> inputParams) {
        this.state = State.CREATED;
        this.retry = 0;
        this.node = node;
        this.id = String.format("%s:%s", chainId, this.node.getId());
        this.inputParams = inputParams;
        this.outputParams = new HashMap<>();
        this.tracing = new HashMap<>(){{
            put(state.getStateName(), new Date().getTime());
        }};
    }

    public void changeState(State state) {
        if (this.state.isSameSate(state)) return;
        this.tracing.put(String.format("%s -> %s", this.state.getStateName(), state.getStateName()), new Date().getTime());
        this.state = state;
    }
}
