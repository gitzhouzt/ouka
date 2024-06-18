package com.cbs.oukasystem.service.driverApp;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.operate.CallEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.operate.CallVOEntityMapStruct;
import com.cbs.oukasystem.service.order.OrderService;
import com.cbs.oukasystem.service.operate.CallService;
import com.cbs.oukasystem.service.user.UserLogService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.in.operate.IUCallVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayOrderVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

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

    @Autowired
    private UserLogService logService;

    public TodayVO getTodayOrders() {
        TodayVO driverVO = new TodayVO();
        try {
            String userId = LoginUtils.getLoginUserId();
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
            if (callVOs.size() > 0) {
                int amCall = 0;
                int pmCall = 0;
                CallVO callVO = callVOs.get(0);
                if (null != callVO.getAm1() && null != callVO.getAm2() && null != callVO.getAm3()
                        && null != callVO.getAm4()) {
                    amCall = callVOs.stream()
                            .filter(c -> c.getAm1().equals("有") && c.getAm2().equals("無") && c.getAm3().equals("無")
                                    && c.getAm4().equals("無"))
                            .collect(Collectors.toList()).size() > 0 ? 1 : 2;
                }
                if (null != callVO.getPm1() && null != callVO.getPm2() && null != callVO.getPm3()
                        && null != callVO.getPm4()) {
                    pmCall = callVOs.stream()
                            .filter(c -> c.getPm1().equals("有") && c.getPm2().equals("無") && c.getPm3().equals("無")
                                    && c.getPm4().equals("無"))
                            .collect(Collectors.toList()).size() > 0 ? 1 : 2;
                }
                driverVO.setAmCall(amCall);
                driverVO.setPmCall(pmCall);
            }
            List<UserLogVO> logVOs = logService.findByDriver1IdAndDate(userId, today);
            driverVO.setLog(logVOs.size());
        } catch (Exception e) {
            new BaseException(EnumDataCheck.ERROR, e.getMessage());
        }
        return driverVO;
    }

    public CallVO setAmCall(IUCallVO iuVo) {
        return callService.setAmCall(iuVo);
    }

    public CallVO setPmCall(IUCallVO iuVo) {
        return callService.setPmCall(iuVo);
    }

}
