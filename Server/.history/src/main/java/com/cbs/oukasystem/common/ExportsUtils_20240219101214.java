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
    public static void downLoadCSVFile(String screenName, Map<String, String> dataMap,
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
            if (dataMap != null && dataMap.size() > 0) {
                int index = 0;
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    Label label = new Label(index, 0, entry.getKey(), cellFormat);
                    sheet.addCell(label);
                    index++;
                }
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
                    int index = 0;
                    for (Field field : fields) {
                        if (index >= dataMap.size()) {
                            break;
                        }
                        field.setAccessible(true);
                        if (index == 0) {
                            Label lt0 = new Label(index, row, row + "", cellFormat2);
                            sheet.addCell(lt0);
                        } else {
                            int j = 0;
                            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                                if (entry.getValue().equals(field.getName())) {
                                    Label lt0 = new Label(j, row, field.get(object) + "", cellFormat2);
                                    sheet.addCell(lt0);
                                }
                                j++;
                            }
                        }
                        index++;
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
