package xyz.majexh.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import xyz.majexh.workflow.annotations.ProcessorTypeAnnotation;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.receiver.processor.Processor;
import xyz.majexh.workflow.workflow.receiver.processor.ProcessorMapConstructor;
import xyz.majexh.workflow.workflow.receiver.processor.SystemBarrierProcessor;
import xyz.majexh.workflow.workflow.workflowEnum.State;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class WorkflowApplicationTests {


    @Autowired
    private xyz.majexh.workflow.Test test1;


    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConcurrentHashMap<Type, Processor> map;

    @Test
    void contextLoads() {
        System.out.println(String.format("%d", 1));
    }

    @Test
    void testMap() {
       HashMap<String, Integer> map1 = new HashMap<>(){{
           put("1", 1);
           put("2", 2);
       }}, map2 = new HashMap<>(){{
           put("1", 2);
           put("2", 1);
           put("3", 3);
       }};
       map1.putAll(map2);
       System.out.println(map2);
    }

    @Test
    void testHello() {
        test1.testHello();
    }

}
