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

    @AfterThrowing(pointcut = "execution(* org.software.code.dao.PlaceInfoDao.*(..))", throwing = "ex")
    public void handlePlaceInfoDatabaseException(JoinPoint joinPoint, Throwable ex) {
        // 打印堆栈跟踪
        ex.printStackTrace();

        // 获取方法名称
        String methodName = joinPoint.getSignature().getName();

        // 记录异常信息
        logger.error("Exception occurred in PlaceInfoMapper method: {}, message: {}", methodName, ex.getMessage());

        // 根据方法名抛出相应的业务异常
        if (methodName.equals("insertPlace")) {
            throw new BusinessException(ExceptionEnum.PLACE_CODE_INSERT_FAIL);
        } else if (methodName.equals("updatePlaceStatusByPid")) {
            throw new BusinessException(ExceptionEnum.PLACE_CODE_UPDATE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.PLACE_CODE_SELECT_FAIL);
        }
    }

    @AfterThrowing(pointcut = "execution(* org.software.code.dao.PlaceMappingDao.*(..))", throwing = "ex")
    public void handlePlaceMappingDatabaseException(JoinPoint joinPoint, Throwable ex) {
        // 打印堆栈跟踪
        ex.printStackTrace();

        // 获取方法名称
        String methodName = joinPoint.getSignature().getName();

        // 记录异常信息
        logger.error("Exception occurred in PlaceMappingMapper method: {}, message: {}", methodName, ex.getMessage());

        // 根据方法名抛出相应的业务异常
        if (methodName.equals("addUserInfo")) {
            throw new BusinessException(ExceptionEnum.USER_PLACE_CODE_INSERT_FAIL);
        } else if (methodName.equals("updateUserInfo")) {
            throw new BusinessException(ExceptionEnum.USER_PLACE_CODE_UPDATE_FAIL);
        } else if (methodName.equals("deleteById")) {
            throw new BusinessException(ExceptionEnum.USER_PLACE_CODE_DELETE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.USER_PLACE_CODE_SELECT_FAIL);
        }
    }
}