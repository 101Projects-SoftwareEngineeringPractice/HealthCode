package org.software.code.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseExceptionAspect {

    private static final Logger logger = LogManager.getLogger(DatabaseExceptionAspect.class);

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.UserInfoMapper.*(..))", throwing = "ex")
    public void handleUserInfoDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in UserInfoMapper method: {}, message: {}", methodName, ex.getMessage());

        if (methodName.equals("addUserInfo")) {
            throw new BusinessException(ExceptionEnum.USER_INSERT_FAIL);
        } else if (methodName.equals("updateUserInfo")) {
            throw new BusinessException(ExceptionEnum.USER_UPDATE_FAIL);
        } else if (methodName.equals("deleteById")) {
            throw new BusinessException(ExceptionEnum.USER_DELETE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.USER_SELECT_FAIL);
        }
    }

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.UidMappingMapper.*(..))", throwing = "ex")
    public void handleUidMappingDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in UidMappingMapper method: {}, message: {}", methodName, ex.getMessage());

        if (methodName.equals("addUserMapping")) {
            throw new BusinessException(ExceptionEnum.USER_BIND_INSERT_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.USER_BIND_SELECT_FAIL);
        }
    }

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.NucleicAcidTestPersonnelMapper.*(..))", throwing = "ex")
    public void handleAcidTestPersonnelDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in NucleicAcidTestPersonnelMapper method: {}, message: {}", methodName, ex.getMessage());

        if (methodName.equals("addNucleicAcidTestPersonnel")) {
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_USER_INSERT_FAIL);
        } else if (methodName.equals("updateStatusByTID")) {
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_USER_UPDATE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.NUCLEIC_ACID_TEST_USER_SELECT_FAIL);
        }
    }

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.HealthCodeMangerMapper.*(..))", throwing = "ex")
    public void handleHealthCodeMangerDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        logger.error("Exception occurred in HealthCodeMangerMapper method: {}, message: {}", methodName, ex.getMessage());

        if (methodName.equals("addHealthCodeManager")) {
            throw new BusinessException(ExceptionEnum.MANAGER_USER_INSERT_FAIL);
        } else if (methodName.equals("updateStatusByMID")) {
            throw new BusinessException(ExceptionEnum.MANAGER_USER_UPDATE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.MANAGER_USER_SELECT_FAIL);
        }
    }
}