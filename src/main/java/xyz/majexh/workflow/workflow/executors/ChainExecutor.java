package xyz.majexh.workflow.workflow.executors;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class ChainExecutor {

    /**
     * 根据传入的拓扑和参数 生成第一个任务
     * @param inputParams 该拓扑的输入参数
     * @return 生成的第一个需要执行的任务
     * @throws BaseException 会传出失败的原因以更新chain的message字段和状态
     */
    public Task getFirstTask(Chain chain, JSON inputParams) {
        Topology topology = chain.getTopology();
        String startId = topology.getStartNodeId();
        if (!topology.getNodeMap().containsKey(startId)) {
            log.error(String.format("cannot find %s node", startId));
            throw new BaseException(ExceptionEnum.START_TOPOLOGY_ERROR);
        }
        Node firstNode = topology.getNodeMap().get(startId);
        if (!firstNode.checkInputParams(inputParams)) {
            throw new BaseException(ExceptionEnum.INPUT_PARAMS_ERROR);
        }
        Task temp = new Task(firstNode, chain.getId(), inputParams);
        chain.saveTask(temp);
        // 获取到了第一个任务后 会立即执行整个链的开始
        chain.changeState(State.RUNNING);
        chain.saveParams(inputParams);
        return temp;
    }

    /**
     * 根据taskId获取接下来需要执行的任务
     * @param chain 当前任务链
     * @param taskId 当前任务id
     * @return 任务队列
     * @throws BaseException 会传出失败的原因以更新chain的message字段和状态
     */
    public List<Task> getNextTasks(Chain chain, String taskId) {
        if (!chain.hasTask(taskId)) {
            log.error(String.format("chain %s cannot find %s task", chain.getId(), taskId));
            throw new BaseException(ExceptionEnum.TASK_NOT_FOUND);
        }
        List<Task> tasks = new ArrayList<>();
        // 执行到末尾
        // TODO: master中修改bug
        String nodeId = StringUtils.extractNodeIdFromTaskId(taskId);
        if (nodeId.equals(chain.getTopology().getEndNodeId())) {
            chain.changeState(State.FINISHED);
            log.info(String.format("%s task reach the end of the chain", chain.getId()));
            return tasks;
        }
        for (String node : chain.getNextNodes(nodeId)) {
            String newTaskId = StringUtils.getTaskId(chain.getId(), node);
            if (chain.hasTask(newTaskId)) {
                // TODO: fix bugs
                tasks.add(chain.getTask(newTaskId));
            } else {
                Node next = chain.getNode(node);
                HashMap<String, Object> input = new HashMap<>();
                // TODO: 优化效率 因为现在扫描了两遍
                // TODO: e param 没找到 说明 success没有把e加进去

                if (!next.checkInputParams(JSONUtils.hashMap2Json(chain.getParams()))) {
                    log.error(String.format("%s's params %s, cannot satisfy %s input params", taskId, chain.getParams(), next.getInputParams()));
                    throw new BaseException(ExceptionEnum.OUTPUT_NOT_SATISFY);
                } else {
                    for (String key : next.getInputParams()) {
                        input.put(key, chain.getParams().get(key));
                    }
                }
                Task nextTask = new Task(next, chain.getId(), JSONUtils.hashMap2Json(input));
                this.fileSystemTaskArgs(chain.getId(), nextTask);
                chain.saveTask(nextTask);
                tasks.add(nextTask);
            }
        }
        log.debug(String.format("get next tasks success, which is %s", tasks));
        return tasks;
    }

    /**
     * 用于填充system类型节点的输入参数
     * 其输入参数 主要是运行时的各类前置节点 需要在遇到屏障的时候
     * 判断前置节点是否已经全部执行完毕
     * @param chainId 当前task所属的链
     * @param task 当前task
     */
    private void fileSystemTaskArgs(String chainId, Task task) {
        if (task.getNodeType().isSameType(Type.SYSTEM_BARRIER)) {
            // 拿到原来的输入
            HashMap<String, Object> input = JSONUtils.json2HashMap(task.getInputParams());
            List<String> res = new ArrayList<>();
            // 不用再次检查taskId域里面的值 因为topology不会动态的更新
            // 添加所有的运行时的taskId到barrier的inputParams["taskId"]中
            if (!input.containsKey(task.getId())) {
                for (String id : task.getNodeSystemArgs()) {
                    res.add(StringUtils.getTaskId(chainId, id));
                }
                input.put(task.getId(), res);
            }
            // 更新原来的输入
            task.setInputParams(JSONUtils.hashMap2Json(input));
            log.debug(String.format("%s barrier add input params %s as required filed", task.getId(), res));
        }
    }
}
