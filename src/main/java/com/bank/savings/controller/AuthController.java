package com.bank.savings.controller;

import com.bank.savings.bean.ResponseBean;
import com.bank.savings.model.User;
import com.bank.savings.model.UserInfo;
import com.bank.savings.model.require.UserChangePassword;
import com.bank.savings.model.require.UserRegisterBean;
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

    /**
     * 测试接口 登陆可访问
     * @return "部署成功了哈!"
     */
    @GetMapping("/hello")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "guest"})
    public String hello(){
        return "部署成功了哈!";
    }

    /**
     * 添加用户 所有人都可以访问
     * @param userRegisterBean
     * @return ResponseBean
     */
    @PostMapping("/add")
    public ResponseBean addUser(@RequestBody UserRegisterBean userRegisterBean){
        return userService.addUser(userRegisterBean);
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

    /**
     * 注销银行卡，仅admin用户可访问
     * @param bankCardNumber
     * @return
     */
    @GetMapping("/admin_delete_bank_card/{bankCardNumber}")
    @RequiresRoles("admin")
    public ResponseBean deleteBankCard(@PathVariable String bankCardNumber){
        return userService.deleteBankCardNumber(bankCardNumber);
    }

    @PostMapping("/change_password")
    public ResponseBean changePassword(@RequestBody UserChangePassword user){
        return userService.changePassword(user);
    }

}
