package xyz.majexh.workflow.workflow.entity.running;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Node被启动后形成Task
 */
@Data
public class Task {

    private String id;
    private Node node;

    private JSON inputParams;
    private JSON outputParams;
    private HashMap<String, Long> tracing;

    private int retry;
    private State state;

    public Task(Node node, String chainId, JSON inputParams) {
        this.state = State.CREATED;
        this.retry = 0;
        this.node = node;
        this.id = StringUtils.getTaskId(chainId, node.getId());
        this.inputParams = inputParams;
        this.tracing = new HashMap<>();
        this.tracing.put(state.getStateName(), new Date().getTime());
    }

    public void changeState(State state) {
        if (this.state.isSameSate(state)) return;
        this.tracing.put(String.format("%s -> %s", this.state.getStateName(), state.getStateName()), new Date().getTime());
        this.state = state;
    }

    public Type getNodeType() {
        return this.node.getType();
    }

    public List<String> getNodeSystemArgs() {
        return this.node.getSystemArgs();
    }

    public int getRetryMax() {
        return this.node.getRetryMax();
    }
}
