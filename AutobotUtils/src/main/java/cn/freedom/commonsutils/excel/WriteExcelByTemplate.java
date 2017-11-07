package cn.freedom.commonsutils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;


public class WriteExcelByTemplate {

	/**
	 * 
	 * 
	* @Title: createExcel 
	* @Description: 根据模版写execl
	* @param @param 模版路径
	* @param @param 保存文件路径
	* @param @param 模版头信息占用的行数
	* @param @param  写入的数据
	* @return void    返回类型描述
	* @throws
	 */

	public static void createExcel(String templateExcelPath, String savePath, int headindex, List<List<String>> writeWorks) {
		OutputStream out = null;
		InputStream is = null;
		try {
			is = new FileInputStream(templateExcelPath);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			Row row = null;

			for (int rowIndex = 0; rowIndex < writeWorks.size(); rowIndex++) {
				List<String> writeWork = writeWorks.get(rowIndex);
				row = hssfSheet.createRow(rowIndex + headindex);

				for (int i = 0; i < writeWork.size(); i++) {
					String data = writeWork.get(i);
					Cell cell = row.createCell(i);
					cell.setCellType(CellType.STRING);
					cell.setCellValue(data);
				}
			}

			// 保存为Excel文件

			File target = new File(savePath);
			target.getParentFile().mkdirs();
			out = new FileOutputStream(target);
			hssfWorkbook.write(out);
		} catch (IOException e) {
			System.out.println(e.toString());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}
		}
	}

	public static void createExcelByArr(String templateExcelPath, String savePath, int headindex, List<String[]> writeWorks) {
		List<List<String>> data = new ArrayList<List<String>>(writeWorks.size());
		for (String[] list : writeWorks) {
			List<String> line = new ArrayList<String>(list.length);

			for (int i = 0; i < list.length; i++) {
				line.add(list[i]);
			}
			data.add(line);
		}
		createExcel(templateExcelPath, savePath, headindex, data);
	}
}
