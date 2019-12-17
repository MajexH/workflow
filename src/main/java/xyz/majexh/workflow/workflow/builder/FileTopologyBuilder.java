package xyz.majexh.workflow.workflow.builder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import xyz.majexh.workflow.utils.JSONUtils;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.entity.def.Topology;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * 从/resources/topology下读取所有的以json格式文件 每个json格式文件会被format成为一个JSONObject
 */
@Slf4j
@Component
public class FileTopologyBuilder implements TopologyBuilder {

    @Autowired
    private NodeBuilder nodeBuilder;

    @Override
    public HashMap<String, Topology> loadTopologies() {
        HashMap<String, Topology> topologies = new HashMap<>();
        try {
            File topology = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "topology");
            File[] files =  topology.listFiles((file, name) -> name.endsWith("json"));
            if (files == null) return topologies;
            for (File file : files) {
                log.debug(String.format("load topology from %s", file.getName()));
                JSONObject json = JSONUtils.jsonReader(new BufferedInputStream(new FileInputStream(file)));
                List<Node> nodes = nodeBuilder.buildNodes(json.getJSONArray("nodes"));
                Topology temp = getTopologyFromJson(json, nodes);
                topologies.put(temp.getName(), temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("读取topology预定义文件失败");
        }
        log.info("load all json file finished");
        return topologies;
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
