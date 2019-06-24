package com.greensuresun.utils;

import com.csvreader.CsvReader;
import com.greensuresun.entity.ConstantPO;
import org.apache.commons.csv.CSVFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/28
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public class CSVUtil {
    private static Logger log = LoggerFactory.getLogger(CSVUtil.class);



   /* public static void readCSV(){

        try {
            // CSV数据集合
            ArrayList<ConstantPO> csvConstantDataList = new ArrayList<ConstantPO>();
            ConstantPO constantPO = new ConstantPO();

            // CSV文件路径
            String csvFilePath = "d://test.csv";
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader csvReader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            try {
                csvReader.readHeaders();
                // 逐行读入除表头的数据
                while (csvReader.readRecord()){
                    log.info("Record: " + csvReader.getRawRecord() );
                    csvConstantDataList.add(csvReader.getValues());
                }
                //关闭文件流
                csvReader.close();
                // 遍历读取的CSV文件
                for (int row = 0; row < csvConstantDataList.size(); row++) {
                    // 取得第row行第0列的数据
                    String cell = csvConstantDataList.get(row)[0];
                    System.out.println("------------>"+cell);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/

}
