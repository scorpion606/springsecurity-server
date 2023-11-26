package com.scorpion.springsecurity.Filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scorpion.springsecurity.common.RedisKey;
import com.scorpion.springsecurity.entity.LoginUser;
import com.scorpion.springsecurity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * @author scorpion
 * @date 2023/10/28
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("access_token");
        //  1，判断请求头是否存在token
        if(!StringUtils.hasText(accessToken)){
            filterChain.doFilter(request,response);
            return;
        }
        //  2，解析token，判断是否合法,从redis获取用户信息
        String userId=null;
        try {
            Claims claims = JwtUtil.parseJWT(accessToken);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //  3，从redis获取用户信息并判断token是否过期
        String userKey=RedisKey.USER_KEY+ userId;
        String loginUserStr = stringRedisTemplate.opsForValue().get(userKey);
        if(!StringUtils.hasText(loginUserStr)){
            throw new RuntimeException("用户登录已过期");

        }
        //  4，将用户信息存入SecurityContextHolder
        LoginUser loginUser = objectMapper.readValue(loginUserStr, LoginUser.class);
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);
    }
}
