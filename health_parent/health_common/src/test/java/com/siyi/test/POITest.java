package com.siyi.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class POITest {
/*    @Test
    public void test1() throws IOException {
        //加载指定文件，创建一个Excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("E:\\123.xlsx")));
        //读取Excel文件中第一个Sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);
        //遍历Sheet标签页，获得每一行数据
        for(Row row:sheet){
            for (Cell cell:row){
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }

    @Test
    public void test2() throws Exception{
        //加载指定文件，创建一个Excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("E:\\123.xlsx")));
        //读取Excel文件中第一个Sheet表情页
        XSSFSheet sheet = excel.getSheetAt(0);
        //获取当前工作表中最后一个行号，需要注意，行号从零开始
        int lastRowNum = sheet.getLastRowNum();
        for(int i=0;i<=lastRowNum;i++){
            XSSFRow row = sheet.getRow(i);
            //获得当前行最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
            for(int j=0;j<lastCellNum;j++){
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }

    //使用POI想Excel文件写入数，并且通过输出流将创建的Excel文件保存到本地磁盘
    @Test
    public void test3() throws IOException {
        //在内存中创建一个Excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作对象
        XSSFSheet sheet = excel.createSheet("私忆");
        //在工作表中创建行对象
        XSSFRow title = sheet.createRow(0);
        //在行中，建单元格对象
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小明");
        dataRow.createCell(1).setCellValue("上海");
        dataRow.createCell(2).setCellValue("20");

        //创建一个输出流，通过输出流将对象内存中的Excel文件写到磁盘
        FileOutputStream out = new FileOutputStream(new File("e:\\hello.xlsx"));
        excel.write(out);
        out.flush();
        excel.close();
    }*/
}
