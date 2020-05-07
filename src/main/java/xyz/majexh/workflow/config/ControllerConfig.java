package xyz.majexh.workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.majexh.workflow.workflow.entity.def.Topology;
import xyz.majexh.workflow.workflow.entity.message.MessageBody;
import xyz.majexh.workflow.workflow.entity.running.Chain;
import xyz.majexh.workflow.workflow.entity.running.Task;
import xyz.majexh.workflow.workflow.receiver.processor.Processor;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class ControllerConfig {

    @Bean
    public LinkedBlockingQueue<MessageBody> getResQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public LinkedBlockingQueue<Task> getTaskQueue() {
        return new LinkedBlockingQueue<>();
    }

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
