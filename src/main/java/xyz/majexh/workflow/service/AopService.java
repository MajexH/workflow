package xyz.majexh.workflow.service;

import org.springframework.stereotype.Service;
import xyz.majexh.workflow.workflow.workflowEnum.State;

import java.lang.reflect.Method;

/**
 * 因为domain类是不受spring管辖的 因此不能通过SpringAop的方式去调用AOP
 * 虽然可以通过ProxyFactory去生成代理类
 * 但是这样就需要侵入到类里面 才行
 * 因此为了改的最少 就这样做吧
 */
@Service
public class AopService {

    public <T> void changeState(T obj, State state) {
        try {
            Method method = obj.getClass().getMethod("changeState", State.class);
            method.invoke(obj, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
