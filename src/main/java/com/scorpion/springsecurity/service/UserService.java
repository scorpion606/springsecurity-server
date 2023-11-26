package com.scorpion.springsecurity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scorpion.springsecurity.common.ResponseResult;
import com.scorpion.springsecurity.dto.UserDto;
import com.scorpion.springsecurity.entity.User;

/**
 * @author scorpion
 * @date 2023/10/19
 */
public interface UserService extends IService<User> {
    ResponseResult login(UserDto userDto);
    ResponseResult logout();
}
