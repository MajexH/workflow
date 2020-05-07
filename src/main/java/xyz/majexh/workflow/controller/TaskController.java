package xyz.majexh.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.majexh.workflow.service.ControllerService;

import java.util.HashMap;

@RestController
@RequestMapping("/workflow")
public class TaskController {

    private ControllerService controllerService;

    @Autowired
    public void setControllerService(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @GetMapping("/allTask")
    public ResponseEntity<HashMap<String, Object>> getAllTask(String chainId) throws Exception {
        return ResEntity.okDefault(this.controllerService.getAllTask(chainId));
    }

    @GetMapping("/task")
    public ResponseEntity<HashMap<String, Object>> getTask(String taskId) throws Exception {
        return ResEntity.okDefault(this.controllerService.getTask(taskId));
    }
}
