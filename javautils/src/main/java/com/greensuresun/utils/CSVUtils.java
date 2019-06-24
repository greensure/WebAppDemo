package com.greensuresun.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @Description: csv文件读写工具类
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/28
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public class CSVUtils {
    private static final Logger logger = LoggerFactory.getLogger("CsvUtils.class");

    /**
     * @param objectList 对象集合
     * @param fileHeader 头部标题
     * @param fileName   文件名-不带后缀
     * @return file 文件
     */
    public static File writeCsv(List<Object> objectList, String[] fileHeader, String fileName) {
        // 这里显式地配置一下CSV文件的Header，然后设置跳过Header（要不然读的时候会把头也当成一条记录）
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(fileHeader).withRecordSeparator("\n");

        // 这个是定位   判断某个字段的数据应该放在records数组中的那个位子
        Map<String, Integer> map = Maps.newHashMap();

        for (int headerSize = 0; headerSize < fileHeader.length; headerSize++) {
            map.put(fileHeader[headerSize], headerSize);
        }

        File csvFile = new File(fileName);

        try {
            // 获取对象的PropertyDescriptor
            Map<String, PropertyDescriptor> descriptorMap = null;
            //
            BufferedWriter bufferedWriter = null;

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"));

            CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, csvFormat);
            for (Object object : objectList) {
                if (CheckUtils.isEmpty(descriptorMap)) {
                    descriptorMap = CSVUtils.getCsvFieldMapPropertyDescriptor(object.getClass());
                }
                String[] records = new String[fileHeader.length];

                for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()){
                    if (descriptorMap.containsKey(stringIntegerEntry.getKey())){
                        records[map.get(stringIntegerEntry.getKey())] = (String) descriptorMap.get(stringIntegerEntry.getKey()).getReadMethod().invoke(object);
                    }
                }
                csvPrinter.printRecord(Arrays.asList(records));
            }
            bufferedWriter.flush();
            bufferedWriter.close();
            csvPrinter.close();
        } catch (Exception e) {
            logger.error("CsvUtils.writeCsv,写csv文件失败,message:{}", e.getMessage(), e);
            e.printStackTrace();
        }
        return csvFile;
    }

    /**
     * 获取对应对象中包含CsvCsvField字段的 PropertyDescriptor
     *
     * @param tClass
     * @return 对象的class
     * @throws IntrospectionException
     * @throws NoSuchFieldException
     */
    public static Map<String, PropertyDescriptor> getCsvFieldMapPropertyDescriptor(Class tClass) throws IntrospectionException, NoSuchFieldException {
        Map<String, PropertyDescriptor> descriptorMap = Maps.newHashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(tClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            // 获取该字段赋值过来的  字段名称
            if (propertyDescriptor.getWriteMethod() == null) {
                continue;
            }
            Field field = tClass.getDeclaredField(propertyDescriptor.getName());
            CsvField csvField = field.getAnnotation(CsvField.class);

            if (csvField == null) {
                continue;
            }

            String fieldMetaName = csvField.name();

            if (CheckUtils.isEmpty(fieldMetaName)) {
                continue;
            }

            descriptorMap.put(fieldMetaName, propertyDescriptor);
        }
        return descriptorMap;
    }

    public static <T> List<T> readCSV(String filePath, String[] headers, Class<T> tClass){
        // 创建CSVFormat
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headers);
        // 获取对象的PropertyDescriptor
        List<T> tList = new ArrayList<>();
        try {
            Map<String, PropertyDescriptor> descriptorMap = CSVUtils.getCsvFieldMapPropertyDescriptor(tClass);
            FileReader fileReader = new FileReader(filePath);

            //创建CSVParser对象
            CSVParser parser = new CSVParser(fileReader, format);
            Map<String, Integer> map = parser.getHeaderMap();

            for (CSVRecord csvRecord : parser){
                T t = tClass.newInstance();
                for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()){
                    if (descriptorMap.containsKey(stringIntegerEntry.getKey()) && csvRecord.size() > stringIntegerEntry.getValue()){
                        descriptorMap.get(stringIntegerEntry.getKey()).getWriteMethod().invoke(t, csvRecord.get(stringIntegerEntry.getValue()));
                    }
                }
                tList.add(t);
            }
            parser.close();
            fileReader.close();

        } catch (Exception e) {
            logger.error("CsvUtils.readCSV,读取csv文件,message:{}", e.getMessage(), e);
            e.printStackTrace();
        }
        return tList;
    }

    public void readTest(){
        String[] fileHeader = {""};
        long readTimeStart = System.currentTimeMillis();
        List<String> m = CSVUtils.readCSV("d:\\test.csv", fileHeader, String.class);
        for (String s : m) {
            System.out.println(m);
        }
        logger.info("读取时间：" + (System.currentTimeMillis() - readTimeStart));
    }

    public static void main(String[] args) throws Exception {
        String[] fileHeader = {""};
        // 测试写
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("姓名44444888" + i);
        }
        long writeTimeStart = System.currentTimeMillis();
        CSVUtils.writeCsv(list, fileHeader, "d:\\workbook.csv");
        logger.info("写入时间：" + (System.currentTimeMillis() - writeTimeStart));

//        for (MsgResponse msgResponse : m) {
//            logger.info(msgResponse.getCode() + "               " + msgResponse.getMsg());
//        }
    }

}
