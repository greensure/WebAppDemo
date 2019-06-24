package part1.jxl;

import java.io.File;
import java.io.IOException;

import jxl.Cell;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * JXL解析Excel
 * @author green
 *
 */
public class JxlReadExcel {

	public static void main(String[] args) {
		try {
			//创建workbook并指定路径
			Workbook workbook = Workbook.getWorkbook(new File("d:/jxl_test.xls"));
			//获取第一个工作表sheet
			Sheet sheet = workbook.getSheet(0);
			
			//获取数据
			for (int i = 0; i < sheet.getRows(); i++) {
				for (int j = 0; j < sheet.getColumns(); j++) {
					Cell cell = sheet.getCell(j, i);
					System.out.print(cell.getContents() + " ");
				}
				
				//循环完一行之后换行
				System.out.println();
			}
			
			// 关闭工作簿
			workbook.close();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
