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

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.NucleicAcidTestMapper.*(..))", throwing = "ex")
    public void handleNucleicAcidTestMapperDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("insertTestRecord")) {
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_INSERT_FAIL);
        } else if (methodName.equals("updateTestRecord") || methodName.equals("updateRetestStatus")) {
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_INSERT_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_SELECT_FAIL);
        }
    }
}
