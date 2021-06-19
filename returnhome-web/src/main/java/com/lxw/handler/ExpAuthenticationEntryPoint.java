package com.lxw.handler;

import com.lxw.response.Result;
import com.lxw.utils.JSONAuthentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证异常
 */
@Component
public class ExpAuthenticationEntryPoint extends JSONAuthentication implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Result result = Result.error().message("认证异常 => 请走正确的认证流程");
        //处理编码方式，防止中文乱码
        this.WriteJSON(request, response, result);
    }


}
