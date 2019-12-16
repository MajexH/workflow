package xyz.majexh.workflow.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanGetter implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(SpringBeanGetter.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext application) throws BeansException {
        applicationContext = application;
        logger.info("applicationContext加载完毕");
    }

    public static <T>T getBean(String id, Class<T> type) {
        return applicationContext.getBean(id, type);
    }
}
