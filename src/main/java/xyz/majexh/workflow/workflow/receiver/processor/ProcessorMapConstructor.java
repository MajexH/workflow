package xyz.majexh.workflow.workflow.receiver.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import xyz.majexh.workflow.annotations.ProcessorTypeAnnotation;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class ProcessorMapConstructor implements InitializingBean {

    public ConcurrentHashMap<Type, Processor> processorMap;

    private ApplicationContext applicationContext;

    @Autowired
    public void setProcessorMap(ConcurrentHashMap<Type, Processor> processorMap) {
        this.processorMap = processorMap;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        log.debug("start scan processor implements");
        // 拿到所有的实现processor接口的类名
        for (String name : getCandidateProcessorNames()) {
            registerProcessor(this.applicationContext.getType(name));
        }
        log.debug(String.format("processors added: %s", this.processorMap));
    }

    private String[] getCandidateProcessorNames() {
        return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, Processor.class);
    }

    private void registerProcessor(Class<?> clazz) {
        ProcessorTypeAnnotation annotation = AnnotationUtils.getAnnotation(clazz, ProcessorTypeAnnotation.class);
        if (annotation == null) return;
        this.processorMap.put(annotation.value(), getProcessorBean(clazz));
        log.debug(String.format("%s has been added to processorMap", clazz));
    }

    private Processor getProcessorBean(Class<?> type) {
        return (Processor) this.applicationContext.getBean(type);
    }
}
