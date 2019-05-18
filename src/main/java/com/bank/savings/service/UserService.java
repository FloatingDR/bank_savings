package com.bank.savings.service;

import com.bank.savings.bean.ResponseBean;
import com.bank.savings.model.User;

/**
 * @author taylor
 * @ClassName: UserService
 * @Description:
 * @date: 2019-05-18 21:58
 */
public interface UserService {

    /**
     * 添加用户
     * @param user
     * @return
     */
    ResponseBean addUser(User user);

    /**
     * 用户登陆
     * @param user
     * @return
     */
    ResponseBean login(User user);
}
