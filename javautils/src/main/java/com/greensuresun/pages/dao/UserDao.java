package com.greensuresun.pages.dao;

import com.greensuresun.pages.pojo.User;

/**
 * @Description: 分页功能用到
 * @Author: sunyuanyuan
 * @CreateDate: 2019/6/4
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public interface UserDao {


    /**
     * @param userId
     * @return User
     */
    public User selectUserById(Integer userId);
}
