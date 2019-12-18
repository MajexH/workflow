package xyz.majexh.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.majexh.workflow.annotations.ProcessorTypeAnnotation;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class WorkflowApplicationTests {

    @Test
    void contextLoads() {
        HashMap<String, Object> res = new HashMap<>(){{
            put("a", 1);
            put("b", 2);
        }};
        HashMap<String, Object> test = new HashMap<>(){{
            put("taskId", "test");
            put("status", State.CREATED);
//            put("res", res);
        }};
        MessageEntity entity = MessageEntity.getMessageFromJSON(new JSONObject(test));
        System.out.println(entity);

    }

    @Test
    void testAnnotation() {
        Class<?> clazz = xyz.majexh.workflow.Test.class;
        System.out.println(clazz.getAnnotation(ProcessorTypeAnnotation.class).type().isSameType(Type.USER));
    }

}
