package xyz.majexh.workflow.workflow.receiver.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.majexh.workflow.annotations.ProcessorTypeAnnotation;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

@ProcessorTypeAnnotation(Type.USER)
@Slf4j
public class UserProcessor implements Processor {

    private MessageController messageController;

    @Autowired
    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
    }

    @Override
    public void process(Chain chain, Task task, MessageEntity entity) {
        // 如果下一个任务是user类型的 就直接上交任务
        log.debug("userProcessor submit task" + task.getId());
        this.messageController.putTask(task);
    }
}
