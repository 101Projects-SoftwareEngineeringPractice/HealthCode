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

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.PlaceInfoMapper.*(..))", throwing = "ex")
    public void handlePlaceInfoDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("insertPlace")) {
            throw new BusinessException(ExceptionEnum.PLACE_CODE_INSERT_FAIL);
        } else if (methodName.equals("updatePlaceStatusByPid")) {
            throw new BusinessException(ExceptionEnum.PLACE_CODE_UPDATE_FAIL);
        } else {
            throw new BusinessException(ExceptionEnum.PLACE_CODE_SELECT_FAIL);
        }
    }

    @AfterThrowing(pointcut = "execution(* org.software.code.mapper.PlaceMappingMapper.*(..))", throwing = "ex")
    public void handlePlaceMappingDatabaseException(JoinPoint joinPoint, Throwable ex) {
        ex.printStackTrace();
        String methodName = joinPoint.getSignature().getName();
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
