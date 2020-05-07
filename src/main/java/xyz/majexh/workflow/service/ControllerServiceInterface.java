package xyz.majexh.workflow.service;

import com.alibaba.fastjson.JSON;
import org.springframework.lang.Nullable;
import xyz.majexh.workflow.controller.ResEntity;
import xyz.majexh.workflow.domain.ChainRes;
import xyz.majexh.workflow.domain.TaskRes;
import xyz.majexh.workflow.domain.TopologyRes;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;

import java.util.List;

public interface ControllerServiceInterface {

    void submitTask(String taskId) throws Exception;

    void restartTask(String taskId) throws Exception;

    Chain getChain(String chainId) throws Exception;

    List<ChainRes> getAllChain() throws Exception;

    List<TaskRes> getAllTask(String chainId) throws Exception;

    Task getTask(String taskId) throws Exception;

    List<TopologyRes> getAllTopology() throws Exception;

    Topology getTopologyByName(String topologyName) throws Exception;

    void updateNodeOfTopologyByTopologyName(String topologyName, Node node) throws Exception;

    void startTopologyByName(String topologyName, @Nullable JSON inputParams) throws Exception;
}
