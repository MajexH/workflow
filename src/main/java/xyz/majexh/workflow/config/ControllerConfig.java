package xyz.majexh.workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.receiver.processor.Processor;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ControllerConfig {

    @Bean
    public ConcurrentHashMap<String, Chain> getChainMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<String, Topology> getTopologyMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ConcurrentHashMap<Type, Processor> getProcessorMap() {
        return new ConcurrentHashMap<>();
    }
}
