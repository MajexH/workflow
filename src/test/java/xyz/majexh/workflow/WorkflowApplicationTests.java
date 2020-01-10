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
import xyz.majexh.workflow.dao.UserDao;
import xyz.majexh.workflow.service.AopService;
import xyz.majexh.workflow.utils.JwtUtils;
import xyz.majexh.workflow.workflow.Controller;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Task;
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
    AopService service;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConcurrentHashMap<Type, Processor> map;

    @Autowired
    Controller controller;

    @Autowired
    UserDao userDao;

    @Test
    void testUserDao() {
        System.out.println(userDao.findUserByUsernameEquals("123"));
        System.out.println(userDao.findUserByUsernameEquals(""));
    }

    @Test
    void contextLoads() {
        System.out.println(String.format("%d", 1));
    }

    @Test
    void testMap() {
        HashMap<String, Integer> map1 = new HashMap<>() {{
            put("1", 1);
            put("2", 2);
        }}, map2 = new HashMap<>() {{
            put("1", 2);
            put("2", 1);
            put("3", 3);
        }};
        map1.putAll(map2);
        System.out.println(map2);
        System.out.println(map2.get("5"));
    }

    @Test
    void testController() {
        this.controller.start();
        System.out.println(this.controller.getAllTopology());
    }

    @Test
    void testHello() {

        Task task = new Task(new Node(), "1", new JSONObject());
        System.out.println(task.getState());
        service.changeState(task, State.FINISHED);

        System.out.println(task.getState());
    }
}
