package com.mpic.evolution.chair.util;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 *
 * @ClassName: ImpotExcelUtil
 * @Description: excel 导入数据
 * @author SJ
 * @date 2021/05/18
 * @time 上午10:28:48
 */
public class ExpotExcelUtil {
    /**
     *
     * @param title 标题
     * @param headers  表头
     * @param values  表中元素
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String title, String headers[], String [][] values){

        //创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(title);

        //创建标题合并行
        // addMergedRegion(x,y,z)是设置单元格合并
        // 第一行是标题，所以得合并第一行的单元格
        // x代表是要合并第几行，y，z表示，从第x行的第y个格子到第z个格子合并成一个格子
        hssfSheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)headers.length - 1));

        //设置标题样式
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置标题字体
        Font titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        style.setFont(titleFont);

        //设置值表头样式 设置表头居中
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        hssfCellStyle.setBorderTop(BorderStyle.THIN);

        //设置表内容样式
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);

        //产生标题行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);



        //产生表头
        HSSFRow row1 = hssfSheet.createRow(1);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell hssfCell = row1.createCell(i);
            hssfCell.setCellValue(headers[i]);
            hssfCell.setCellStyle(hssfCellStyle);
        }

        //创建内容
        for (int i = 0; i <values.length; i++){
            row1 = hssfSheet.createRow(i +2);
            for (int j = 0; j < values[i].length; j++){
                //将内容按顺序赋给对应列对象
                HSSFCell hssfCell = row1.createCell(j);
                hssfCell.setCellValue(values[i][j]);
                hssfCell.setCellStyle(style1);
            }
        }
        return hssfWorkbook;
    }
}
