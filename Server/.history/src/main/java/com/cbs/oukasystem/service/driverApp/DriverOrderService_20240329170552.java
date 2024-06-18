package com.cbs.oukasystem.service.driverApp;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
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
import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.finance.PayRecordService;
import com.cbs.oukasystem.service.order.OrderFileService;
import com.cbs.oukasystem.service.order.OrderGoodsService;
import com.cbs.oukasystem.service.order.OrderService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderGoodsVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.user.QueryUserVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driverApp.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderFileVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;
import com.cbs.oukasystem.vo.out.user.UserVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

@Service
@Transactional
public class DriverOrderService {

    String KEY = "司机端-Order";

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderGoodsService goodsService;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private OrderFileService fileService;

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
    public OrderEntity getEntity(@NonNull String id) {
        Optional<OrderEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    public OrderDetailsVO getDetails(String id) {
        OrderDetailsVO detailsVO = new OrderDetailsVO();
        detailsVO.setId(id);
        List<OrderGoodsVO> orderGoodsVOs = goodsService.getByOrderId(id);
        List<PayRecordVO> payRecordVOs = payRecordService.getByCashOrderId(id);
        List<OrderFileVO> orderFileVOs = fileService.getByOrderIdAndShare(id, true);
        detailsVO.setOrderVO(getById(id));
        detailsVO.setOrderGoodsVOs(orderGoodsVOs);
        detailsVO.setPayRecordVOs(payRecordVOs);
        detailsVO.setOrderFileVOs(orderFileVOs);
        return detailsVO;
    }

    public Boolean confirm(String id) {
        OrderEntity entity = getEntity(id);
        entity.setOrderStatus(EnumOrderStatus.Booked);
        repository.save(entity);
        return true;
    }

    public Boolean work(String id) {
        OrderEntity entity = getEntity(id);
        entity.setOrderStatus(EnumOrderStatus.Working);
        repository.save(entity);
        return true;
    }

    public Boolean complete(String id) {
        OrderEntity entity = getEntity(id);
        entity.setOrderStatus(EnumOrderStatus.Completed);
        repository.save(entity);
        return true;
    }

    public PayRecordVO driverPay(IUPayRecordVO iuVo) {
        PayRecordVO payRecordVO = new PayRecordVO();
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            iuVo.setAuditByName(null);
            iuVo.setIsAudit(false);
            iuVo.setStatus(EnumStatus.Waiting);
            iuVo.setStatusName(EnumStatus.Waiting.getMessage());
            payRecordVO = payRecordService.addOrEdit(iuVo);
        } else {
            OrderEntity entity = getEntity(iuVo.getOrderId());
            iuVo.setOrder(entity);
            UserEntity userEntity = userService.getEntity(LoginUtils.getLoginUserId());
            iuVo.setCreateByName(userEntity.getUserName());
            iuVo.setAuditByName(null);
            iuVo.setIsAudit(false);
            iuVo.setStatus(EnumStatus.Waiting);
            iuVo.setStatusName(EnumStatus.Waiting.getMessage());
            payRecordVO = payRecordService.addOrEdit(iuVo);
        }
        return payRecordVO;
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

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date()); // 当天
                calendar.add(Calendar.DATE, 1);// 第二天

                LocalDate startTime = LocalDate.parse(CommonUtils.getCurrentDate("yyyy-MM-dd"));
                LocalDate endTime = LocalDate.parse(CommonUtils.getFormatDate(calendar.getTime(), "yyyy-MM-dd"));
                predicate.add(cb.between(root.get("startTime").as(LocalDate.class), startTime,
                        endTime));

                // 待确认 已分配 已完成 作业中
                List<EnumOrderStatus> statusList = new ArrayList<>();
                statusList.add(EnumOrderStatus.Check);
                statusList.add(EnumOrderStatus.Booked);
                statusList.add(EnumOrderStatus.Working);
                statusList.add(EnumOrderStatus.Completed);
                Predicate[] status = new Predicate[statusList.size()];
                int index = 0;
                for (EnumOrderStatus orderStatus : statusList) {
                    status[index] = cb.equal(root.get("orderStatus"), orderStatus);
                    index++;
                }
                predicate.add(cb.or(status));

                predicate.add(cb.equal(root.get("isAudit"), true));
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

}
