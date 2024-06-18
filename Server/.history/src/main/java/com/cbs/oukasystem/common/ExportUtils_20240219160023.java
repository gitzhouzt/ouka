package com.cbs.oukasystem.common;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportsUtils {

    /**
     * csvファイルをダウンロードする。
     * 
     * @param screenName    画面名称
     * @param fieldList     タイトル名称
     * @param sheetDataList ダウンロード情報
     */
    @SuppressWarnings("rawtypes")
    public static void downLoadCSVFile(String screenName, String[][] dataMap,
            List sheetDataList) {

        // ファイル名
        String downDate = CommonUtils.getCurrentDateNo();
        String filename = screenName + "_" + downDate + ".xls";

        try {

            File name = new File(filename);
            WritableWorkbook workbook = Workbook.createWorkbook(name);
            WritableSheet sheet = workbook.createSheet(screenName, 0);
            WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

            WritableCellFormat cellFormat = new WritableCellFormat(font);
            cellFormat.setBackground(Colour.WHITE);
            cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            sheet.getSettings().setDefaultColumnWidth(20);
            cellFormat.setWrap(true);

            // 字段名
            for (int i = 0; i < dataMap.length; i++) {
                Label label = new Label(i, 0, dataMap[i][0], cellFormat);
                sheet.addCell(label);
            }
            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
            cellFormat2.setAlignment(Alignment.LEFT);
            cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
            cellFormat2.setBackground(Colour.WHITE);
            cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
            cellFormat2.setWrap(true);

            // 字段值
            int row = 1;
            if (sheetDataList != null && sheetDataList.size() > 0) {
                for (Object object : sheetDataList) {
                    Field[] fields = object.getClass().getDeclaredFields();
                    for (int i = 0; i < dataMap.length; i++) {
                        if (i == 0) {
                            Label lt0 = new Label(i, row, row + "", cellFormat2);
                            sheet.addCell(lt0);
                        } else {
                            for (Field field : fields) {
                                field.setAccessible(true);
                                if (dataMap[i][1].equals(field.getName())) {
                                    String value = field.get(object) != null ? field.get(object) + "" : "";
                                    Label lt0 = new Label(i, row, value, cellFormat2);
                                    sheet.addCell(lt0);
                                }
                            }
                        }
                    }
                    row++;
                }
            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        export(filename);
    }

    /**
     * ｃｓｖファイルデータを取得すること。
     * 
     * @param inputStream アップロードファイル
     */
    public static List<List<String>> readCSVFile(InputStream inputStream) {
        List<List<String>> resultList = new ArrayList<>();

        if (inputStream == null) {
            return resultList;
        }

        try {
            jxl.Workbook rwb = Workbook.getWorkbook(inputStream);
            Sheet[] sheet = rwb.getSheets();
            for (int i = 0; i < sheet.length; i++) {
                Sheet rs = rwb.getSheet(i);
                for (int j = 0; j < rs.getRows(); j++) {
                    Cell[] cells = rs.getRow(j);
                    List<String> list = new ArrayList<>();
                    for (int k = 0; k < cells.length; k++) {
                        list.add(cells[k].getContents());
                    }
                    resultList.add(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * ｃｓｖファイルデータを取得すること。
     * 
     * @param inputStream アップロードファイル
     */
    public static List<List<String>> readXlsxFile(InputStream inputStream) {
        List<List<String>> resultList = new ArrayList<>();

        if (inputStream == null) {
            return resultList;
        }

        try {
            //
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static void export(String dest) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();

        OutputStream out = null;
        try {
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(dest, "utf-8"));
            out = response.getOutputStream();
            InputStream is = new FileInputStream(dest);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportPdf(String dest) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();

        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream;charset=utf-8");
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(dest, "utf-8"));
            out = response.getOutputStream();
            InputStream is = new FileInputStream(dest);
            byte[] b = new byte[4096];
            int size = is.read(b);
            while (size > 0) {
                out.write(b, 0, size);
                size = is.read(b);
            }
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
