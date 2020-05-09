package xyz.majexh.workflow.workflow.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;

import java.util.HashMap;
import java.util.List;

@Component("jsonTopologyBuilder")
@Slf4j
public class JsonTopologyBuilder implements TopologyBuilder {

    @Autowired
    private NodeBuilder nodeBuilder;

    @Override
    public HashMap<String, Topology> loadTopologies() {
        return null;
    }

    public Topology loadTopology(JSONObject json) {
        List<Node> nodes = nodeBuilder.buildNodes(json.getJSONArray("nodes"));
        return getTopologyFromJson(json, nodes);
    }

    public Topology getTopologyFromJson(JSONObject json, List<Node> nodes) throws BaseException {
        Topology topology = JSONObject.parseObject(json.toString(), Topology.class);
        topology.setNodes(nodes);
        // 初始化nodeMap
        HashMap<String, Node> nodeMap = new HashMap<>();
        for (Node node : topology.getNodes()) {
            if (nodeMap.containsKey(node.getId())) {
                log.error("node节点出现重复id");
                throw new BaseException(ExceptionEnum.DUPLICATE_NODE_ID);
            }
            node.setTopologyId(topology.getId());
            nodeMap.put(node.getId(), node);
        }
        topology.setNodeMap(nodeMap);
        // 检查start end节点是否存在
        if (!nodeMap.containsKey(topology.getStartNodeId()) || !nodeMap.containsKey(topology.getEndNodeId())) {
            log.error("拓扑头结点或尾节点不存在");
            throw new BaseException(ExceptionEnum.TOPOLOGY_ARGS_NOT_CAPABLE);
        }
        // 初始化edges
        JSONArray edges = JSONArray.parseArray(json.getString("edges"));
        for (Object edge : edges) {
            List<String> temp = JSONArray.parseArray(edge.toString(), String.class);
            topology.addEdge(temp.get(0), temp.get(1));
        }
        return topology;
    }
}
