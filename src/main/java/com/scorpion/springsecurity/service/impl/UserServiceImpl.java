package com.scorpion.springsecurity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scorpion.springsecurity.common.RedisKey;
import com.scorpion.springsecurity.common.ResponseResult;
import com.scorpion.springsecurity.config.BeanConfig;
import com.scorpion.springsecurity.dto.UserDto;
import com.scorpion.springsecurity.entity.LoginUser;
import com.scorpion.springsecurity.entity.User;
import com.scorpion.springsecurity.mapper.UserMapper;
import com.scorpion.springsecurity.service.UserService;
import com.scorpion.springsecurity.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.scorpion.springsecurity.common.RedisKey.USER_KEY;

/**
 * @author scorpion
 * @date 2023/10/19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public ResponseResult login(UserDto userDto) {
        //  1、判断是否认证通过
        String userName = userDto.getUsername();
        String password = userDto.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userName,password);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //  2、认证不通过返回登录失败
        if(Objects.isNull(authenticate)){
            return ResponseResult.fail();
        }
        //  3、认证通过将用户信息存入redis，生成token，返回用户信息
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId, 7 * 24 * 60 * 60 * 1000L);
        loginUser.setAccessToken(jwt);
        try {
            String loginUserStr = objectMapper.writeValueAsString(loginUser);
            stringRedisTemplate.opsForValue().set(USER_KEY+userId,loginUserStr,1L, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ResponseResult(200,"成功",loginUser);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String userKey=USER_KEY+userId.toString();
        Boolean delete = stringRedisTemplate.delete(userKey);
        if(delete){
            return new ResponseResult(200,"成功",null);
        }else{
            return ResponseResult.fail();
        }
    }
}
