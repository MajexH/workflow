package xyz.majexh.workflow.workflow.entity.running;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.Date;
import java.util.HashMap;

/**
 * topology被启动后形成chain
 */
@Data
public class Chain {

    private String id;
    // 当前链对应的topology
    private Topology topology;
    private State state;
    private HashMap<String, Task> taskMap;
    private HashMap<String, Long> tracing;
    // 这个地方要求所有的params的名字是独立的
    private HashMap<String, JSON> params;

    public Chain(Topology topology) {
        this.topology = topology;
        this.state = State.CREATED;
        this.taskMap = new HashMap<>();
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
