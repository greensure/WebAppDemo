package dao;

import entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SeckillDao  <br>
 * @Description 秒杀对象接口
 **/
@Repository
public interface SeckillDao {

    /**
     * @MethodName
     * @Description 减库存，返回影响的行数>1，表示更新的记录行数
     **/
    int reduceNumner(@Param("seckillId") final Long seckillId, @Param("killTime") final Date killTime);

    /**
     * @MethodName
     * @Description 根据id查询秒杀对象
     **/
    Seckill queryById(final long seckillId);

    /**
     * @MethodName
     * @Description 根据偏移量查询秒杀商品列表
     **/
    List<Seckill> queryAll(@Param("offset") final int offset, @Param("limit") final int limit);

    /**
     * @MethodName
     * @Description 使用存储过程执行秒杀
     * 该接口无需返回值，void即可
     *
     * 为什么该接口参数传入Map?
     * 原因分析如下：
     * result需要把result也放入参数里，告知MySQL,
     * 当存储过程执行完了之后，result被赋值
     *
     **/
    void killByProcedure(Map<String, Object> paramMap);
}
