package com.lxw.handler;

import com.lxw.response.Result;
import com.lxw.utils.JSONAuthentication;
import com.lxw.response.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败
 */
@Component
public class MyAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Result result = null;
        if (exception instanceof AccountExpiredException) {
            //账号过期
            result = Result.error(ResultCode.USER_ACCOUNT_EXPIRED);
        } else if (exception instanceof BadCredentialsException) {
            //密码错误
            result = Result.error(ResultCode.USER_CREDENTIALS_ERROR);
        } else if (exception instanceof CredentialsExpiredException) {
            //密码过期
            result = Result.error(ResultCode.USER_CREDENTIALS_EXPIRED);
        } else if (exception instanceof DisabledException) {
            //账号不可用
            result = Result.error(ResultCode.USER_ACCOUNT_DISABLE);
        } else if (exception instanceof LockedException) {
            //账号锁定
            result = Result.error(ResultCode.USER_ACCOUNT_LOCKED);
        } else if (exception instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result = Result.error(ResultCode.USER_NOT_EXIST);
        }else{
            //其他错误
            result = Result.error(ResultCode.COMMON_FAIL);
        }

        //输出
        this.WriteJSON(request, response, result);

    }

}
