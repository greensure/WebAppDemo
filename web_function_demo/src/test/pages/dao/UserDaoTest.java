package pages.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
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
// 使用标准的JUnit @RunWith注释来告诉JUnit使用Spring TestRunner
@RunWith(SpringJUnit4ClassRunner.class)
// 告知junit spring配置文件 -- 指定bean注入的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class UserDaoTest {
    @Resource
    UserDao userDao;
    @Test
    public void selectUserById() {
        userDao.selectUserById(1);
    }
}