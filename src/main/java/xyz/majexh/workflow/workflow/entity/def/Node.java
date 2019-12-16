package xyz.majexh.workflow.workflow.entity.def;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.Utils.ConfigGetter;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.List;
import java.util.UUID;

@Data
public class Node {

    private String id;
    private String name;
    // handle name
    private String handle;
    // 当前节点所属的topology
    private String topologyId;
    // 当前节点的输入和输出参数名
    private List<String> inputParams;
    private List<String> outputParams;

    private int retryMax;
    private double retryDelta;

    private Type type;

    public Node() {
        this.retryMax = ConfigGetter.getRetryMax();
        this.retryDelta = ConfigGetter.getRetryDelta();
        // 默认构造一个属于user的节点
        this.type = Type.USER;
    }

    public Node(Type type) {
        this();
        this.type = type;
    }
}
