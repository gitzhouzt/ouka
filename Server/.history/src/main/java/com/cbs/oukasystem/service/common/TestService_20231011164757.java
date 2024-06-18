package com.cbs.oukasystem.service.common;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.MessageEnum.EnumResponseCheck;
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
}
