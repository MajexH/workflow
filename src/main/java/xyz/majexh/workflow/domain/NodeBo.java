package xyz.majexh.workflow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.majexh.workflow.workflow.entity.def.Node;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeBo {
    private Node node;
    private String topologyName;
}
