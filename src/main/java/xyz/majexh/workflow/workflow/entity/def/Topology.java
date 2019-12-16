package xyz.majexh.workflow.workflow.entity.def;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
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

    public Topology() {
        this.id = UUID.randomUUID().toString();
        this.nodes = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        this.edgePair = new HashMap<>();
    }

    public void addEdge(String from, String to) {
        if (!this.nodeMap.containsKey(from) || !this.nodeMap.containsKey(to)) {
            log.debug(String.format("from:%s or to:%s has no definition", from, to));
            return;
        }
        if (this.edgePair.containsKey(from)) {
            this.edgePair.get(from).add(to);
        } else {
            this.edgePair.put(from, new ArrayList<>(){{
                add(to);
            }});
        }
    }

    public Node getNode(String nodeId) {
        return this.nodeMap.get(nodeId);
    }
}
