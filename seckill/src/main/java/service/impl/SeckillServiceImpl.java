package service.impl;

import dao.SeckillDao;
import dao.SuccessKilledDao;
import dao.cache.RedisDao;
import dto.Exposer;
import dto.SeckillExecution;
import entity.Seckill;
import entity.SuccessKilled;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import service.SeckillService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enum.SeckillStatEnum;

/**
 * @ClassName SeckillServiceImpl  <br>
 * @Description TODO
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 注入依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    // md5盐值字符串，用于混淆md5
    // 在md5基础上加入混淆概念，为啥加入混淆呢？不希望用户猜到结果，混淆的盐值越复杂越好
    private final String slat = "fidie@#$((!!feoo3fi2335-i***43r9*/-++";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        /*
        秒杀的优化点：
        Seckill seckill = seckillDao.queryById(seckillId);该步骤的数据库操作，
        虽然该步骤的访问是根据秒杀的主键去访问，应该会很快。
        由于所有秒杀单都要去调用暴露的接口这个方法，我们需要用Redis把他缓存起来，这样可以降低数据库的访问压力；
        因为数据库之后还有很大的用处：用来作为执行秒杀的最重要的使用场景；

        该部分优化代码应该放在dao，dao专门用于存放数据库或其他存储访问的类所在的包：dao.cache.RedisDao
        */
//        Seckill seckill = seckillDao.queryById(seckillId);
        /**
         * 常见的缓存优化方法：伪代码
         * get from cache
         * if null
         *   get db
         *   else
         *      put cache
         * login
         * 注意事项：
         * 上述伪代码经常会被写道业务逻辑里，最好写道DAO层
         */

        //优化点：缓存优化,一致性：建立在超时基础上维护一致性
        //1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null){
            //2.访问数据库<-缓存没有就访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null){
                //数据库也没有就返回false，表示秒杀单不存在
                return new Exposer(false, seckillId);
            }else {
                //3.如果存在就放入Redis
                redisDao.putSeckill(seckill);
            }
        }

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        // TODO 意义 end?

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();

        // 秒杀还未开始 或 秒杀已经结束
        if (nowTime.getTime() < startTime.getTime() ||
                nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        // 转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        // 若秒杀开启，则返回秒杀开启的操作；
        return new Exposer(true, md5, seckillId);
    }

    /**
     * @MethodName MD5加密
     * @Description 不希望外部访问到的方法《——安全性
     * md5的目的是加密，为什么要写一个独立的md5方法呢？
     * （1）因为执行秒杀还是需要md5 的验证过程需要根据getMD5(）的规则和用户传来的md5做比较，若不一样就被驳回，说明数据被篡改了；
     * （2）因为有重用的点，多次/处调用，就把他抽象为一个方法来用；
     *
     **/
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Transactional
    /**
     * @MethodName
     * @Description 改了md5，匹配不上（系统异常）
     * 使用注解控制方法的优点：
     * 1.开发团队约定明确标注事务方法的风格；
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他的网络操作:RPC/HTTP请求、或移动到事务方法外部；
     * 3.不是所有的方法都需要事务，如果只有一条修改操作，只读操作不需要事务控制。
     **/
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws
            SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }

        // 执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();

        //除了try catch已知的异常外，也可能有其他异常，例如插入记录可能会超时；
        try {
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 唯一seckillId,userPhone
            // 根据id,userphone进行唯一验证，若之前已经秒杀成功了，再去执行insertSuccesskilled（）时，其实是插入不了的;
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumner(seckillId, nowTime);

                // updateCount<=0对于DAO层来说是没有更新DAO操作，对业务而言是秒杀结束了
                if (updateCount <= 0) {
                    // 没有更新到记录，秒杀结束
                    throw new SeckillCloseException("sekill is closed");
                } else {
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译器异常转化为运行期异常
            throw new SeckillException("Seckill inner error: " + e.getMessage());
        }

    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            // 秒杀数据被重写了
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);

        /**
         * killByProcedure(map) 接口参数为什么定义为map？
         * 原因分析：
         * result需要把result也放入参数里，告知MySQL,
         * 当存储过程执行完了之后，result被赋值
         */

        try{
            seckillDao.killByProcedure(map);
            // 获取Result  <---通过，需引入pom依赖:commons-collections
            // 建议平时的项目中引入guaua,commons-collections
            int result = MapUtils.getInteger(map,"result", -2);
            if (result == 1){
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                //返回秒杀成功接口，将秒杀成功对象也传到接口中
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
            }else{
                //根据result拿秒杀的状态--字符串类型
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }

        } catch (Exception e){
            logger.error(e.getMessage(), e);
            // 若出现异常，则接口抛出异常
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

}
