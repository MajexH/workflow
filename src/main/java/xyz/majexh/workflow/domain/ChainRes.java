package xyz.majexh.workflow.domain;

import lombok.Data;
import xyz.majexh.workflow.workflow.workflowEnum.State;

@Data
public class ChainRes {
    private String topologyName;
    private String chainId;
    private State state;

    public ChainRes(String topologyName, String chainId, State state) {
        this.topologyName = topologyName;
        this.chainId = chainId;
        this.state = state;
    }
}
