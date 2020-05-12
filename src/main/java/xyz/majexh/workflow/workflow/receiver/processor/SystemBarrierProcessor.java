package xyz.majexh.workflow.workflow.receiver.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.majexh.message.client.entity.MessageEntity;
import xyz.majexh.message.client.enums.CommandEnum;
import xyz.majexh.workflow.annotations.ProcessorTypeAnnotation;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;
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
    public void process(Chain chain, Task task, MessageBody entity) {
        log.debug("check barrier");
        HashMap<String, Object> inputParams = JSONUtils.json2HashMap(task.getInputParams());
        if (!inputParams.containsKey(task.getId())) {
            log.error("task {} is barrier but lack the input params for pass, task detail: {}", task.getId(), task);
            // TODO: exception
            return;
        }
        // 可能优化的点 这个地方现在想到的只有强转了 没有其他想法
        List<String> preTasks = (List<String>) inputParams.get(task.getId());
        for (String preTaskId : preTasks) {
            // 如果其中有任何一个任务没有结束 则barrier不能通过
            if (!chain.getTask(preTaskId).getState().isSameSate(State.FINISHED)) {
                log.debug(String.format("barrier %s don't satisfy because of the pre task %s not finish", task.getId(), preTaskId));
                return;
            }
        }
        MessageBody body = new MessageBody(){{
            setCode(200);
            setMsg("");
            HashMap<String, Object> data = new HashMap<>();
            data.put("taskId", task.getId());
            data.put("params", new JSONObject());
            setData(new JSONObject(data));
        }};
        MessageEntity res = new MessageEntity();
        res.setCommand(CommandEnum.RESULT);
        res.setBody(JSON.toJSONString(body));
        this.messageController.putState(res);
        log.debug("barrier pass");
    }
}
