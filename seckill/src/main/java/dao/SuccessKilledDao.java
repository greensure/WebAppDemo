package dao;

import entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName SuccessKilledDao  <br>
 * @Description TODO
 **/
public interface SuccessKilledDao {

    /**
     * @MethodName
     * @Description 插入购买明细，可以过滤重复，返回插入的行数
     **/
    int insertSuccessKilled(@Param("seckillId") final long seckillId, @Param("userPhone") final long userPhone);

    /**
     * @MethodName
     * @Description 根据id查询SuccessKilled 并 携带秒杀产品对象实体
     * 联合唯一主键可以帮助过滤重复；
     **/
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") final long seckillId, @Param("userPhone") final long uerPhone);

}
