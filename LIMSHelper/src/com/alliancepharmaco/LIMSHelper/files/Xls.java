package com.alliancepharmaco.LIMSHelper.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/*
 * @author Jiangbo Tang
 * @version 15-Aug-2014
 */
public class Xls {
	//map each cell of a row: key = columnIndex, value = cell content
	protected Map<Integer, Object> rowData = new HashMap<Integer, Object>();
	//map each row of a sheet: key = rowIndex, value = row content
	protected Map<Integer, Map<Integer, Object>> sheetData = new HashMap<Integer, Map<Integer, Object>>();
	
	public Xls (File file) {
		init(file);
	}
	
	/*
	 * Convert a cell to a string. Handles numeric cell value too.
	 */
	public static String cellToString(Cell cell) {
		String s = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				s = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				s = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_ERROR:
				s = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				s = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				//cell.getNumericCellValue returns a double
				s = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				s = cell.getStringCellValue();
				break;
			default:
				s = "";
				break;				
			}
		}
		return s;
	}
	
	/*
	 * Read in a .xls file and store its data in Map<Integer, Map<Integer, Object>>
	 * outer map: key = row index of a sheet of .xls
	 * inner map: key = cell index of a row, value = cell content
	 */
	private void init(File f) {
		try {
			FileInputStream file = new FileInputStream(f);
			//Create workbook instance reference to .xls file
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			//Get first/desired sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			//Iterate rows one by one
			Iterator<Row> rowIterator = sheet.rowIterator();
			Integer rownum = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				Map<Integer, Object> rowData = new HashMap<Integer, Object>();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					rowData.put(cell.getColumnIndex(), cell);
				}
				sheetData.put(rownum, rowData);
				rownum++;
			}
			file.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Write data stored in 'sheetData' to a .xls file
	 */
	public void toXls(File file) {
		//Blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		//Create a blank sheet
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		
		//Iterate over 'sheetData' and write to HSSFWorkbook
		for (int i=0; i < sheetData.size(); i++) {
			Row row = sheet.createRow(i);
			Map<Integer, Object> rowData = sheetData.get(i);
			Set<Integer> keyset = rowData.keySet();
			for (Integer key : keyset) {
				Cell cell = row.createCell(key);
				cell.setCellValue(rowData.get(key).toString());
			}		
		}
		//Write HSSFWorkbook to .xls file
		try {
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Return a column of .xls indexed by 'columnIndex' as Map<Integer, Object>
	 * key = row index, value = cell content
	 * only rows with index >= rowindex will be returned
	 */
	public Map<Integer, Object> getColumn (int columnIndex, int rowIndex) {
		Map<Integer, Object> columnData = new HashMap<Integer, Object>(); 
		for (int i=rowIndex; i < sheetData.size(); i++) {
			columnData.put(i, sheetData.get(i).get(columnIndex));
		}
		return columnData;
	}
	
	/*
	 * Return the first row of a .xls file as Map<Integer, Object>
	 * key = column index
	 * value = cell content
	 */
	public Map<Integer, Object> getHeader() {
		return sheetData.get(0);
	}
	
	public Map<Integer, Map<Integer, Object>> getData() {
		return sheetData;
	}
	
	public void setData(Map<Integer, Map<Integer, Object>> data) {
		sheetData = data;
	}
	
	/*
	 * String presentation of the .xls object
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = "";
		for (int i=0; i < sheetData.size(); i++) {
			Map<Integer, Object> rowData = sheetData.get(i);
			//Make a copy of rowData with key sorted
			TreeMap<Integer, Object> sortedRowData = new TreeMap<Integer, Object>(rowData);
			for (Map.Entry<Integer, ?> entry : sortedRowData.entrySet()) {
				str += entry.getValue().toString();
				str += "\t";
			}
			str += "\n";
		}
		
		return str;
	}
	
	
//////////////////////////////////Test Methods////////////////////////////////////////
	public static void testConstructor(String fileAddr) {
		File file = new File(fileAddr);
		Xls xls = new Xls(file);
		int rownum = xls.sheetData.size();
		for (int i=0; i<rownum; i++) {
			Map<Integer, Object> cMap = xls.sheetData.get(i);
			Set<Integer> keyset = cMap.keySet();
			for (Integer key : keyset) {
				System.out.print("column:" + key + ": " + cMap.get(key) + "\t");
			}
			System.out.println("");
		}
	}
	
	public static void testGetColumn() {
		File file = new File(".\\resrc\\manifest.xls");
		Xls xls = new Xls(file);
		Map<Integer, Object> columnData = xls.getColumn(12, 0);
		Set<Integer> keyset = columnData.keySet();
		for (Integer key : keyset) {
			System.out.print("row:" + key + ": " + columnData.get(key) + "\t");
			System.out.println("");
		}
	}
	
	public static void testToString() {
		File file = new File(".\\resrc\\out_97.xls");
		Xls xls = new Xls(file);
		System.out.print(xls.toString());
	}
	
	public static void test() {
//		testConstructor("C:\\Eclipse\\eclipse-standard-luna-R-win32\\eclipse\\workplace\\LIMSHelper\\resrc\\out_97.xls");
//		Xls xls = new Xls("C:\\Eclipse\\eclipse-standard-luna-R-win32\\eclipse\\workplace\\LIMSHelper\\resrc\\out_97.xls");
//		xls.toXls("C:\\Eclipse\\eclipse-standard-luna-R-win32\\eclipse\\workplace\\LIMSHelper\\resrc\\in_97.xls");
		testGetColumn();
	}
}
