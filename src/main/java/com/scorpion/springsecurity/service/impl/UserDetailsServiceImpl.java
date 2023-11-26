package com.scorpion.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scorpion.springsecurity.entity.LoginUser;
import com.scorpion.springsecurity.entity.User;
import com.scorpion.springsecurity.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author scorpion
 * @date 2023/10/20
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //  1、根据用户名查询用户信息
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(userLambdaQueryWrapper);
        //  2、判断用户是否存在
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名不存在!");
        }
        //  3、根据用户信息查询权限信息并返回
        Set<String> permissionSet=new HashSet<>();
        permissionSet = userMapper.selectAllPermissions(user.getId());
        return new LoginUser(user,null,permissionSet);
    }
}
