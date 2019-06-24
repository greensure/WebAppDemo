package com.greensuresun.pages.service;

import com.greensuresun.pages.dao.UserDao;
import com.greensuresun.pages.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/6/4
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User selectUserById(Integer userId) {
        return userDao.selectUserById(userId);
    }
}
