package com.bank.savings.controller;

import com.bank.savings.bean.ResponseBean;
import com.bank.savings.model.User;
import com.bank.savings.service.UserService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author taylor
 * @ClassName: AuthController
 * @Description:
 * @date: 2019-05-18 20:41
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "guest"})
    public String hello(){
        return "部署成功了哈!";
    }

    /**
     * 添加用户 所有人都可以访问
     * @param user
     * @return ResponseBean
     */
    @PostMapping("/add")
    public ResponseBean addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    /**
     * 用户登陆 所有人都可以访问
     * @param user
     * @return ResponseBean
     */
    @PostMapping("/login")
    public ResponseBean login(@RequestBody User user){
        return userService.login(user);
    }

}