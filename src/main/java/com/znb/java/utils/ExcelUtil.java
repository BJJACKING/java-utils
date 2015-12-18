package com.znb.java.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 下午5:10
 */
public class ExcelUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 将excel对象写入文件
     *
     * @param wb
     * @param fileName
     */
    public static void wirteaWorkbook(HSSFWorkbook wb, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            wb.write(fos);
        } catch (FileNotFoundException e) {
            LOG.error("wirte exception!", e);
        } catch (IOException e) {
            LOG.error("wirte exception!", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOG.error("wirte exception!", e);
                } finally {
                    fos = null;
                }
            }
        }
    }

    /**
     * 创建excel的sheet
     *
     * @param wb
     * @param sheetName
     * @return
     */
    public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName) {
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(12);
        sheet.setGridsPrinted(false);
        sheet.setGridsPrinted(false);
        return sheet;
    }

    public static CellStyle createStyle(HSSFWorkbook wb, short backgroundColor, short foregroundColor, short halign, Font font) {
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(halign);
        cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cs.setFillBackgroundColor(backgroundColor);
        cs.setFillForegroundColor(foregroundColor);
        cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cs.setFont(font);
        return cs;

    }

    /**
     * excel中添加一行
     *
     * @param sheet
     * @param rowNum 新添加行包行的列数
     * @return
     */
    public static HSSFRow createRow(HSSFSheet sheet, int rowNum) {
        HSSFRow row = sheet.createRow(rowNum);
        return row;
    }

    /**
     * 在一行中添加一个cell,并返回这个cell
     * @param row     添加cell的行
     * @param cellNum cell的列号
     * @param style   cell的类型,通过.createCellStyle()获取
     * @return
     */
    public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style) {
        HSSFCell cell = row.createCell(cellNum);
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 在一行中添加一个cell,并返回这个cell
     *
     * @param row     添加cell的行
     * @param cellNum cell的列号
     * @return
     */
    public static HSSFCell createCell(HSSFRow row, int cellNum) {
        HSSFCell cell = row.createCell(cellNum);
        return cell;
    }

    public static void main(String[] args) {
        List<String> sortedDateList = new ArrayList<String>();
        sortedDateList.add("2015-02-03");
        sortedDateList.add("2015-02-04");
        sortedDateList.add("2015-02-05");
        sortedDateList.add("2015-02-06");
        sortedDateList.add("2015-02-07");
        sortedDateList.add("2015-02-08");
        sortedDateList.add("2015-02-09");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = ExcelUtil.createSheet(wb, "znb");
        HSSFCellStyle style = wb.createCellStyle();
        int rowIndex = 0;
        int headColumnIndex = 0;
        HSSFRow row = ExcelUtil.createRow(sheet, rowIndex++);
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("公司名称");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("公司地址");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("站点");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("合作金额");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("合作模式");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("位置");
        for (String date : sortedDateList) {
            int indexTemp = headColumnIndex;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, headColumnIndex, ++headColumnIndex));
            ExcelUtil.createCell(row, indexTemp, style).setCellValue(date);
            headColumnIndex++;
        }
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("本周/本月总收入");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("上线系统");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("量级占比");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("点击用户机型占比");
        ExcelUtil.createCell(row, headColumnIndex++, style).setCellValue("备注");
        ExcelUtil.wirteaWorkbook(wb, "/home/dell/exc.xls");

    }

}
