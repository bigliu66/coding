package com.lx.log.logrecord.aspect;

import com.alibaba.fastjson.JSON;
import com.lx.log.logrecord.annotation.LogRecordAnnotation;
import com.lx.log.logrecord.model.LogDTO;
import com.lx.log.logrecord.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author lx
 * @date 2021/12/6 13:29
 */
@Aspect
@Component
@Slf4j
public class LogRecordAspect implements Serializable {
    @Autowired
    private LogService logService;

    private static final ThreadLocal<List<LogDTO>> LOGDTO_THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal logDTOList");

    private final SpelExpressionParser parser = new SpelExpressionParser();

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Before("@annotation(com.lx.log.logrecord.annotation.LogRecordAnnotation) || @annotation(com.lx.log.logrecord.annotation.LogRecordAnnotations)")
    public void doBefore(JoinPoint joinPoint) {
        try {
            List<LogDTO> logDTOList = new ArrayList<>();
            LOGDTO_THREAD_LOCAL.set(logDTOList);

            Object[] arguments = joinPoint.getArgs();
            Method method = getMethod(joinPoint);
            LogRecordAnnotation[] annotations = method.getAnnotationsByType(LogRecordAnnotation.class);

            // 批量处理注解
            for (LogRecordAnnotation annotation : annotations) {
                // 初始化logDTO
                LogDTO logDTO = new LogDTO();
                logDTOList.add(logDTO);
                String bizNoSpel = annotation.bizNo();
                String operateSpel = annotation.operator();
                String bizNo = bizNoSpel;
                String operate = operateSpel;

                try {
                    String[] params = discoverer.getParameterNames(method);
                    EvaluationContext context = new StandardEvaluationContext();
                    if (params != null) {
                        for (int len = 0; len < params.length; len++) {
                            context.setVariable(params[len], arguments[len]);
                        }
                    }

                    // bizNo 处理：直接传入字符串会抛出异常，写入默认传入的字符串
                    if (StringUtils.isNotBlank(bizNoSpel)) {
                        Expression bizIdExpression = parser.parseExpression(bizNoSpel);
                        bizNo = bizIdExpression.getValue(context, String.class);
                    }
                    // userId 处理，写入默认传入的字符串
                    if (StringUtils.isNotBlank(operateSpel)) {
                        Expression msgExpression = parser.parseExpression(operateSpel);
                        operate = msgExpression.getValue(context, String.class);
                    }

                } catch (Exception e) {
                    log.error("SystemLogAspect doBefore error", e);
                } finally {
                    logDTO.setLogId(UUID.randomUUID().toString());
                    logDTO.setSuccess(true);
                    logDTO.setBizNo(bizNo);
                    logDTO.setOperator(operate);
                    logDTO.setContent(annotation.success());
                    logDTO.setCategory(annotation.category());
                    logDTO.setOperateDate(new Date());
                }
            }

        } catch (Exception e) {
            log.error("SystemLogAspect doBefore error", e);
        }
    }

    protected Method getMethod(JoinPoint joinPoint) {
        Method method = null;
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature ms = (MethodSignature) signature;
            Object target = joinPoint.getTarget();
            method = target.getClass().getMethod(ms.getName(), ms.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("SystemLogAspect getMethod error", e);
        }
        return method;
    }

    @Around("@annotation(com.lx.log.logrecord.annotation.LogRecordAnnotation) || @annotation(com.lx.log.logrecord.annotation.LogRecordAnnotations)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        try {
            result = pjp.proceed();
            // logDTO写入返回值信息 若方法抛出异常，则不会走入下方逻辑
            List<LogDTO> logDTOList = LOGDTO_THREAD_LOCAL.get();
            String returnStr = JSON.toJSONString(result);
            logDTOList.forEach(logDTO -> logDTO.setReturnStr(returnStr));
        } catch (Throwable throwable) {
            // logDTO写入异常信息
            List<LogDTO> logDTOList = LOGDTO_THREAD_LOCAL.get();
            logDTOList.forEach(logDTO -> {
                logDTO.setSuccess(false);
                logDTO.setException(throwable.getMessage());
            });
            throw throwable;
        } finally {
            // logDTO发送至数据管道
            List<LogDTO> logDTOList = LOGDTO_THREAD_LOCAL.get();
            logDTOList.forEach(logDTO -> {
                try {
                    logService.createLog(logDTO);
                } catch (Throwable throwable) {
                    log.error("logRecord send message failure", throwable);
                }
            });
            LOGDTO_THREAD_LOCAL.remove();
        }
        return result;
    }
}
