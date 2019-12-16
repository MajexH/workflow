package xyz.majexh.workflow.executors;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class TopologyExecutor {

    /**
     * 根据传入的拓扑和参数 生成第一个任务
     * @param inputParams 该拓扑的输入参数
     * @return 生成的第一个需要执行的任务
     * @throws BaseException 会传出失败的原因以更新chain的message字段和状态
     */
    public Task getFirstTask(Chain chain, HashMap<String, JSON> inputParams) {
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
        if (taskId.equals(chain.getTopology().getEndNodeId())) {
            chain.changeState(State.FINISHED);
            log.info(String.format("%s已经执行完毕", chain.getId()));
            return tasks;
        }
        Task task = chain.getTask(taskId);
        List<String> nodes = chain.getTopology().getEdgePair().get(taskId.split(":")[1]);
        for (String node : nodes) {
            String newTaskId = String.format("%s:%s", chain.getId(), node);
            if (chain.hasTask(newTaskId)) {
                tasks.add(chain.getTask(taskId));
            } else {


//                new Task(node, chain.getId(), task.getOutputParams());
            }
        }
        return tasks;
    }
}
