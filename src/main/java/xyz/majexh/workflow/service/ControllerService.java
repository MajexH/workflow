package xyz.majexh.workflow.service;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import xyz.majexh.workflow.controller.ResEntity;
import xyz.majexh.workflow.domain.ChainRes;
import xyz.majexh.workflow.domain.TaskRes;
import xyz.majexh.workflow.domain.TopologyRes;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.Controller;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ControllerService implements ControllerServiceInterface {

    private Controller controller;
    private ConcurrentHashMap<String, Topology> topologies;

    @Autowired
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Autowired
    public void setTopologies(ConcurrentHashMap<String, Topology> topologies) {
        this.topologies = topologies;
    }

    @Override
    public void submitTask(String taskId) throws Exception {
        this.controller.submitTask(taskId);
    }

    @Override
    public void restartTask(String taskId) throws Exception {
        this.controller.restartTask(taskId);
    }

    @Override
    public Chain getChain(String chainId) throws Exception {
        return this.controller.getChain(chainId);
    }

    @Override
    public List<ChainRes> getAllChain() throws Exception {
        List<ChainRes> res = new ArrayList<>();
        for (Chain chain : this.controller.getAllChain()) {
            res.add(new ChainRes(chain.getTopology().getName(), chain.getId()));
        }
        return res;
    }

    @Override
    public List<TaskRes> getAllTask(String chainId) throws Exception {
        List<TaskRes> res = new ArrayList<>();
        for (Task task : this.controller.getAllTask(chainId)) {
            res.add(new TaskRes(task.getId(), task.getNode().getName()));
        }
        return res;
    }

    @Override
    public Task getTask(String taskId) throws Exception {
        return this.controller.getChain(StringUtils.extractChainIdFromTaskId(taskId)).getTaskMap().get(taskId);
    }

    @Override
    public List<TopologyRes> getAllTopology() throws Exception {
        List<TopologyRes> res = new ArrayList<>();
        for (Topology temp : this.controller.getAllTopology()) {
            res.add(new TopologyRes(temp.getId(), temp.getName(), temp.getStartNodeId(), temp.getEndNodeId()));
        }
        return res;
    }

    @Override
    public Topology getTopologyByName(String topologyName) throws Exception {
        return this.controller.getTopologyByName(topologyName);
    }

    @Override
    public void updateNodeOfTopologyByTopologyName(String topologyName, Node node) throws Exception {
        if (!this.topologies.containsKey(topologyName)) {
            throw new BaseException(ExceptionEnum.TOPOLOGY_NOT_FOUND);
        }
        Topology topology = this.topologies.get(topologyName);
        Node nodeInner = topology.getNode(node.getId());
        nodeInner.updateNode(node);
    }

    @Override
    public void startTopologyByName(String topologyName, JSON inputParams) throws Exception {
        this.controller.startTopologyByName(topologyName, inputParams);
    }


}
