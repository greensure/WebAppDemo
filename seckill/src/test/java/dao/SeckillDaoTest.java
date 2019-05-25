package dao;

import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告知junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    // 注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumner() {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumner(1000L, killTime);
        System.out.println("updateCount = " + updateCount);
    }

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offet' not found. Available parameters are [0, 1, param1, param2]
         *
         * List<Seckill> queryAll(int offet, int limit);
         * java没有保存形参的记录  queryAll(int offet, int limit)—>queryAll(arg0, arg1)
         * 改为List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
         */
        List<Seckill> seckills = seckillDao.queryAll(0, 1000);
        for (Seckill s : seckills) {
            System.out.println(s);
        }
    }

    @Test
    public void killByProcedure() {
    }
}