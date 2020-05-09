package xyz.majexh.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.majexh.workflow.domain.NodeBo;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;
import xyz.majexh.workflow.service.ControllerService;
import xyz.majexh.workflow.workflow.builder.JsonTopologyBuilder;
import xyz.majexh.workflow.workflow.builder.TopologyBuilder;
import xyz.majexh.workflow.workflow.entity.def.Topology;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/workflow")
public class TopologyController {

    private ControllerService controllerService;
    private JsonTopologyBuilder builder;
    private ConcurrentHashMap<String, Topology> topologies;

    @Autowired
    public void setTopologies(ConcurrentHashMap<String, Topology> topologies) {
        this.topologies = topologies;
    }

    @Autowired
    public void setControllerService(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @Autowired
    @Qualifier("jsonTopologyBuilder")
    public void setBuilder(JsonTopologyBuilder builder) {
        this.builder = builder;
    }

    @GetMapping("/allTopology")
    public ResponseEntity<HashMap<String, Object>> getAllTopology() throws Exception {
        return ResEntity.okDefault(this.controllerService.getAllTopology());
    }

    @GetMapping("/topology")
    public ResponseEntity<HashMap<String, Object>> getTopology(String topologyName) throws Exception {
        Topology res = this.controllerService.getTopologyByName(topologyName);
        System.out.println(res);
        if (res == null) {
            // 空 返回一个空的topology
            return ResEntity.okDefault(new Topology());
        }
        return ResEntity.okDefault(this.controllerService.getTopologyByName(topologyName));
    }

    @PostMapping(path = "/topology")
    public ResponseEntity<HashMap<String, Object>> updateTopology(@RequestBody NodeBo nodeBo) throws Exception {
        this.controllerService.updateNodeOfTopologyByTopologyName(nodeBo.getTopologyName(), nodeBo.getNode());
        return ResEntity.okDefault(null);
    }

    // 启动topology 需要传入第一个任务 及其任务的输入参数
    @PostMapping(path = "/startTopology")
    @SuppressWarnings("unchecked")
    public ResponseEntity<HashMap<String, Object>> startTopology(@RequestBody HashMap<String, Object> input) throws Exception {
        if (!input.containsKey("topologyName") || !input.containsKey("inputParams")) {
            throw new BaseException(ExceptionEnum.WRONG_PARAMS);
        }
        String chainId = this.controllerService.startTopologyByName((String) input.get("topologyName"), new JSONObject((HashMap<String, Object>) input.get("inputParams")));
        return ResEntity.okDefault(new HashMap<String, Object>(){{
            put("chainId", chainId);
        }});
    }

    @PostMapping(path = "/addTopology")
    public ResponseEntity<HashMap<String, Object>> addTopology(@RequestBody String json) {
        Topology temp = builder.loadTopology(JSON.parseObject(json));
        this.topologies.put(temp.getName(), temp);
        return ResEntity.okDefault(new HashMap<String, Object>(){{
            put("topologyId", temp.getId());
        }});
    }
}
