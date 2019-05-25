package dao.cache;

import dao.SeckillDao;
import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/18
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告知junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id = 1001;

    @Autowired
    private RedisDao redisDao;

    /**
     * 由于现在的缓存是空的，先拿到秒杀对象放到缓存中
     */
    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void getSeckill() {
    }

    @Test
    public void getOrPutSeckill() {
    }

    @Test
    public void putSeckill() {
    }

    @Test
    public void putSeckill1() {
    }

    @Test
    public void testSeckil1() throws Exception{
        //分别测试 get and put方法
        // 拿到缓存对象
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null){
            seckill = seckillDao.queryById(id);
            if (seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);

            }
        }
    }
}