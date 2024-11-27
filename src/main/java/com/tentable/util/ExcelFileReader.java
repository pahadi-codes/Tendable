package com.tentable.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class ExcelFileReader {
	Workbook workbook = null;
	@Getter
	Map<Integer, String> sheets = new TreeMap<>();

	public ExcelFileReader(URL fileUrl) {
		try {
			workbook = new XSSFWorkbook(fileUrl.getPath());
			populateSheetNames();
		} catch (Exception exception) {
			exception.fillInStackTrace();
		}
	}

	void populateSheetNames() {
		for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
			sheets.put(index, workbook.getSheetName(index));
		}
	}

	public boolean doesSheetExist(String sheetName) {
		return sheets.containsValue(sheetName);
	}

	public int getRowCount(String sheetName) {
		if (doesSheetExist(sheetName)) {
			return workbook.getSheet(sheetName).getLastRowNum() + 1;
		} else {
			return 0;
		}
	}

	public int getColCount(String sheetName) {
		if (doesSheetExist(sheetName)) {
			Row row = workbook.getSheet(sheetName).getRow(0);
			if (row != null) {
				return row.getLastCellNum();
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	public String getCellValue(String sheetName, int rowNum, int colIndex) {
		try {
			Cell cell = workbook.getSheet(sheetName).getRow(rowNum - 1).getCell(colIndex);
			if (cell == null) {
				return "";
			}
			switch (cell.getCellType()) {
				case STRING:
					return cell.getStringCellValue();
				case NUMERIC:
					return String.valueOf(cell.getNumericCellValue());
				case FORMULA:
					return "FORMULA: " + cell.getCellFormula();
				case BOOLEAN:
					return String.valueOf(cell.getBooleanCellValue());
				default:
					return "";
			}
		} catch (Exception exception) {
			log.error(exception.getMessage(), exception);
			return "Column [" + colIndex + "] & Row [" + rowNum + "] not found";
		}
	}

	public String getCellValue(String sheetName, int rowNum, String colName) {
		int colIndex = -1;
		Map<Integer, String> cells = getCellsFromRow(sheetName, 1);
		for (int activeColIndex : cells.keySet()) {
			if (cells.get(activeColIndex).equals(colName)) {
				colIndex = activeColIndex;
			}
		}
		return getCellValue(sheetName, rowNum, colIndex);
	}

	public Map<Integer, String> getCellsFromRow(String sheetName, int rowNum) {
		Map<Integer, String> cells = new TreeMap<>();
		for (int index = 0; index < getColCount(sheetName); index++) {
			cells.put(index, getCellValue(sheetName, rowNum, index));
		}
		return cells;
	}
}
