package org.software.code.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseExceptionAspect {

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.HealthCodeMapper.*(..))", throwing = "ex")
    public void handleDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("addHealthCode")) {
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_INSERT_FAIL);
        } else if (methodName.equals("updateColorByUID")) {
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_UPDATE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.HEALTH_CODE_SELECT_FAIL);
        }
    }
}
