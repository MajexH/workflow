package xyz.majexh.workflow.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.builder.TopologyBuilder;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.executors.ChainExecutor;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.receiver.Receiver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private MessageController messageControllerImpl;
    private Receiver receiver;
    private TopologyBuilder builder;
    private ChainExecutor executor;

    @Autowired
    public void setExecutor(ChainExecutor executor) {
        this.executor = executor;
    }

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
    public void setZmqMessageControllerImpl(MessageController messageControllerImpl) {
        this.messageControllerImpl = messageControllerImpl;
    }

    @Autowired
    public void setBuilder(TopologyBuilder builder) {
        this.builder = builder;
    }

    private Chain createChain(String name) {
        Chain chain = new Chain(this.topologies.get(name));
        this.chainMap.put(chain.getId(), chain);
        log.info(String.format("%s chain has been created", chain.getId()));
        this.putChain(chain);
        return chain;
    }

    private void loadTopology() {
        this.topologies.putAll(this.builder.loadTopologies());
    }

    private void startRecv() {
        ex.submit(() -> {
            while (true) {
                receiver.receiveMessage();
            }
        });
    }

    public void putChain(Chain chain) {
        this.chainMap.put(chain.getId(), chain);
    }

    public Chain getChain(String chainId) {
        if (!this.chainMap.containsKey(chainId)) {
            // TODO: throw Exception
            return null;
        }
        return this.chainMap.get(chainId);
    }

    public List<Chain> getAllChain() {
        return new ArrayList<>(this.chainMap.values());
    }

    public List<Task> getAllTask(String chainId) {
        return new ArrayList<>(this.chainMap.get(chainId).getTaskMap().values());
    }

    /**
     * load预定义的json文件
     * 同事启动接受线程
     * 同时把start读取的topology 每个启动一个副本
     */
    public void start() {
        this.loadTopology();
        this.startRecv();
    }

    public void startTopologyByName(String name, @Nullable JSON inputParams) {
        Chain chain = this.createChain(name);
        if (inputParams == null) {
            inputParams = new JSONObject();
        }
        Task task = this.executor.getFirstTask(chain, inputParams);
        // 启动任务
        this.submitTask(task.getId());
    }

    // TODO: 针对第一个任务的类型 也要调用相应的节点判断逻辑 和 节点的运行逻辑
    public void submitTask(String taskId) {
        Chain chain = this.getChain(StringUtils.extractChainIdFromTaskId(taskId));
        this.messageControllerImpl.putTask(chain.getTask(taskId));
    }

    public void restartTask(String taskId) {
        Chain chain = this.getChain(StringUtils.extractChainIdFromTaskId(taskId));
        Task task = chain.getTask(taskId);
        if (task == null) {
            log.error(String.format("the task %s submitted to restart is not found", taskId));
        }
        this.submitTask(taskId);
    }
}
