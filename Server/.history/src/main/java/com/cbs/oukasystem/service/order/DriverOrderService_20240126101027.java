package com.cbs.oukasystem.service.order;

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
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driver.TodayCarVO;
import com.cbs.oukasystem.vo.out.driver.TodayVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;

@Service
@Transactional
public class DriverOrderService {

    String KEY = "司机端";

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    /*
     * idによって注文を得る
     */

    public OrderVO getById(String id) {
        return OrderVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /*
     * 注文を得る
     */
    public OrderEntity getEntity(String id) {
        Optional<OrderEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    public TodayVO getTodayOrder() {
        String userId = LoginUtils.getLoginUserId();
        TodayVO driverVO = new TodayVO();
        driverVO.setId(userId);
        driverVO.setUserDriveVO(userService.getUserDrierVO(userId));
        String today = CommonUtils.getCurrentDate("yyyy/MM/dd");
        driverVO.setToday(today);
        List<TodayCarVO> carVOs = queryTodayOrder(userId);
        List<TodayCarVO> todayCarVOs = new ArrayList<>();
        List<TodayCarVO> tomorrowCarVOs = new ArrayList<>();
        for (TodayCarVO carVO : carVOs) {
            if (CommonUtils.getFormatDate(carVO.getStartTime(), "yyyy/MM/dd").equals(today)) {
                todayCarVOs.add(carVO);
            } else {
                tomorrowCarVOs.add(carVO);
            }
        }
        driverVO.setOrderCnt(carVOs.size());
        driverVO.setCarVOs(carVOs);
        return driverVO;
    }

    public List<TodayCarVO> queryTodayOrder(String driverId) {
        List<Map> maps = repository.queryTodayOrder(driverId);
        Object data = maps;
        List<TodayCarVO> list = (List<TodayCarVO>) data;
        return list;
    }

    public List<TodayCarVO> queryTodayCar(String driverId) {
        List<Map> maps = repository.queryTodayByDriver(driverId);
        Object data = maps;
        List<TodayCarVO> list = (List<TodayCarVO>) data;
        return list;
    }

    /*
     * 全ての注文
     */

    public List<OrderVO> getAll() {
        return OrderVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * 注文のリスト
     */

    public ListVO<OrderVO> getPages(QueryOrderVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.ASC, "startTime");
        Specification<OrderEntity> specification = Search(qVo);
        Page<OrderEntity> pages = repository.findAll(specification, pageable);
        List<OrderVO> list = OrderVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<OrderVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 注文検索
     */
    public Specification<OrderEntity> Search(QueryOrderVO qVo) {
        return new Specification<OrderEntity>() {
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                // keyword
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preOrderNo = cb.like(root.get("orderNo"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preCustomerName = cb.like(root.get("customerName"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preOrderKey = cb.like(root.get("orderKey"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preOrderSource = cb.like(root.get("orderSource"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preCarName = cb.like(root.get("carName"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent1 = cb.like(root.get("contactContent1"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent2 = cb.like(root.get("contactContent2"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent3 = cb.like(root.get("contactContent3"),
                            "%" + qVo.getKeyword() + "%");
                    predicate.add(
                            cb.or(preOrderNo, preCustomerName,
                                    preOrderKey, preOrderSource, preCarName, preContent1, preContent2, preContent3));
                }

                // ドライバー ID
                String userId = LoginUtils.getLoginUserId();
                predicate.add(cb.equal(root.get("driverId"), userId));

                // 配車済
                predicate.add(cb.equal(root.get("orderStatus"), EnumOrderStatus.Booked));

                // 只显示当天和第二天订单
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date()); // 当天
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Date startTime = calendar.getTime();
                Predicate preBeginTime = cb.greaterThanOrEqualTo(root.get("startTime"),
                        startTime);
                predicate.add(preBeginTime);
                calendar.add(Calendar.DATE, 1);// 第二天
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                Date endTime = calendar.getTime();
                Predicate preEndTime = cb.lessThanOrEqualTo(root.get("startTime"), endTime);
                predicate.add(preEndTime);

                if (null != qVo.getOrderStatus() && qVo.getOrderStatus().length > 0) {
                    Predicate[] status = new Predicate[qVo.getOrderStatus().length];
                    int index = 0;
                    for (EnumOrderStatus orderStatus : qVo.getOrderStatus()) {
                        status[index] = cb.equal(root.get("orderStatus"), orderStatus);
                        index++;
                    }
                    predicate.add(cb.or(status));
                }

                predicate.add(cb.equal(root.get("isAudit"), true));
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

}
