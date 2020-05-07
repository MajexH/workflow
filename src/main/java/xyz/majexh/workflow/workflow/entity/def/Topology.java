package xyz.majexh.workflow.workflow.entity.def;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.utils.StringUtils;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

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
    @JsonIgnore
    private HashMap<String, Node> nodeMap;
    // topology的所有图谱关系
    private HashMap<String, List<String>> edgePair;

    public Topology() {
        this.id = StringUtils.getUUID();
        this.nodes = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        this.edgePair = new HashMap<>();
    }

    private void setSystemBarrier(String from, String to) {
        Node toNode = this.nodeMap.get(to);
        if (toNode.getType().isSameType(Type.SYSTEM_BARRIER)) {
            toNode.getSystemArgs().add(from);
        }
    }

    public void addEdge(String from, String to) {
        if (!this.nodeMap.containsKey(from) || !this.nodeMap.containsKey(to)) {
            log.debug(String.format("from:%s or to:%s has no definition", from, to));
            return;
        }
        setSystemBarrier(from, to);
        if (this.edgePair.containsKey(from)) {
            this.edgePair.get(from).add(to);
        } else {
            this.edgePair.put(from, new ArrayList<String>(){{
                add(to);
            }});
        }
    }

    public Node getNode(String nodeId) {
        return this.nodeMap.get(nodeId);
    }


    public List<String> getNextNodes(String nodeId) {
        if (!this.edgePair.containsKey(nodeId)) return new ArrayList<>();
        else return this.edgePair.get(nodeId);
    }
}
