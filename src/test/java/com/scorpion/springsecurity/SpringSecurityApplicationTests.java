package com.scorpion.springsecurity;

import com.scorpion.springsecurity.entity.User;
import com.scorpion.springsecurity.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SpringSecurityApplicationTests {
    @Resource
    private  UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testBCryptPasswordEncoder(){
        String encode = passwordEncoder.encode("1234");
        String encode1 = passwordEncoder.encode("1234");
        System.out.println(encode);
        System.out.println(encode1);
        boolean b = passwordEncoder.matches("12345", "$2a$10$hDa143mrDUgnU7QXzgYqVe/gUWizQso5xRY9Qm5XcK7bHvI9fB/lW");
        System.out.println(b);
    }

}
