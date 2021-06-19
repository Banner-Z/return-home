package com.lxw.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxw.entity.Family;
import com.lxw.entity.User;
import com.lxw.entity.Volunteer;
import com.lxw.response.Result;
import com.lxw.entity.JwtUser;
import com.lxw.system.service.FamilyService;
import com.lxw.system.service.UserRoleService;
import com.lxw.system.service.UserService;
import com.lxw.system.service.VolunteerService;
import com.lxw.utils.JSONAuthentication;
import com.lxw.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 如果登录成功，就生成token并返回
 */
@Component
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private VolunteerService volunteerService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        JwtUser principal = (JwtUser) authentication.getPrincipal();
        String token = JwtTokenUtils.getToken(principal.getUsername(),principal.getAuthorities().toString());
        //返回创建成功的token,按照jwt的规定，最后请求的格式应该是 `Bearer token`
//        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);

        String role = "";
        //判断权限  如果是志愿者 做如下处理
        for (GrantedAuthority authority : principal.getAuthorities()) {
            role = authority.getAuthority();
            if(authority.getAuthority().equals("ROLE_VOLUNTEER")){
                String openid = request.getParameter("openid");
                if(openid != null && !openid.isEmpty()) {
                    Volunteer volunteer = new Volunteer();
                    volunteer.setVolunteerId(principal.getVounteerId());
                    volunteer.setOpenid(openid);
                    volunteerService.updateById(volunteer);
                }
                Result result = Result.success().message("登录成功,欢迎您,志愿者！").data("volunteerId",principal.getVounteerId()).data("token",token);
                this.WriteJSON(request, response, result);
                return;
            }
        }

        //工作人员登录
        Result result = Result.success().message("欢迎您,工作人员！").data("token",token)
                .data("role",role);
        this.WriteJSON(request, response, result);
    }

}
