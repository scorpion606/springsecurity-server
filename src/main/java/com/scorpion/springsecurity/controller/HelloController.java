package com.scorpion.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author scorpion
 * @date 2023/6/12
 */
@RestController
@CrossOrigin
public class HelloController {
    @RequestMapping("/hello")
    @PreAuthorize("hasAnyAuthority('system/dept/list')")
    public String hello(){
        return "hello";
    }
}
