package com.greensuresun.excel_utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/21
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public class ExcelUtils {
    /**
     * Description:
     *
     * @param: response
     * @param fileName
     * @param headMap 表头map
     * @param dataList 表格数据
     * @return:
     * @auther: sunyuanyuan
     * @date: 2019/5/21
     * @classname: ExcelUtils
     */
    public static void exportXlsx(HttpServletResponse response, String fileName, Map<String, String> headMap, List<Map<String, Object>> dataList){
        Workbook workbook = exportXlsx(fileName, headMap, dataList);
    }

    public static Workbook exportXlsx(String sheetName, Map<String, String> headMap, List<Map<String, Object>> dataList){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0, columnIndex = 0;
        Set<String> keys = headMap.keySet();

        //table head
        Row row = sheet.createRow(rowIndex++);
        for (String key : keys){
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(headMap.get(key));
        }

        //内容
        if (dataList != null && !dataList.isEmpty()){
            for (Map<String, Object> map : dataList){
                row = sheet.createRow(rowIndex++);
                columnIndex = 0;
                for (String key : keys){
                    Cell cell = row.createCell(columnIndex++);
//                    setC
                }

            }
        }
        return null;
    }

    private static void setCellValue(Cell cell, Object object){
        if (object == null){
            return;
        }

        if (object instanceof String){
            cell.setCellValue((String) object);
        }else if (object instanceof Date){
            Date date = (Date) object;
            if (date != null){
//                cell.setCellValue(DateUtils.df);
            }

        }
    }
}
