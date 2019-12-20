package xyz.majexh.workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.message.MessageEntity;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.receiver.processor.Processor;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

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

    @Bean
    public LinkedBlockingQueue<Task> getLinkedQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public LinkedBlockingQueue<MessageEntity> getMessageQueue() {
        return new LinkedBlockingQueue<>();
    }
}
