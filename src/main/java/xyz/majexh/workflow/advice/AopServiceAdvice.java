package xyz.majexh.workflow.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AopServiceAdvice {

    @After("execution(* xyz.majexh.workflow.service.AopService.changeState(..))")
    public void after(JoinPoint joinPoint) {
        // 可以从joinPoint里面拿去函数的数据
        System.out.println("aop service after -- test");
    }
}
