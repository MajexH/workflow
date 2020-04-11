package xyz.majexh.workflow.workflow.receiver.processor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.majexh.workflow.annotations.ProcessorTypeAnnotation;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.HashMap;
import java.util.List;

@ProcessorTypeAnnotation(Type.SYSTEM_BARRIER)
@Slf4j
public class SystemBarrierProcessor implements Processor {

    private MessageController messageController;

    @Autowired
    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(Chain chain, Task task, MessageEntity entity) {
        log.info("check barrier");
        HashMap<String, Object> inputParams = JSONUtils.json2HashMap(task.getInputParams());
        if (!inputParams.containsKey(task.getId())) {
            log.error(String.format("task %s is barrier but lack the input params for pass", task.getId()));
            // TODO: exception
            return;
        }
        // TODO: 可能优化的点 这个地方现在想到的只有强转了 没有其他想法
        List<String> preTasks = (List<String>) inputParams.get(task.getId());
        for (String preTaskId : preTasks) {
            // 如果其中有任何一个任务没有结束 则barrier不能通过
            if (!chain.getTask(preTaskId).getState().isSameSate(State.FINISHED)) {
                log.debug(String.format("barrier %s don't satisfy because of the pre task %s not finish", task.getId(), preTaskId));
                return;
            }
        }
        this.messageController.putState(new MessageEntity(){{
            setStatus("success");
            setTaskId(task.getId());
            setRes(new JSONObject());
            setMessage("");
        }});
        log.info("barrier pass");
    }
}
