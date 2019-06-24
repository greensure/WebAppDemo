package com.greensuresun.pages.service;

import com.greensuresun.pages.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/6/4
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告知junit spring配置文件 -- 指定bean注入的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void selectUserById() {
        User user = userService.selectUserById(1);
        System.out.println(user.getUserName() + ":" + user.getUserPassword());
    }
}