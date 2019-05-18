package com.bank.savings.service.impl;

import com.bank.savings.bean.ResponseBean;
import com.bank.savings.bean.ResultCode;
import com.bank.savings.config.jwt.JWTUtil;
import com.bank.savings.mapper.UserMapper;
import com.bank.savings.model.User;
import com.bank.savings.service.UserService;
import com.bank.savings.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author taylor
 * @ClassName: UserServiceImpl
 * @Description:
 * @date: 2019-05-18 21:58
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;


    /**
     * 添加用户，默认添加普通用户
     * @param user
     * @return ResponseBean 403：用户名重复
     */
    @Override
    public ResponseBean addUser(User user) {
        User currentUser=userMapper.getInfoByUsername(user.getUsername());
        if (currentUser!=null) {
            return new ResponseBean(402, "this account is repeated", null);
        }
        if (user.getRoleId() == null) {
            user.setRoleId(2);
        }
        user.setPassword(MD5Util.md5(user.getPassword()));
        int msg = userMapper.insert(user);
        if (msg > 0) {
            return new ResponseBean(ResultCode.SUCCESS, "insert succeed", null);
        }
        return new ResponseBean(ResultCode.FAIL, "insert fail", null);
    }

    /**
     * 用户登陆
     * @param user
     * @return
     */
    @Override
    public ResponseBean login(User user) {
        User currentUser=userMapper.getInfoByUsername(user.getUsername());
        if(currentUser==null){
            return new ResponseBean(ResultCode.NOTFOUND,"no this auth",null);
        }
        String passwordEncoded = currentUser.getPassword();
        if(MD5Util.verify(user.getPassword(),passwordEncoded)){
            String token= JWTUtil.sign(user.getUsername(),passwordEncoded);
            return new ResponseBean(ResultCode.SUCCESS,token,null);
        }
        return new ResponseBean(ResultCode.FORBIDDEN,"password error",null);
    }
}
