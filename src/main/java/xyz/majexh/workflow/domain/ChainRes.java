package xyz.majexh.workflow.domain;

import lombok.Data;

@Data
public class ChainRes {
    private String topologyName;
    private String chainId;

    public ChainRes(String topologyName, String chainId) {
        this.topologyName = topologyName;
        this.chainId = chainId;
    }
}
