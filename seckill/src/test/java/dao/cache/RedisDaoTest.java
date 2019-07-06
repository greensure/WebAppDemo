package dao.cache;

import dao.SeckillDao;
import dto.Exposer;
import dto.SeckillExecution;
import entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.SeckillService;

import javax.annotation.Resource;

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
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class RedisDaoTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private long id = 1001;

    @Resource
    private RedisDao redisDao;

    /**
     * 由于现在的缓存是空的，先拿到秒杀对象放到缓存中
     */
    @Resource
    private SeckillDao seckillDao;
    @Autowired
    private SeckillService seckillService;

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
            //如果seckill对象为空，就从数据库中查取
            seckill = seckillDao.queryById(id);
            if (seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println("放入Redis缓存中的seckill对象：" + result);
                seckill = redisDao.getSeckill(id);
                System.out.println("取出Redis缓存中的seckill对象：" + seckill);

            }
        }
    }

    /**
     * 测试通过存储过程完成秒杀的方法是否OK
     * 首先要获取md5
     */
    @Test
    public void executeSeckillProcedure(){
        long seckillId = 1001;
        long phone = 1234567890;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }


    }


}