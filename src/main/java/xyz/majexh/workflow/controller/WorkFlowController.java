package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import java.util.HashMap;
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
    public ResponseEntity<HashMap<String, Object>> getAllChain() throws Exception {
        return ResEntity.okDefault(this.controllerService.getAllChain());
    }

    @GetMapping("/chain")
    public ResponseEntity<HashMap<String, Object>> getChain(String chainId) throws Exception {
        return ResEntity.okDefault(this.controllerService.getChain(chainId));
    }

    @GetMapping("/allTask")
    public ResponseEntity<HashMap<String, Object>> getAllTask(String chainId) throws Exception {
        return ResEntity.okDefault(this.controllerService.getAllTask(chainId));
    }

    @GetMapping("/task")
    public ResponseEntity<HashMap<String, Object>> getTask(String taskId) throws Exception {
        return ResEntity.okDefault(this.controllerService.getTask(taskId));
    }

    @GetMapping("/allTopology")
    public ResponseEntity<HashMap<String, Object>> getAllTopology() throws Exception {
        return ResEntity.okDefault(this.controllerService.getAllTopology());
    }

    @GetMapping("/topology")
    public ResponseEntity<HashMap<String, Object>> getTopology(String topologyName) throws Exception {
        Topology res = this.controllerService.getTopologyByName(topologyName);
        if (res == null) {
            // 空 返回一个空的topology
            return ResEntity.okDefault(new Topology());
        }
        return ResEntity.okDefault(this.controllerService.getTopologyByName(topologyName));
    }

}
