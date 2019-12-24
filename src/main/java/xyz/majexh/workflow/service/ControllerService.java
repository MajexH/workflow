package xyz.majexh.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.majexh.workflow.domain.ChainRes;
import xyz.majexh.workflow.domain.TaskRes;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.Controller;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ControllerService implements ControllerServiceInterface {

    private Controller controller;
    private ConcurrentHashMap<String, Chain> chainMap;
    private ConcurrentHashMap<String, Topology> topologyMap;

    @Autowired
    public void setTopologyMap(ConcurrentHashMap<String, Topology> topologyMap) {
        this.topologyMap = topologyMap;
    }

    @Autowired
    public void setChainMap(ConcurrentHashMap<String, Chain> chainMap) {
        this.chainMap = chainMap;
    }

    @Autowired
    public void setController(Controller controller) {
        this.controller = controller;
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


}
