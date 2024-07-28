package org.software.code.common.except;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.software.code.common.result.Result;
import org.springframework.beans.BeansException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.software.code.common.except.ExceptionEnum.*;
import static org.software.code.common.except.ExceptionEnum.TOKEN_NOT_FIND;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * body数据验证
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Result<?>> handleValidationExceptions(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        Result<?> result = Result.failed(objectError.getDefaultMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数缺失
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Result<?>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        Result<?> result;
        if (e.getHeaderName().equals("Authorization")) {
            result = Result.failed(TOKEN_NOT_FIND.getMsg());
        } else {
            String errorMessage = String.format("参数%s缺失", e.getHeaderName());
            result = Result.failed(errorMessage);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数缺失
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<?>> handleMHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Result<?> result= Result.failed(REQUEST_PARAMETER_ERROR.getMsg());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }



    /**
     * 参数缺失
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String errorMessage = String.format("参数%s缺失", e.getParameterName());
        Result<?> result = Result.failed(errorMessage);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数空值
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<?>> handleConstraintViolationException(ConstraintViolationException e) {
        ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        Result<?> result = Result.failed(violation.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


    /**
     * Bean
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BeansException.class)
    public ResponseEntity<Result<?>> handleBeansExceptions(BeansException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(Result.failed(BEAN_FORMAT_ERROR.getMsg()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 服务通信
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Result<?>> handleFeignExceptions(FeignException ex) {
        ex.printStackTrace();
        Result<?> resultEx;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            resultEx = objectMapper.readValue(ex.contentUTF8(), Result.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Result.failed(FEIGN_EXCEPTION.getMsg()), HttpStatus.BAD_REQUEST);
        }
        Result<?> result = Result.failed(resultEx.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    /**
     * 自定义异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<?>> handleBusinessExceptions(BusinessException ex) {
        return new ResponseEntity<>(Result.failed(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 通用异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleExceptions(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(Result.failed(RUN_EXCEPTION.getMsg()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}