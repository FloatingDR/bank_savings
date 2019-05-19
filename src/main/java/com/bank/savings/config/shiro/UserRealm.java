package com.bank.savings.config.shiro;

import com.bank.savings.config.jwt.JWTToken;
import com.bank.savings.config.jwt.JWTUtil;
import com.bank.savings.mapper.UserMapper;
import com.bank.savings.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author taylor
 * @ClassName: UserRealm
 * @Description: 自定义Realm，用于处理用户是否合法
 * @date: 2019-04-24 19:20
 */
@Service
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserMapper userMapper;

    /**
     * 必须重写此方法，不然shiro会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 执行授权逻辑
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String bankCardNumber = JWTUtil.getBankCardNumber(principals.getPrimaryPrincipal().toString());
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //获得该用户角色
        String roleStyle=userMapper.getRoleStyleByBCNumber(bankCardNumber);
        Set<String> roleStyleSet = new HashSet<>();
        roleStyleSet.add(roleStyle);
        //设置该用户拥有的角色和权限
        simpleAuthorizationInfo.setRoles(roleStyleSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 执行认证逻辑
     * @param authToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        String token=(String)authToken.getCredentials();
        //解密获得username,用于和数据库对比
        String bankCardNumber= JWTUtil.getBankCardNumber(token);

        if(bankCardNumber==null){
            throw new AuthenticationException("token invalid");
        }
        User user=userMapper.getUserByBCNumber(bankCardNumber);

        if(user==null){
            throw new UnknownAccountException("user not found");
        }

        if (!JWTUtil.verify(token, bankCardNumber, user.getLoginPassword())) {
            throw new IncorrectCredentialsException("username or password error");
        }
        return new SimpleAuthenticationInfo(token, token,"UserRealm");
    }
}
