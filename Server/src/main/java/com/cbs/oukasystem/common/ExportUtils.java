package com.cbs.oukasystem.common;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ExportUtils {

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
