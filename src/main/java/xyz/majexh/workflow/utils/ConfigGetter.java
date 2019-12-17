package xyz.majexh.workflow.utils;

public class ConfigGetter {

    private static WorkflowConfigGetter getter = SpringBeanGetter.getBean("workflowConfigGetter", WorkflowConfigGetter.class);

    public static int getRetryMax() {
        return getter.getRetryMax();
    }

    public static double getRetryDelta() {
        return getter.getRetryDelta();
    }
}
