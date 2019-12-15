package xyz.majexh.workflow.workflow.entity.def;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Data
@Component
@Scope("prototype")
public class Topology {

    private String id;
    private String name;
    private List<Node> nodes;
    private String startNodeId;
    private String endNodeId;
    // topology的所有节点
    private HashMap<String, Node> nodeMap;
    // topology的所有图谱关系
    private HashMap<String, List<String>> edgePair;
}
