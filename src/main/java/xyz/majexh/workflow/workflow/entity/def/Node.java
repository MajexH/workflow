package xyz.majexh.workflow.workflow.entity.def;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.List;
import java.util.UUID;

@Data
@Component
@Scope("prototype")
public class Node {

    private String id;
    private String name;
    // handle name
    private String handle;
    private String topologyId;
    // 当前节点的输入和输出参数名
    private List<String> inputParams;
    private List<String> outputParams;

    private int maxRetry = 5;
    // 重试间隔
    // TODO: 从config里面拿取
    private int retryDelta;

    private Type type;

    public Node() {
        this.id = UUID.randomUUID().toString();
    }
}
