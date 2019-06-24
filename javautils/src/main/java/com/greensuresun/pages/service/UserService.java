package com.greensuresun.pages.service;

import com.greensuresun.pages.pojo.User;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/6/4
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@Service("userService")
public interface UserService {
    User selectUserById(Integer userId);
}
