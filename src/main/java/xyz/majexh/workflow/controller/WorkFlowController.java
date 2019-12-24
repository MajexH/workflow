package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.majexh.workflow.domain.ChainRes;
import xyz.majexh.workflow.domain.TaskRes;
import xyz.majexh.workflow.service.ControllerService;
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
    public List<ChainRes> getAllChain() throws Exception {
        return this.controllerService.getAllChain();
    }

    @GetMapping("/chain")
    public Chain getChain(String chainId) throws Exception {
        return this.controllerService.getChain(chainId);
    }

    @GetMapping("/allTask")
    public List<TaskRes> getAllTask(String chainId) throws Exception {
        return this.controllerService.getAllTask(chainId);
    }

    @GetMapping("/task")
    public Task getTask(String taskId) throws Exception {
        return this.controllerService.getTask(taskId);
    }


}
