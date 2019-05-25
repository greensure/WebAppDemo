package com.greensuresun.dao;

import com.greensuresun.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/24
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public interface ExcelDao {
    /**
     * Description: 
     * 向数据库中写入一条数据
     * @param: seckillId
     * @param userPhone
     * @return: int
     * @auther: sunyuanyuan
     * @date: 2019/5/24 
     * @classname: ExcelDao
     */
    public int writeDataFromExcel(@Param("seckillId") final long seckillId, @Param("userPhone") final long userPhone);

    public int writeDatasFromExcel(List<SuccessKilled> contentList );
}
