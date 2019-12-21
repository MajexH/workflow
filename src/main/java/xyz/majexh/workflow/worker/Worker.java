package xyz.majexh.workflow.worker;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.message.MessageController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * 测试类
 */
@Component
@Scope("prototype")
public class Worker implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Worker.class);

    @Autowired
    @Qualifier("memoryMessage")
    public MessageController messageInterface;

    private HashMap<String, Method> map = new HashMap<>(){{
        Class<Worker> worker = Worker.class;
        try {
            put("handle1", worker.getMethod("handle1", HashMap.class));
            put("handle2", worker.getMethod("handle2", HashMap.class));
            put("handle3", worker.getMethod("handle3", HashMap.class));
            put("handle4", worker.getMethod("handle4", HashMap.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }};

    public HashMap<String, Object> handle1(HashMap<String, Object> inputParams) {
        return new HashMap<>(){{
            put("c", (Integer) inputParams.get("a") + (Integer) inputParams.get("b"));
        }};
    }

    public HashMap<String, Object> handle2(HashMap<String, Object> inputParams) {
        return new HashMap<>(){{
            put("d", (Integer) inputParams.get("c") + 1);
        }};
    }

    public HashMap<String, Object> handle3(HashMap<String, Object> inputParams) {
        Random r = new Random();
        r.nextInt();
        if (r.nextDouble() > 0.5d) {
            throw new RuntimeException("任务执行失败");
        }
        HashMap<String, Object> map = new HashMap<>(){{
            put("e", (Integer) inputParams.get("c") - 1);
        }};
        return map;
    }

    public HashMap<String, Object> handle4(HashMap<String, Object> inputParams) {
        return new HashMap<>(){{
            put("f", (Integer) inputParams.get("d") + (Integer) inputParams.get("e"));
        }};
    }

    public void processTask() {
        Task task = this.messageInterface.getTask();
        logger.debug(String.format("接受到%s %s任务", task.getId(), task.getNode().getName()));
        HashMap<String, Object> res1 = null;
        try {
            res1 = (HashMap<String, Object>) map.get(task.getNode().getHandle()).invoke(this, JSONUtils.json2HashMap(task.getInputParams()));



            HashMap<String, Object> finalRes = res1;
            MessageEntity entity = new MessageEntity(){{
                setStatus("success");
                setRes(JSONUtils.hashMap2Json(finalRes));
                setTaskId(task.getId());
                setMessage("");
            }};
            this.messageInterface.putState(entity);
        } catch (Exception e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            this.messageInterface.putState(new MessageEntity(){{
               setStatus("fail");
               setMessage(e.getMessage());
               setTaskId(task.getId());
               setRes(new JSONObject());
            }});
        }
    }

    @Override
    public void run() {
        logger.info("worker run");
        while (true) {
            this.processTask();
        }
    }
}
