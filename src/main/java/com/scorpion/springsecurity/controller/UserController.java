package com.scorpion.springsecurity.controller;

import com.scorpion.springsecurity.dto.UserDto;
import com.scorpion.springsecurity.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author scorpion
 * @date 2023/10/20
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public Object login(@RequestBody UserDto userDto){
        return userService.login(userDto);
    }
    @PostMapping("/logout")
    public Object logout(){
        return userService.logout();
    }
}
