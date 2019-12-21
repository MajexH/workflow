package xyz.majexh.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.worker.Worker;
import xyz.majexh.workflow.workflow.Controller;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.executors.ChainExecutor;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class Test {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ChainExecutor executor;

    @Autowired
    Controller controller;


    @org.junit.jupiter.api.Test
    public void test() throws InterruptedException {
        controller.start();
        ExecutorService ex = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });

        for (int i = 0; i < 1; i++)
            ex.submit(applicationContext.getBean("worker", Worker.class));
        for (int i = 0; i < 1; i++) {
            Chain c = controller.createChain("GNSS");

            Task task = executor.getFirstTask(c, JSONUtils.hashMap2Json(new HashMap<>() {{
                Random random = new Random(new Date().getTime());
                put("a", random.nextInt());
                put("b", random.nextInt());
            }}));
            controller.submitTask(task);
        }
        Thread.sleep(3000000);
    }

}
