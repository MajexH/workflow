package xyz.majexh.workflow.workflow.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.majexh.workflow.workflow.entity.def.Node;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JSONNodeBuilder implements NodeBuilder {

    @Override
    public List<Node> buildNodes(JSONArray json) {
        List<Node> res = new ArrayList<>();
        for (Object obj : json) {
            JSONObject jsonNode = JSON.parseObject(obj.toString());
            Node node = JSON.parseObject(obj.toString(), Node.class);
            String type = jsonNode.getString("type");
            if (type != null)
                node.setType(chooseType(type, jsonNode.getString("name")));
            res.add(node);
        }
        return res;
    }

    private Type chooseType(String type, String name) {
        switch (type) {
            case "system":
                switch (name) {
                    case "barrier":
                        return Type.SYSTEM_BARRIER;
                    default:
                        return Type.SYSTEM_NONE;
                }
            case "user":
                return Type.USER;
            default:
                return Type.USER;
        }
    }
}
