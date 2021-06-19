package com.lxw.handler;

import com.lxw.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lxw.response.ResultCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice//basePackages：指定包
@ResponseBody //这里要加@ResponseBody 不然返回的不是一个json数据 不能被RestController识别
//@RestControllerAdvice //等于@ControllerAdvice +@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 全局异常处理 不指定异常类型
     */
    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error().message("发生了未知错误！");
    }

    /**
     * 算术异常
     */
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        log.error(e.getMessage());
        return Result.error().code(ResultCode.ARITHMETIC_EXCEPTION.getCode()).
                message(ResultCode.ARITHMETIC_EXCEPTION.getMessage());
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result error(BusinessException e){
        log.error(e.getErrMsg());
        return Result.error().code(e.getCode()).message(e.getErrMsg());
    }

    /**
     * 请求参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result error(MissingServletRequestParameterException e){
        log.error(e.getMessage());
        return Result.error().message("请求参数缺失");
    }

    /**
     * 请求参数缺失
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Result error(MissingServletRequestPartException e){
        log.error(e.getMessage());
        return Result.error().message("请求参数缺失");
    }

    /**
     * 请求方法错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleException(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage());
        return Result.error().message("请求方法错误！");
    }


    /**
     * 操作数据库出现异常:名称重复，外键关联
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleException(DataIntegrityViolationException e) {
        e.printStackTrace();
        log.error("操作数据库出现异常:", e.getMessage());
        return Result.error().message("操作数据库出现异常：例如字段重复等问题");
    }


    /**
     * 上传文件异常
     */
    @ExceptionHandler(MultipartException.class)
    public Result handleException(MultipartException e) {
        log.error( e.getMessage());
        return Result.error().message("上传文件异常");
    }


    /**
     * 权限异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handleException(AccessDeniedException e) {
        log.error(e.getMessage());
        return Result.error().message("权限不足 =》 请在权限范围内做事");
    }








}
