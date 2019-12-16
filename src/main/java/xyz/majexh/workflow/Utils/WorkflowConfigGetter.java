package xyz.majexh.workflow.Utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "workflow")
public class WorkflowConfigGetter {

    private int retryMax;
    private double retryDelta;

    public int getRetryMax() {
        return retryMax;
    }

    public void setRetryMax(int retryMax) {
        this.retryMax = retryMax;
    }

    public double getRetryDelta() {
        return retryDelta;
    }

    public void setRetryDelta(double retryDelta) {
        this.retryDelta = retryDelta;
    }
}
