package com.wipro.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelBase {

	private FileInputStream fis;
	public  XSSFSheet sheet;
	public  XSSFWorkbook wbook;
	private Row row;

	public void createSheet(String sheetname) throws IOException{
		//		create an object of Workbook for the excel file to write
		wbook = new XSSFWorkbook();
		sheet = wbook.createSheet(sheetname);
	}

	public void writeExcel(String outputExcelFilename) throws IOException 
	{
		outputExcelFilename = System.getProperty("user.dir")+"\\resources\\output_data\\" + outputExcelFilename;
		
		FileOutputStream fos = new FileOutputStream(outputExcelFilename);
		wbook.write(fos);
		fos.close();
		wbook.close();
	}

	public void createRow(int RowNum) {
		row = sheet.createRow(RowNum);
	}

	public void setCellData(int ColNum, String value)  {
		row.createCell(ColNum).setCellValue(value);
	}

	public excelBase() {}

	public excelBase(String excelFileName, String sheetName) throws IOException {
		File filepath=new File(System.getProperty("user.dir")+"\\resources\\testdata\\" + excelFileName);
		fis =new FileInputStream(filepath);
		wbook=new XSSFWorkbook(fis);
		sheet = wbook.getSheet( sheetName);
	}

	public excelBase(String excelFileName) throws IOException {
		File filepath=new File(System.getProperty("user.dir")+"\\resources\\testdata\\" + excelFileName);
		fis =new FileInputStream(filepath);
		wbook=new XSSFWorkbook(fis);
	}

	public void getSheet(String sheetName) {
		sheet=	wbook.getSheet( sheetName);
	}

	public Object[][] getDataTable()    throws Exception
	{   
		String[][] dataSet = new String[this.getRows()][this.getCols()];
		for(int row=1; row <= this.getRows(); row++)
		{
			for(int col=0; col < this.getCols(); col++)
			{
				dataSet[row-1][col]=	this.getCellData(row, col);
			}
		}
		return (dataSet);
	}

	public int getRows() {
		return sheet.getLastRowNum();
	}


	public int getCols() {
		return sheet.getRow(1).getLastCellNum();

	}

	public String getCellData(int RowNum, int ColNum)  {

		try{
			XSSFCell cell = sheet.getRow(RowNum).getCell(ColNum);
			switch (cell.getCellTypeEnum()) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return cell.getNumericCellValue()+"";
			default:
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return "";
	}


	public void close() throws IOException
	{
		if ( fis != null)  fis.close();
		wbook.close();
	}
}
