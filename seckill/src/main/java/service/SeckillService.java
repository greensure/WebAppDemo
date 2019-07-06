package service;

import dto.Exposer;
import dto.SeckillExecution;
import entity.Seckill;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SeckillService  <br>
 * @Description
 * 业务接口：站在 使用者 角度设计接口;
 * 三个方面：方法定义的粒度、参数、返回类型（return类型/异常）;
 **/
public interface SeckillService {
    /**
     * 查询所有的秒杀记录——>用于列表页，用来展示所有的秒杀产品列表
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

//    重要行为接口 Start
    /**
     * 秒杀开启时输出秒杀接口地址，
     * 否则输出秒杀开始时间、系统时间
     * 目的：用于输出秒杀接口地址，当秒杀没有开启的时候，谁也猜不到秒杀的地址，应该达到这样的效果，
     * 而不是用户通过URL规则来拼接出URL地址；
     * 可以提前去交由浏览器插件去执行秒杀
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * 该接口需要出异常，原因：告诉Spring声明式事务执行提交还是回滚
     * TODO md5此处作用？
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     *  通过存储过程，执行秒杀操作
     *  java客户端调用存储过程的逻辑
     *  注意：该接口不需要抛出异常，原因：无需告诉Spring声明式事务执行提交还是回滚
     *  TODO md5此处作用？
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}

