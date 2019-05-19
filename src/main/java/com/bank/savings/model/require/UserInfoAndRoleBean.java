package com.bank.savings.model.require;

import com.bank.savings.model.UserInfo;
import lombok.Data;

/**
 * @author taylor
 * @ClassName: UserInfoAndRoleBean
 * @Description:
 * @date: 2019-05-19 19:11
 */
@Data
public class UserInfoAndRoleBean {
    private UserInfo userInfo;
    private int roleId;
    private String roleStyle;
}
