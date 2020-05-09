package xyz.majexh.workflow.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.majexh.workflow.domain.TaskBo;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.service.ControllerService;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

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

    @PostMapping("/restartTask")
    public ResponseEntity<HashMap<String, Object>> restartTask(@RequestBody TaskBo taskBo) throws Exception {
        this.controllerService.restartTask(taskBo);
        return ResEntity.okDefault(null);
    }
}
