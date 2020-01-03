package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.majexh.workflow.domain.ChainRes;
import xyz.majexh.workflow.domain.TaskRes;
import xyz.majexh.workflow.domain.TopologyRes;
import xyz.majexh.workflow.service.ControllerService;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;

import java.util.List;

@RestController
@RequestMapping("/workflow")
public class WorkFlowController {

    private ControllerService controllerService;

    @Autowired
    public void setControllerService(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/allChain")
    public ResEntity<List<ChainRes>> getAllChain() throws Exception {
        return new ResEntity<List<ChainRes>>().okDefault(this.controllerService.getAllChain());
    }

    @GetMapping("/chain")
    public ResEntity<Chain> getChain(String chainId) throws Exception {
        return new ResEntity<Chain>().okDefault(this.controllerService.getChain(chainId));
    }

    @GetMapping("/allTask")
    public ResEntity<List<TaskRes>> getAllTask(String chainId) throws Exception {
        return new ResEntity<List<TaskRes>>().okDefault(this.controllerService.getAllTask(chainId));
    }

    @GetMapping("/task")
    public ResEntity<Task> getTask(String taskId) throws Exception {
        return new ResEntity<Task>().okDefault(this.controllerService.getTask(taskId));
    }

    @GetMapping("/allTopology")
    public ResEntity<List<TopologyRes>> getAllTopology() throws Exception {
        return new ResEntity<List<TopologyRes>>().okDefault(this.controllerService.getAllTopology());
    }

    @GetMapping("/topology")
    public ResEntity<Topology> getTopology(String topologyName) throws Exception {
        Topology res = this.controllerService.getTopologyByName(topologyName);
        if (res == null) {
            // 空 返回一个空的topology
            return new ResEntity<Topology>().okDefault(new Topology());
        }
        return new ResEntity<Topology>().okDefault(this.controllerService.getTopologyByName(topologyName));
    }

}
