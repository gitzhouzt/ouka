package com.cbs.oukasystem.service.driverApp;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.user.UserDriveVOEntityMapStruct;
import com.cbs.oukasystem.service.order.OrderService;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.finance.PayRecordService;
import com.cbs.oukasystem.service.operate.CallService;
import com.cbs.oukasystem.service.order.OrderGoodsService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderGoodsVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driverApp.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayOrderVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

@Service
@Transactional
public class DriverHomeService {

        String KEY = "司机端-Home";

        @Autowired
        private OrderService orderService;

        @Autowired
        private UserService userService;

        @Autowired
        private CallService callService;

        public TodayVO getTodayOrders() {
                String userId = LoginUtils.getLoginUserId();
                TodayVO driverVO = new TodayVO();
                UserEntity user = userService.getEntity(userId);
                driverVO.setId(userId);
                driverVO.setUserName(user.getUserName());
                driverVO.setUserNo(user.getUserNo());
                String today = CommonUtils.getCurrentDate("yyyy/MM/dd");
                driverVO.setToday(today);
                List<TodayOrderVO> list = orderService.queryTodayOrders(userId);
                driverVO.setOrderCnt(list.size());// 总订单数
                driverVO.setTodayOrderVOs((List<TodayOrderVO>) list);
                List<CallVO> callVOs = callService.getByDriverIdAndDate(userId, today);
                driverVO.setCallVOs(callVOs);
                driverVO.setAmCall(0);
                driverVO.setPmCall(0);
                if (callVOs.size() > 0) {
                        driverVO.setAmCall(callVOs.stream()
                                        .filter(c -> c.getAm1().equals("有") && c.getAm2().equals("無")
                                                        && c.getAm3().equals("無")
                                                        && c.getAm4().equals("無"))
                                        .collect(Collectors.toList()).size() > 0 ? 1 : 2);
                        driverVO.setAmCall(callVOs.stream()
                                        .filter(c -> c.getAm1().equals("有") && c.getAm2().equals("無")
                                                        && c.getAm3().equals("無")
                                                        && c.getAm4().equals("無"))
                                        .collect(Collectors.toList()).size() > 0 ? 1 : 2);
                }
                return driverVO;
        }

}
