package com.lxw.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.*;

/**
 * jwt工具类（对jwt封装 方便后续调用）
 */
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET = "jwtsecretdemo";

    // 过期时间是三天
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 3;

    // 添加角色的key
    private static final String CLAIMS_ROLE = "rol";


    /**
     * 签发JWT
     */
    public static String getToken(String username,String roles) {
        HashMap<String, Object> claims = new HashMap<>();
        //主体---这步不是很懂在干啥
        claims.put(CLAIMS_ROLE, roles);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)// 加密
                .setClaims(claims)// 这里要早set一点，放到后面会覆盖别的字段
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .compact();
    }

    /**
     * 验证JWT
     */
    public static Boolean validateToken(String token) {
        return (!isExpiration(token));
    }

    /**
     * token是否过期
     */
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 根据token获取username
     */
    public static String getUsername(String token){
        return getTokenBody(token).getSubject();
    }


    /**
     * 根据token获取权限
     */
    public static Set<GrantedAuthority> getRolseByToken(String token) {
        String rolse = (String) getTokenBody(token).get(CLAIMS_ROLE);
        String[] strArray = StringUtils.strip(rolse, "[]").split(",");
        Set<GrantedAuthority> authoritiesSet = new HashSet();
        if (strArray.length>0){
            Arrays.stream(strArray).forEach(rols-> {
                GrantedAuthority authority = new SimpleGrantedAuthority(rols);
                authoritiesSet.add(authority);
            });
        }
        System.out.println("权限池"+authoritiesSet);
        return authoritiesSet;
    }


    /**
     * 解析JWT 并返回
     */
    private static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


}
