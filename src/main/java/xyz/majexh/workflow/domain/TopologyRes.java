package xyz.majexh.workflow.domain;

import lombok.Data;

@Data
public class TopologyRes {

    private String id;
    private String name;
    private String startNodeId;
    private String endNodeId;

    public TopologyRes(String id, String name, String startNodeId, String endNodeId) {
        this.id = id;
        this.name = name;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
    }
}
