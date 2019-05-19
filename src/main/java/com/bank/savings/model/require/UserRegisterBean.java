package com.bank.savings.model.require;

import com.bank.savings.model.User;
import com.bank.savings.model.UserInfo;
import lombok.Data;

/**
 * @author taylor
 * @ClassName: UserRegisterBean
 * @Description:
 * @date: 2019-05-19 15:04
 */
@Data
public class UserRegisterBean {
    private User user;
    private UserInfo userInfo;
}
