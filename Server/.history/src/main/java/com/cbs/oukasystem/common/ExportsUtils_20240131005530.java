package com.cbs.oukasystem.common;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.excel.EasyExcel;
import com.cbs.oukasystem.entity.order.OrderEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

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

    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<OrderEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderEntity data = new OrderEntity();
            data.setId("字符串" + 0);
            data.setOrderNo(CommonUtils.getCurrentDate());
            list.add(data);
        }

        EasyExcel.write(response.getOutputStream(), OrderEntity.class).sheet("模板").doWrite(list);
    }

    /**
     * csvファイルをダウンロードする。
     * 
     * @param screenName    画面名称
     * @param fieldList     タイトル名称
     * @param sheetDataList ダウンロード情報
     */
    @SuppressWarnings("rawtypes")
    public static void downLoadCSVFile(String screenName, List<String> fieldList,
            List sheetDataList) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();

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

            if (fieldList != null && fieldList.size() > 0) {
                for (int index = 0; index < fieldList.size(); index++) {
                    Label label = new Label(index, 0, fieldList.get(index), cellFormat);
                    sheet.addCell(label);
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

            int row = 1;
            if (sheetDataList != null && sheetDataList.size() > 0) {
                for (Object object : sheetDataList) {
                    Field[] fields = object.getClass().getDeclaredFields();
                    int index = 0;
                    for (Field field : fields) {
                        if (index >= fieldList.size()) {
                            break;
                        }
                        field.setAccessible(true);
                        if (index == 0) {
                            Label lt0 = new Label(index, row, row + "", cellFormat2);
                            sheet.addCell(lt0);
                        } else {
                            Label lt0 = new Label(index, row, field.get(object) + "", cellFormat2);
                            sheet.addCell(lt0);
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

        OutputStream out = null;
        try {
            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(filename, "utf-8"));
            out = response.getOutputStream();
            InputStream is = new FileInputStream(filename);
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
}
