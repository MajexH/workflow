package xyz.majexh.workflow.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;

import java.util.HashMap;

@Configuration
public class ControllerConfig {

    @Bean
    public HashMap<String, Chain> getChainMap() {
        return new HashMap<>();
    }

    @Bean
    public HashMap<String, Topology> getTopologyMap() {
        return new HashMap<>();
    }
}
