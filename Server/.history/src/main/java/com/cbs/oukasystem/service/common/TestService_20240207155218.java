package com.cbs.oukasystem.service.common;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Map;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.MessageEnum.EnumResponseCheck;
import com.cbs.oukasystem.common.PdfUtils;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.service.order.OrderService;

@Service
@Transactional
public class TestService {

    @Autowired
    I18nService i18nService;

    @Autowired
    OrderService orderService;

    public String i18nTest() {
        throw new BaseException(EnumResponseCheck.REQUEST_SYNTAX_ERROR);
        // String message = i18nService.getMessage("response.syntax.error");
        // return message;
    }

    public void pdf() {
        try {
            Map<String,Object> dataMap = new TreeMap<String,Object>();
            map.put("customerName","鈴木様"),
            PdfUtils.tablePdf(,"table.pdf");
        } catch (InterruptedIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
