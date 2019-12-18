package xyz.majexh.workflow.annotations;

import org.springframework.core.annotation.AliasFor;
import xyz.majexh.workflow.workflow.workflowEnum.Type;

import java.lang.annotation.*;

/**
 * Processor的类型注解
 * 利用翻身 构建Node type的HashMap
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ProcessorTypeAnnotation {

    @AliasFor("type")
    Type value() default Type.USER;

    @AliasFor("value")
    Type type() default Type.USER;
}
