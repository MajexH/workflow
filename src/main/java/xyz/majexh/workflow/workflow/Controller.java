package xyz.majexh.workflow.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.builder.TopologyBuilder;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.executors.ChainExecutor;
import xyz.majexh.workflow.workflow.message.MessageController;
import xyz.majexh.workflow.workflow.receiver.Receiver;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class Controller implements ApplicationRunner {

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

    private Chain createChain(String name) throws Exception {
        if (!this.topologies.containsKey(name)) throw new BaseException(ExceptionEnum.WRONG_TOPOLOGY_NAME);
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

    public List<Topology> getAllTopology() {
        return new ArrayList<>(this.topologies.values());
    }

    /**
     * 现在topologies的map，key为topology的name
     * @param topologyName
     * @return
     */
    public Topology getTopologyByName(String topologyName) {
        return this.topologies.get(topologyName);
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

    public String startTopologyByName(String name, @Nullable JSON inputParams) throws Exception {
        Chain chain = this.createChain(name);
        if (inputParams == null) {
            inputParams = new JSONObject();
        }
        Task task = this.executor.getFirstTask(chain, inputParams);
        // 启动任务
        this.submitTask(task.getId());
        return chain.getId();
    }

    // TODO: 针对第一个任务的类型 也要调用相应的节点判断逻辑 和 节点的运行逻辑
    public void submitTask(String taskId) {
        Chain chain = this.getChain(StringUtils.extractChainIdFromTaskId(taskId));
        this.messageControllerImpl.putTask(chain.getTask(taskId));
    }

    public void restartTask(String taskId) throws BaseException {
        Chain chain = this.getChain(StringUtils.extractChainIdFromTaskId(taskId));
        if (chain == null) {
            log.error("chain {} not found", StringUtils.extractChainIdFromTaskId(taskId));
            throw new BaseException(ExceptionEnum.CHAIN_NOT_FOUND);
        }
        Task task = chain.getTask(taskId);
        if (task == null) {
            log.error(String.format("the task %s submitted to restart is not found", taskId));
            throw new BaseException(ExceptionEnum.TASK_NOT_FOUND);
        }
        if (!task.getState().equals(State.FAIL) && !task.getState().equals(State.FINISHED)) {
            throw new BaseException(ExceptionEnum.WRONG_RESUBMIT_TASK_STATE);
        }
        this.submitTask(taskId);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("controller start up");
        this.start();
    }
}
