package com.chaos.handler.exception;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import com.chaos.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常:" + e);
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常: " + e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode() ,e.getMessage());
    }
}
