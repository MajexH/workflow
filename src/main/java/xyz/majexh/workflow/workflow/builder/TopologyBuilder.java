package xyz.majexh.workflow.workflow.builder;

import xyz.majexh.workflow.workflow.entity.def.Topology;

import java.util.HashMap;

public interface TopologyBuilder {

    HashMap<String, Topology> loadTopologies();
}
