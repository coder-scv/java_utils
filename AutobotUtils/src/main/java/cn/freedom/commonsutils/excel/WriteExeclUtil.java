package cn.freedom.commonsutils.excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class WriteExeclUtil {

	public static void writeCSV(String path, String[] heads, List<List<String>> writeWorks) {
		try {
			File target = new File(path);
			target.getParentFile().mkdirs();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "utf-8"));
			StringBuffer sb = new StringBuffer();

			if (heads != null) {

				for (String head : heads) {
					sb.append(head);
					sb.append(",");
				}
				sb.setLength(sb.length() - 1);
				bw.write(sb.toString());
				bw.newLine();
			}

			for (List<String> writeWork : writeWorks) {
				sb.setLength(0);
				for (String data : writeWork) {
					sb.append(data);
					sb.append(",");
				}
				sb.setLength(sb.length() - 1);
				bw.write(sb.toString());
				bw.newLine();
			}

			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createExcel(String path, String[] heads, List<List<String>> writeWorks) {
		HSSFWorkbook wb = new HSSFWorkbook();
		// 添加Worksheet（不添加sheet时生成的xls文件打开时会报错）
		Sheet sheet1 = wb.createSheet("first");
		int headindex = 0;

		Row row = null;
		if (heads != null) {
			headindex = 1;
			row = sheet1.createRow(0);

			for (int i = 0; i < heads.length; i++) {
				String head = heads[i];
				Cell cell = row.createCell(i);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(head);
			}
		}

		for (int rowIndex = 0; rowIndex < writeWorks.size(); rowIndex++) {
			List<String> writeWork = writeWorks.get(rowIndex);
			row = sheet1.createRow(rowIndex + headindex);

			for (int i = 0; i < writeWork.size(); i++) {
				String head = writeWork.get(i);
				Cell cell = row.createCell(i);
				cell.setCellType(CellType.STRING);
				cell.setCellValue(head);
			}
		}

		// 保存为Excel文件
		OutputStream out = null;
		try {
			if (!path.endsWith(".xls")) {
				path += ".xls";
			}
			File target = new File(path);
			target.getParentFile().mkdirs();
			out = new FileOutputStream(target);
			wb.write(out);
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
		}
	}

}
