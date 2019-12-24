package xyz.majexh.workflow.service;

import xyz.majexh.workflow.domain.ChainRes;
import xyz.majexh.workflow.domain.TaskRes;
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
}
