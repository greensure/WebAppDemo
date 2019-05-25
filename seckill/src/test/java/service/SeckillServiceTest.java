package service;

import dto.Exposer;
import dto.SeckillExecution;
import entity.Seckill;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/4/12
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}",exposer);
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000;
        long userPhone = 177232949822L;
        String md5 = "fkjfwejfjfeijefi";
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result={}",execution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
    }

//    exportSeckillUrl + executeSeckill
//将上面两个测试代码集成完整逻辑，注意可以重复执行
@Test
public void testSeckillLogic() throws Exception {
    long seckillId = 1001;
    Exposer exposer = seckillService.exportSeckillUrl(seckillId);

    if (exposer.isExposed()){
        logger.info("exposer={}",exposer);
        long userPhone = 177232949822L;
        String md5 = exposer.getMd5();
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result={}",execution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
    } else {
        //秒杀未开启
        logger.warn("exposer={}",exposer);
    }
}

    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1002;
        long phone = 15694332288L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }
}