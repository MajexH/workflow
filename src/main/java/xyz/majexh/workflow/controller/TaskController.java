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
    private ConcurrentHashMap<String, Chain> chainMap;

    @Autowired
    public void setChainMap(ConcurrentHashMap<String, Chain> chainMap) {
        this.chainMap = chainMap;
    }

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
        if (!this.chainMap.containsKey(StringUtils.extractChainIdFromTaskId(taskBo.getId()))) {
            throw new BaseException(ExceptionEnum.CHAIN_NOT_FOUND);
        }
        Chain chain = this.chainMap.get(StringUtils.extractChainIdFromTaskId(taskBo.getId()));
        Task task = chain.getTask(taskBo.getId());
        if (task == null) {
            throw new BaseException(ExceptionEnum.TASK_NOT_FOUND);
        }
        if (!task.getState().equals(State.FAIL) || !task.getState().equals(State.FINISHED)) {
            throw new BaseException(ExceptionEnum.WRONG_RESUBMIT_TASK_STATE);
        }
        task.setInputParams(new JSONObject(taskBo.getInputParams()));
        task.setRetry(0);
        this.controllerService.restartTask(task.getId());
        return ResEntity.okDefault(null);
    }
}
