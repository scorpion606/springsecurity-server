package com.scorpion.springsecurity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scorpion.springsecurity.entity.User;

import java.util.Set;

/**
 * @author scorpion
 * @date 2023/10/19
 */
public interface UserMapper extends BaseMapper<User> {
    Set<String> selectAllPermissions(Long userId);
}
