package com.bank.savings.service.impl;

import com.bank.savings.bean.ResponseBean;
import com.bank.savings.bean.ResultCode;
import com.bank.savings.config.jwt.JWTUtil;
import com.bank.savings.mapper.UserInfoMapper;
import com.bank.savings.mapper.UserMapper;
import com.bank.savings.model.User;
import com.bank.savings.model.UserInfo;
import com.bank.savings.model.require.UserChangePassword;
import com.bank.savings.model.require.UserInfoAndRoleBean;
import com.bank.savings.model.require.UserRegisterBean;
import com.bank.savings.service.UserService;
import com.bank.savings.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    UserInfoMapper userInfoMapper;


    /**
     * 添加用户，默认添加普通用户
     *
     * @param userRegisterBean
     * @return 403，200
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean addUser(UserRegisterBean userRegisterBean) {
        User user = userRegisterBean.getUser();
        UserInfo userInfo = userRegisterBean.getUserInfo();

        //如果输入的信息不全
        if(user==null || userInfo==null){
            return new ResponseBean(ResultCode.FORBIDDEN, "your info is incomplete", "您的信息不全");
        }

        //判断该银行卡是否已存在
        User user1 = userMapper.getUserByBCNumber(user.getBankCardNumber());
        if (user1 != null) {
            return new ResponseBean(ResultCode.FORBIDDEN, "this band card is already", "卡号重复");
        }

        /**
         * 判断登陆密码和支付密码是否为空，为空返回403
         */
        String loginPassword=user.getLoginPassword();
        String payPassword=user.getPayPassword();
        if(StringUtils.isBlank(loginPassword)||StringUtils.isBlank(payPassword)){
            return new ResponseBean(ResultCode.FORBIDDEN, "your loginPassword or payPassword is empty", "您的登陆密码或支付密码为空，请重试");
        }
        user.setLoginPassword(MD5Util.md5(loginPassword));
        user.setPayPassword(MD5Util.md5(payPassword));

        //默认添加客户角色
        user.setRoleId(2);
        if (userMapper.insert(user) > 0) {
            int userId=userMapper.getUserByBCNumber(user.getBankCardNumber()).getUserId();
            userInfo.setUserId(userId);
            if (userInfoMapper.insert(userInfo) > 0) {
                return new ResponseBean(ResultCode.SUCCESS, "add success", "添加成功");
            }
        }

        //添加成功
        return new ResponseBean(ResultCode.FAIL, "add fail", "添加失败");
    }

    /**
     * 用户登陆
     *
     * @param user
     * @return 404，403，200
     */
    @Override
    public ResponseBean login(User user) {
        //判断该账号是否存在？
        User currentUser = userMapper.getUserByBCNumber(user.getBankCardNumber());
        //不存在
        if (currentUser == null) {
            return new ResponseBean(ResultCode.NOTFOUND, "no this auth", "没有该用户");
        }
        //存在
        String passwordEncoded = currentUser.getLoginPassword();
        if (MD5Util.verify(user.getLoginPassword(), passwordEncoded)) {
            String token = JWTUtil.sign(user.getBankCardNumber(), passwordEncoded);

            UserInfoAndRoleBean userInfoAndRoleBean=new UserInfoAndRoleBean();
            userInfoAndRoleBean.setUserInfo(userInfoMapper.getInfoByUserId(currentUser.getUserId()));
            userInfoAndRoleBean.setRoleId(currentUser.getRoleId());
            userInfoAndRoleBean.setRoleStyle(userMapper.getRoleStyleByBCNumber(user.getBankCardNumber()));

            return new ResponseBean(ResultCode.SUCCESS, token,userInfoAndRoleBean);
        }
        return new ResponseBean(ResultCode.FORBIDDEN, "password error", "密码错误");
    }

    /**
     * 注销银行卡
     * @param bankCardNumber
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean deleteBankCardNumber(String bankCardNumber) {
        User readyDeleteUser=userMapper.getUserByBCNumber(bankCardNumber);
        if(readyDeleteUser==null){
            return new ResponseBean(ResultCode.NOTFOUND, "no this auth", "没有该用户");
        }
        //先删除外键约束再删除主键
        if(userInfoMapper.deleteByUserId(readyDeleteUser.getUserId())>0
                && userMapper.deleteByUserId(readyDeleteUser.getUserId())>0){
            return new ResponseBean(ResultCode.SUCCESS, "delete success", "注销成功");
        }
        return new ResponseBean(ResultCode.FAIL, "delete fail", "注销失败");
    }

    /**
     * 更改密码
     * @param userChangePassword
     * @return 500，403，200
     */
    @Override
    public ResponseBean changePassword(UserChangePassword userChangePassword) {
        User currentUser=new User();
        currentUser.setUserId(userChangePassword.getUserId());
        User DBUser=userMapper.selectByUserId(userChangePassword.getUserId());
        if(DBUser==null){
            return new ResponseBean(ResultCode.NOTFOUND, "no this auth", "没有该用户");
        }

        //修改登陆密码 else if 修改支付密码 else return 403
        if(!StringUtils.isBlank(userChangePassword.getLoginPassword())&&
                !StringUtils.isBlank(userChangePassword.getOldLoginPassword())){
            if(userChangePassword.getOldLoginPassword().equals(userChangePassword.getLoginPassword())){
                return new ResponseBean(ResultCode.FORBIDDEN, "password already", "密码未改变");
            }
            String oldLoginPassword=MD5Util.md5(userChangePassword.getOldLoginPassword());
            if(!oldLoginPassword.equals(DBUser.getLoginPassword())){
                return new ResponseBean(ResultCode.FORBIDDEN, "password error", "原密码错误，请核对后输入");
            }
            currentUser.setLoginPassword(MD5Util.md5(userChangePassword.getLoginPassword()));

        } else if (!StringUtils.isBlank(userChangePassword.getPayPassword())&&
                !StringUtils.isBlank(userChangePassword.getOldPayPassword())){
            if(userChangePassword.getOldPayPassword().equals(userChangePassword.getPayPassword())){
                return new ResponseBean(ResultCode.FORBIDDEN, "password already", "密码未改变");
            }
            String oldPayPassword=MD5Util.md5(userChangePassword.getOldPayPassword());
            if(!oldPayPassword.equals(DBUser.getPayPassword())){
                return new ResponseBean(ResultCode.FORBIDDEN, "password error", "原密码错误，请核对后输入");
            }
            currentUser.setPayPassword(MD5Util.md5(userChangePassword.getPayPassword()));
        }else{
            return new ResponseBean(ResultCode.FORBIDDEN, "empty", "提交的数据为空");
        }

        //是否修改成功
        if(userMapper.updateByPrimaryKeySelective(currentUser)>0){
            return new ResponseBean(ResultCode.SUCCESS, "change success", "修改密码成功");
        }
        return new ResponseBean(ResultCode.FAIL, "change fail", "修改密码失败");
    }


}
