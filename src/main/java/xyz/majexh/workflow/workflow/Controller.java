package xyz.majexh.workflow.workflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.workflow.builder.TopologyBuilder;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.receiver.Receiver;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
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
    private ConcurrentHashMap<String, Topology> topologies;
    private ConcurrentHashMap<String, Chain> chainMap;
    private MessageController zmqMessageControllerImpl;
    private Receiver receiver;
    private TopologyBuilder builder;

    @Autowired
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Autowired
    public void setTopologies(ConcurrentHashMap<String, Topology> topologies) {
        this.topologies = topologies;
    }

    @Autowired
    public void setChainMap(ConcurrentHashMap<String, Chain> chainMap) {
        this.chainMap = chainMap;
    }

    @Autowired
    public void setZmqMessageControllerImpl(MessageController zmqMessageControllerImpl) {
        this.zmqMessageControllerImpl = zmqMessageControllerImpl;
    }

    @Autowired
    public void setBuilder(TopologyBuilder builder) {
        this.builder = builder;
    }

    private void loadTopology() {
        this.topologies.putAll(this.builder.loadTopologies());
    }

    public void createChain(String name) {
        Chain chain = new Chain(this.topologies.get(name));
        this.chainMap.put(chain.getId(), chain);
        log.info(String.format("%s chain has been created", chain.getId()));
    }

    private void recv() {
        ex.submit(() -> {
            while (true) {
                receiver.receiveMessage();
            }
        });
    }

    public void submitTask() {

    }

    public void restartTask() {

    }

    public void monitor() {

    }
}
