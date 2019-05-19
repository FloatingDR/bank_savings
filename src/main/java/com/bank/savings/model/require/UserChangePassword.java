package com.bank.savings.model.require;

import lombok.Data;

/**
 * @author taylor
 * @ClassName: UserChangePassword
 * @Description:
 * @date: 2019-05-19 17:47
 */
@Data
public class UserChangePassword {
    private int userId;
    private String loginPassword;
    private String payPassword;
    private String oldLoginPassword;
    private String oldPayPassword;
}
