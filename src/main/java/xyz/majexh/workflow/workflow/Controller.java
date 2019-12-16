package xyz.majexh.workflow.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.message.Message;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class Controller {

    private ExecutorService ex = Executors.newCachedThreadPool((runnable) -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });
    private HashMap<String, Topology> topologies;
    private HashMap<String, Chain> chainMap;
    private Message zmqMessageImpl;

    @Autowired
    public void setTopologies(HashMap<String, Topology> topologies) {
        this.topologies = topologies;
    }

    @Autowired
    public void setChainMap(HashMap<String, Chain> chainMap) {
        this.chainMap = chainMap;
    }

    @Autowired
    public void setZmqMessageImpl(Message zmqMessageImpl) {
        this.zmqMessageImpl = zmqMessageImpl;
    }

    public void submitTask() {

    }

    public void restartTask() {

    }

    public void monitor() {

    }
}
