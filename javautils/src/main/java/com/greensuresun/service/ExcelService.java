package com.greensuresun.service;

import com.greensuresun.dao.ExcelDao;
import com.greensuresun.excel_utils.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
public class ExcelService {
    @Autowired
    ExcelDao excelDao;

    public void writeData() throws FileNotFoundException {
        ExcelReader excelReader = new ExcelReader();
        List<Map<Integer,String>> contentList = excelReader.readExcelContent("D:\\book.xls");
        System.out.println("获得Excel表格的内容:"+contentList.size());
        //获得Map的key的个数
        int cloumtCount = contentList.get(0).keySet().size();
        System.out.println(contentList.size());

        List<Map<Integer, String>> datasWrited = new ArrayList<Map<Integer, String>>();
        for (int i = 1; i < contentList.size(); i++) {
            for (int j = 1; j <= cloumtCount; j++){
                //TODO 向数据库中批量写数据
            }
        }

    }
}
