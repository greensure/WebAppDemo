package com.greensuresun.excel_utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Description:
 * 操作Excel表格的功能类
 * Ref： https://blog.csdn.net/luckey_zh/article/details/32710363
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/24
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public class ExcelReader {
    private POIFSFileSystem fileSystem;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFRow row;

    public List<Map<Integer, String>> readExcelContent(String url) throws FileNotFoundException{
        List<Map<Integer, String>> contentList = new ArrayList<Map<Integer, String>>();
        InputStream inputStream = new FileInputStream(url);
        String str = "";

        try {
            fileSystem = new POIFSFileSystem(inputStream);
            workbook = new HSSFWorkbook(fileSystem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheetAt(0);
        //得到总行
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 0; i <= rowNum ; i++) {
            Map<Integer, String > content = new HashMap<Integer, String>();
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum){
                str = getStringCellValue(row.getCell((short)j)).trim();
                //Excel每一行放到Map中
                content.put(j, str);
                j++;
            }
            //放到List集合
            contentList.add(content);
        }
        return contentList;
    }

    /**
     * Description: 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return: String 单元格数据内容
     * @auther: sunyuanyuan
     * @date: 2019/5/24
     * @classname: ExcelReader
     */
    private String getStringCellValue(HSSFCell cell){
        String strCell = "";
        switch (cell.getCellTypeEnum()){
            case STRING:
                strCell = cell.getStringCellValue();
                break;
            case NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                strCell = "";
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null){
            return "";
        }
        if (cell == null){
            return null;
        }
        return strCell;
    }

    /**
     * Description:  获取单元格数据内容为日期类型的数据
     *
     * @param: cell Excel单元格
     * @return: String 单元格数据内容
     * @auther: sunyuanyuan
     * @date: 2019/5/24 
     * @classname: ExcelReader
     */
/*    private String getDateCellValue(HSSFCell cell){
        String result = "";
        CellType cellType = cell.getCellTypeEnum();
        if (cellType == CellType.NUMERIC){
            Date date = cell.getDateCellValue();
            result = (date.getDate()+ 1900) + "-" + (date.getMonth() + 1)
                    + "-" + date.getDate();
        }

        return null;
    }*/

}
