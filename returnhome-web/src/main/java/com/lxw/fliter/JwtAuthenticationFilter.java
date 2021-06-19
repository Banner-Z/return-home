package com.lxw.fliter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户账号的验证
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 验证账号密码的正确性
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        // 从输入流中获取到登录的信息
        //            JwtUser jwtUser = new ObjectMapper().readValue(request.getInputStream(), JwtUser.class);
//            System.out.println("test jwtUser"+jwtUser);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"));
        return authenticationManager.authenticate(authenticationToken);//让spring-security去进行验证，无需自己查数据库对比密码
    }

//    /**
//     * 如果验证成功，就生成token并返回
//     */
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//
//        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
//        JwtUser principal = (JwtUser) authResult.getPrincipal();
//        System.out.println("lxw1 :" + principal.toString());
//        String token = JwtTokenUtils.getToken(principal.getUsername(),principal.getAuthorities().toString());
//        //返回创建成功的token,按照jwt的规定，最后请求的格式应该是 `Bearer token`
////        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(JSON.toJSONString(Result.success().message("登录成功,欢迎您！").data("token",token)));
//
//    }

//    /**
//     * 验证失败
//     * @param request
//     * @param response
//     * @param failed
//     * @throws IOException
//     * @throws ServletException
//     */
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
////        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
//        response.getWriter().write(JSON.toJSONString(Result.error().message("密码错误 请重新输入！")));
//    }

}
