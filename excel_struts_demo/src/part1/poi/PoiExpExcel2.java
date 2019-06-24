package part1.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 2-5 Excel实战之XSSF创建高版本Excel 
 * .xls版本：93-2007
	高版本Excel：xlsx;
 * 
 * @author green
 *
 */
public class PoiExpExcel2 {
	public static void main(String[] args) {
		// 标题数组
		String[] title = { "id", "name", "sex" };

		// 创建Excel工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();

		// 创建一个工作表sheet
		Sheet sheet = workbook.createSheet();

		// 创建第一行
		Row row = sheet.createRow(0);
		
		Cell cell = null;
		// 插入第一行数据 id,name,sex
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}

		// 追加数据
		for (int i = 1; i <= 10; i++) {
			Row nextrow = sheet.createRow(i);
			// 第一列
			Cell cell2 = nextrow.createCell(0);
			cell2.setCellValue("a" + i);
			// 第二列
			cell2 = nextrow.createCell(1);
			cell2.setCellValue("user" + i);
			// 第三列
			cell2 = nextrow.createCell(2);
			cell2.setCellValue("男");
		}
		
		// 创建一个文件
		File file = new File("D:/poi_test2.xlsx");

		try {
			file.createNewFile();
			//将Excel内容存盘
			FileOutputStream fileOutputStream = FileUtils.openOutputStream(file);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
