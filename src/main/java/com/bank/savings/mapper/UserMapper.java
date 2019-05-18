package com.bank.savings.mapper;

import com.bank.savings.model.User;

public interface UserMapper {

    /**
     * get info by username
     * @param username
     * @return
     */
    User getInfoByUsername(String username);

    /**
     * 添加用户
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * select info by userId
     * @param userId
     * @return
     */
    User selectByPrimaryKey(Integer userId);

    /**
     * 通过用户名得到权限角色
     * @param username
     * @return
     */
    String getRoleStyle(String username);

    int deleteByPrimaryKey(Integer userId);

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}