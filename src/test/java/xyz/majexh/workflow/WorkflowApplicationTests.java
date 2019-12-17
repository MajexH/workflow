package xyz.majexh.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.workflowEnum.State;

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

}
