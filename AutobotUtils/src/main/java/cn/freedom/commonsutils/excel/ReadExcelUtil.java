package cn.freedom.commonsutils.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.freedom.commonsutils.StringUtils;

public class ReadExcelUtil {

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param excel
	 *            excel文件
	 * @param numSheet
	 *            sheet 索引
	 * @param maxCellIndex
	 *            最大读取列数
	 * @return List<String[]> 每行一个保存在一个string数组中
	 * @throws IOException
	 */
	public static List<String[]> readXls(File excel, int numSheet, int maxCellIndex) {
		List<String[]> result = new ArrayList<String[]>();
		try {
			InputStream is = new FileInputStream(excel);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet != null) {
				// Read the Row
				for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					String[] row = new String[maxCellIndex];
					if (hssfRow != null) {
						try {
							int lastCellIndex = hssfRow.getLastCellNum();
							for (int i = 0; i < maxCellIndex; i++) {
								if (i > lastCellIndex) {
									row[i] = "";
									continue;
								}
								HSSFCell cell = hssfRow.getCell(i);
								row[i] = getValue(cell);
							}
							//System.out.println("读到工作 ---- " + StringUtils.join(row));
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					} else {
						for (int i = 0; i < maxCellIndex; i++) {
							row[i] = "";
						}
					}
					result.add(row);
				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String getValue(HSSFCell hssfCell) {
		if(hssfCell==null){
			return "";
		}
		try {
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				String val = "" + hssfCell.getNumericCellValue();
				BigDecimal bg = new BigDecimal(val);
				return bg.toPlainString();
			}else if(hssfCell.getCellType() == hssfCell.CELL_TYPE_BLANK){
				return "";
			} else if(hssfCell.getCellType() == hssfCell.CELL_TYPE_ERROR) {
				return "";
			}else{
				
				return String.valueOf(hssfCell.getStringCellValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(hssfCell.getStringCellValue());
	}

	
	
	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param csvFile
	 *            scv文件
	 * @param numSheet
	 *            sheet 索引
	 * @param maxCellIndex
	 *            最大读取列数
	 * @return List<String[]> 每行一个保存在一个string数组中
	 * @throws IOException
	 */
	public static List<String[]> readCsv(File csvFile) {
		List<String[]> result = new ArrayList<String[]>();
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile) , "UTF-8"));
			String line = null;
			while ((line = read.readLine()) != null) {
				String[] data = StringUtils.splitString(line);
				result.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (Exception e2) {}
		}
		return result;
	}

	public static List<String[]> readCsv(String path) {
		return readCsv(new File(path));
	}
}
