package com.cbs.oukasystem.service.driverApp;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.finance.PayRecordService;
import com.cbs.oukasystem.service.order.OrderGoodsService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderGoodsVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driverApp.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

@Service
@Transactional
public class DriverUserService {

    String KEY = "司机端-User";

    @Autowired
    private UserService userService;

    public ListVO<UserLogVO> getLogs() {
        String userId = LoginUtils.getLoginUserId();
        ListVO<UserLogVO> listDto = userService.getUserLogs(userId);
        return listDto;
    }

}
