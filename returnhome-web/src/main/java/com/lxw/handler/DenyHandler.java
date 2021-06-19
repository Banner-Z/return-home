package com.lxw.handler;

import com.lxw.response.Result;
import com.lxw.utils.JSONAuthentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足处理
 */
@Component
public class DenyHandler extends JSONAuthentication implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        Result result = Result.error().message("权限不足 => 请在自己的权限范围内做事");
        //处理编码方式，防止中文乱码
        this.WriteJSON(request, response, result);
    }

}
