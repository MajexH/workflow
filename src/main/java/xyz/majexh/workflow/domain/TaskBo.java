package xyz.majexh.workflow.domain;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.majexh.workflow.workflow.entity.def.Node;

import java.util.HashMap;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskBo {

    private String id;
    private Node node;

    private HashMap<String, Object> inputParams;

    private int retry;
}
