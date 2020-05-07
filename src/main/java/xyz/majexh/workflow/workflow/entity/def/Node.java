package xyz.majexh.workflow.workflow.entity.def;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.majexh.workflow.utils.ConfigGetter;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public class Node {

    private String id;
    private String name;
    // 当前节点需要使用的 远程服务 的名称
    private String handle;
    // 当前节点所属的topology
    @JsonIgnore
    private String topologyId;
    // 当前节点的输入和输出参数名
    private List<String> inputParams;
    private List<String> outputParams;
    @JsonIgnore
    private List<String> systemArgs;

    private int retryMax;
    private double retryDelta;

    private Type type;

    public Node() {
        this.retryMax = ConfigGetter.getRetryMax();
        this.retryDelta = ConfigGetter.getRetryDelta();
        this.inputParams = new ArrayList<>();
        this.outputParams = new ArrayList<>();
        this.systemArgs = new ArrayList<>();
        // 默认构造一个属于user的节点
        this.type = Type.USER;
    }

    public Node(Type type) {
        this();
        this.type = type;
    }

    public boolean checkInputParams(JSON inputParams) {
        HashMap<String, Object> inputParamsMap = JSONUtils.json2HashMap(inputParams);
        for (String inputParam : this.inputParams) {
            if (!inputParamsMap.containsKey(inputParam)) {
                log.error(String.format("%s param not exists", inputParam));
                return false;
            }
        }
        return true;
    }

    public void updateNode(Node node) {
        this.name = node.name;
        this.handle = node.handle;
        this.inputParams = node.inputParams;
        this.outputParams = node.outputParams;
        this.retryMax = node.retryMax;
        this.retryDelta = node.retryDelta;
//        this.type = node.type;
    }

}
