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

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.ItineraryCodeMapper.*(..))", throwing = "ex")
    public void handleItineraryCodeDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("addItineraryCodeDao")) {
            throw new BusinessException(ExceptionEnum.ITINERARY_CODE_INSERT_FAIL);
        } else if (methodName.equals("deleteItineraryCodeBeforeTime")) {
            throw new BusinessException(ExceptionEnum.ITINERARY_CODE_DELETE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.ITINERARY_CODE_SELECT_FAIL);
        }
    }
}
