package xyz.majexh.workflow;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    @Around("execution(* xyz.majexh.workflow.Test.*(..))")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            System.out.println("before");
            Object obj = joinPoint.proceed();
            System.out.println("after");
            return obj;
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("around");
        }
    }
}
