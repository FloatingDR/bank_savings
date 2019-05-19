package com.bank.savings.service;

import com.bank.savings.bean.ResponseBean;
import com.bank.savings.model.User;
import com.bank.savings.model.require.UserChangePassword;
import com.bank.savings.model.require.UserRegisterBean;

/**
 * @author taylor
 * @ClassName: UserService
 * @Description:
 * @date: 2019-05-18 21:58
 */
public interface UserService {

    /**
     * 添加用户
     * @param userRegisterBean
     * @return
     */
    ResponseBean addUser(UserRegisterBean userRegisterBean);

    /**
     * 用户登陆
     * @param user
     * @return
     */
    ResponseBean login(User user);

    /**
     * 注销银行卡
     * @param bankCardNumber
     * @return
     */
    ResponseBean deleteBankCardNumber(String bankCardNumber);

    /**
     * 更改密码
     * @param userChangePassword
     * @return
     */
    ResponseBean changePassword(UserChangePassword userChangePassword);
}
