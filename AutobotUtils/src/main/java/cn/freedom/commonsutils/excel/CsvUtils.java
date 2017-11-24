package cn.freedom.commonsutils.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.freedom.commonsutils.StringUtils;

public class CsvUtils {
	public static class Cursor {

		private ArrayList<String[]> data;
		private int cursor = -1;
		private Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();

		public boolean moveToFirst() {
			cursor = -1;
			return true;
		}

		public boolean hasNext() {
			return (cursor < data.size() - 1);
		}

		public boolean next() {
			cursor++;
			if (cursor <= data.size() - 1) return true;
			return false;
		}

		public String getString(int index) {
			if (index < data.get(cursor).length) {
				return data.get(cursor)[index];
			} else {
				return null;
			}
		}

		public int getIndex(String title) {
			if (headerIndexMap.containsKey(title.toUpperCase())) {
				return headerIndexMap.get(title);
			} else {
				return -1;
			}
		}

	}

	public static Cursor readCsv(File csvFile) {
		Cursor cursor = new Cursor();
		ArrayList<String[]> result = new ArrayList<String[]>();
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));
			String line = null;
			int index = 0;
			while ((line = read.readLine()) != null) {
				if (index == 0) {
					cursor.headerIndexMap = getHeadIndex(line);
					index++;
				} else {
					String[] data = StringUtils.splitString(line);
					result.add(data);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (Exception e2) {}
		}
		cursor.data = result;

		return cursor;
	}

	public static Map<String, Integer> getHeadIndex(String heads) {

		String[] headStrs = StringUtils.splitString(heads);

		Map<String, Integer> result = new HashMap<String, Integer>();
		for (int i = 0; i < headStrs.length; i++) {
			result.put(headStrs[i].trim().toUpperCase(), i);
		}
		return result;
	}

	public static Cursor readCsv(String path) {
		return readCsv(new File(path));
	}
}
