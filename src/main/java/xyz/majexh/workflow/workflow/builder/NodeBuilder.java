package xyz.majexh.workflow.workflow.builder;

import com.alibaba.fastjson.JSONArray;
import xyz.majexh.workflow.workflow.entity.def.Node;

import java.util.List;

public interface NodeBuilder {
    List<Node> buildNodes(JSONArray json);
}
