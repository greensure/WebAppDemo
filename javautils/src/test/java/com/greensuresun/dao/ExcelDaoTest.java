package com.greensuresun.dao;

import com.greensuresun.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/24
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告知junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ExcelDaoTest {
    @Resource
    private ExcelDao excelDao;

    /**
     * @MethodName insertSuccessKilled
     * @Description
     * 第一次：insertCount=1
     * 第二次：insertCount=2secKillId
     * 不允许重复秒杀插入数据
     *
     * @Param []
     * @return void
     **/
    @Test
    public void writeDataToExcel() {

        long id = 1005L;
        long phone = 12345678900L;
        int insertCount = excelDao.writeDataFromExcel(id, phone);
        System.out.println("insertCount = " + insertCount);
    }
//TODO mybatis批量插入
    @Test
    public void writeDatasToExcel() {
//        SuccessKilled successKilled = new SuccessKilled(1002,123456789);
        SuccessKilled successKilled = new SuccessKilled(1234567890);
//        successKilled = new SuccessKilled(1007,1234567891);
//        successKilled = new SuccessKilled(1008,1234567892);
//        successKilled = new SuccessKilled(1009,1234567893);

        List<SuccessKilled> contentList = new ArrayList<>();

        contentList.add(successKilled);
        for (int i = 0; i < contentList.size(); i++) {
            excelDao.writeDatasFromExcel(contentList);
        }


    }
}