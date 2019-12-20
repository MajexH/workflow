package xyz.majexh.workflow.workflow.entity.running;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * topology被启动后形成chain
 */
@Data
@Slf4j
public class Chain {

    private String id;
    // 当前链对应的topology
    private Topology topology;
    private State state;
    private ConcurrentHashMap<String, Task> taskMap;
    private ConcurrentHashMap<String, Long> tracing;
    // 这个地方要求所有的params的名字是独立的
    private ConcurrentHashMap<String, Object> params;
    // 保存当前失败的错误消息
    private String message;

    public Chain(Topology topology) {
        this.id = StringUtils.getUUID();
        this.topology = topology;
        this.state = State.CREATED;
        this.taskMap = new ConcurrentHashMap<>();
        this.tracing = new ConcurrentHashMap<>(){{
            put(state.getStateName(), new Date().getTime());
        }};
        this.params = new ConcurrentHashMap<>();
    }

    // TODO: AOP hook
    public void changeState(State state) {
        if (this.state.isSameSate(state)) return;
        this.tracing.put(String.format("%s -> %s", this.state.getStateName(), state.getStateName()), new Date().getTime());
        log.info(String.format("%s -> %s", this.state.getStateName(), state.getStateName()));
        this.state = state;
    }

    public void saveTask(Task task) {
        this.taskMap.put(task.getId(), task);
    }

    public void saveParams(JSON params) {
        this.params.putAll(JSONUtils.json2HashMap(params));
    }

    public boolean hasTask(String taskId) {
        return this.taskMap.containsKey(taskId);
    }

    public Task getTask(String taskId) {
        return this.taskMap.get(taskId);
    }

    public Node getNode(String nodeId) {
        return this.topology.getNode(nodeId);
    }

    public List<String> getNextNodes(String nodeId) {
        return this.topology.getNextNodes(nodeId);
    }
}
