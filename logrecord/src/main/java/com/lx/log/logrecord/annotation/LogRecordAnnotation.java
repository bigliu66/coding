package com.lx.log.logrecord.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Repeatable(LogRecordAnnotations.class)
public @interface LogRecordAnnotation {
    // 操作日志文本模板
    String success();
    // 操作失败文本模板
    String fail() default "";
    // 操作人
    String operator() default "";
    // 业务识别号
    String bizNo();
    // 操作种类
    String category() default "";
    // 操作明细
    String detail() default "";
    // 记录日志条件
    String condition() default "";
}