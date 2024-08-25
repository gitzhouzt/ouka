package com.cbs.oukasystem.service.order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderType;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.EmailUtils;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumInsertCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumOrderCheck;
import com.cbs.oukasystem.common.PdfUtils;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.entity.car.CarEntity;
import com.cbs.oukasystem.entity.finance.AdvanceEntity;
import com.cbs.oukasystem.entity.finance.EarningsEntity;
import com.cbs.oukasystem.entity.finance.PayRecordEntity;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.entity.order.DeployRecordEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.mapstruct.finance.PayRecordVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderRepository;
import com.cbs.oukasystem.service.base.DictItemService;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.finance.AdvanceService;
import com.cbs.oukasystem.service.finance.EarningsService;
import com.cbs.oukasystem.service.finance.PayRecordService;
import com.cbs.oukasystem.service.operate.ScheduleService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.QueryDictItemVO;
import com.cbs.oukasystem.vo.in.finance.IUAdvanceVO;
import com.cbs.oukasystem.vo.in.finance.IUEarningsVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.IUOrderVO;
import com.cbs.oukasystem.vo.in.order.OrderCancelVO;
import com.cbs.oukasystem.vo.in.order.OrderDeployVO;
import com.cbs.oukasystem.vo.in.order.QueryDeployRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.out.base.DictItemVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayOrderVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.finance.SettlementVO;
import com.cbs.oukasystem.vo.out.order.DeployDetailsVO;
import com.cbs.oukasystem.vo.out.order.DeployRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.itextpdf.text.DocumentException;

import lombok.NonNull;

@Service
@Transactional
public class OrderService {

    String KEY = "注文";

    @Autowired
    private OrderRepository repository;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private AdvanceService advanceService;

    @Autowired
    private EarningsService earningsService;

    @Autowired
    private DeployRecordService deployRecordService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private DictItemService itemService;

    @Autowired
    private EmailUtils emailUtils;

    // #region Order 订单

    // #region Query And Export

    /**
     * get OrderVO by orderId
     * 
     * @param id orderId
     * @return OrderVO
     */
    public OrderVO getById(String id) {
        return OrderVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /**
     * get OrderEntity by orderId
     * 
     * @param id orderId
     * @return OrderEntity
     */
    public OrderEntity getEntity(@NonNull String id) {
        Optional<OrderEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /**
     * all list
     * 
     * @return List OrderVO
     */
    public List<OrderVO> getAll() {
        return OrderVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    public List<TodayOrderVO> queryTodayOrders(String driverId) {
        List<Map<String, String>> maps = repository.queryTodayOrders(driverId);
        Object data = maps;
        List<TodayOrderVO> list = (List<TodayOrderVO>) data;
        return list;
    }

    /**
     * pages
     * 
     * @param qVo QueryOrderVO
     * @return ListVO OrderVO
     */
    public ListVO<OrderVO> getPages(QueryOrderVO qVo) {
        String sort = "updateTime";
        Direction dir = Sort.Direction.DESC;
        if (null != qVo.getSort() && !qVo.getSort().isEmpty()) {
            sort = qVo.getSort();
        }
        if (null != qVo.getSortType()) {
            if (qVo.getSortType().equals("ascend")) {
                dir = Sort.Direction.ASC;
            } else if (qVo.getSortType().equals("descend")) {
                dir = Sort.Direction.DESC;
            } else {
                sort = "updateTime";
                dir = Sort.Direction.DESC;
            }
        }
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                dir, sort);
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

    /**
     * 検索
     * 
     * @param qVo QueryOrderVO
     * @return Specification OrderEntity
     */
    public Specification<OrderEntity> Search(QueryOrderVO qVo) {
        return new Specification<OrderEntity>() {
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> listAnd = new ArrayList<>();
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
                    Predicate preContent1 = cb.like(root.get("contactContent1"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent2 = cb.like(root.get("contactContent2"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent3 = cb.like(root.get("contactContent3"),
                            "%" + qVo.getKeyword() + "%");
                    listAnd.add(
                            cb.or(preOrderNo, preCustomerName,
                                    preOrderKey, preOrderSource, preContent1, preContent2, preContent3));
                }
                if (null != qVo.getOrderSource() && !qVo.getOrderSource().isEmpty()) {
                    listAnd.add(cb.equal(root.get("orderSource"), qVo.getOrderSource()));
                }
                if (null != qVo.getOrderKey() && !qVo.getOrderKey().isEmpty()) {
                    listAnd.add(cb.equal(root.get("orderKey"), qVo.getOrderKey()));
                }
                if (null != qVo.getCarNo() && !qVo.getCarNo().isEmpty()) {
                    listAnd.add(cb.equal(root.get("carNo"), qVo.getCarNo()));
                }
                if (null != qVo.getDriverName() && !qVo.getDriverName().isEmpty()) {
                    listAnd.add(cb.equal(root.get("driverName"), qVo.getDriverName()));
                }
                if (null != qVo.getSellerName() && !qVo.getSellerName().isEmpty()) {
                    listAnd.add(cb.equal(root.get("sellerName"), qVo.getSellerName()));
                }
                if (null != qVo.getCity() && !qVo.getCity().isEmpty()) {
                    listAnd.add(cb.equal(root.get("city"), qVo.getCity()));
                }
                if (null != qVo.getOrderType()) {
                    listAnd.add(cb.equal(root.get("orderType"), qVo.getOrderType()));
                }
                if (null != qVo.getOrderStatus() && qVo.getOrderStatus().length > 0) {
                    Predicate[] status = new Predicate[qVo.getOrderStatus().length];
                    int index = 0;
                    for (EnumOrderStatus orderStatus : qVo.getOrderStatus()) {
                        status[index] = cb.equal(root.get("orderStatus"), orderStatus);
                        index++;
                    }
                    listAnd.add(cb.or(status));
                }

                if (null != qVo.getBeginTime() && null != qVo.getEndTime()) {

                    Instant instant = qVo.getBeginTime().toInstant();
                    LocalDateTime startTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    instant = qVo.getEndTime().toInstant();
                    LocalDateTime endTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    listAnd.add(cb.between(root.get("createTime").as(LocalDateTime.class), startTime,
                            endTime));
                }
                List<Predicate> listOr = new ArrayList<>();

                // 配車が明日のデータだけを表示 明日㍘～あさって3点
                if (null != qVo.getStartBeginTime() && null != qVo.getStartEndTime()) {

                    Instant instant = qVo.getStartBeginTime().toInstant();
                    LocalDateTime startTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                    instant = qVo.getStartEndTime().toInstant();
                    LocalDateTime endTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    listAnd.add(cb.between(root.get("startTime").as(LocalDateTime.class), startTime,
                            endTime));
                }

                listAnd.add(cb.equal(root.get("isAudit"), true));
                listAnd.add(cb.notEqual(root.get("isDelete"), true));

                if (listAnd.size() > 0) {
                    Predicate[] arrayAnd = new Predicate[listAnd.size()];
                    query.where(cb.and(listAnd.toArray(arrayAnd)));
                }
                if (listOr.size() > 0) {
                    Predicate[] arrayOr = new Predicate[listOr.size()];
                    query.where(cb.and(listAnd.toArray(arrayOr)));
                }
                return query.getRestriction();
            }
        };
    }

    /**
     * export
     * 
     * @param qVo QueryOrderVO
     */
    public void export(QueryOrderVO qVo) {
        String screenName = "配車注文";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "注文番号", "orderNo" },
                { "責任人", "sellerName" },
                { "订单来源", "orderSource" },
                { "第三方", "orderKey" },
                { "サービス地域", "city" },
                { "タイプ", "orderTypeName" },
                { "お客様", "customerName" },
                { "大人数", "adultNum" },
                { "子供数", "childrenNum" },
                { "荷物数", "luggageNum" },
                { "希望車両", "specifyCarType" },
                { "ドライバー", "driverName" },
                { "車両", "carNo" },
                { "座席数", "carSeat" },
                { "基本料金", "orderPrice" },
                { "開始日付", "startTime" },
                { "旅行日数", "orderDays" },
                { "終了日付", "endTime" },
                { "出発点", "orderFrom" },
                { "出発点詳細", "orderFromDetails" },
                { "到着点", "orderTo" },
                { "到着点詳細", "orderToDetails" },
                { "航空便", "flightNo" },
                { "空港", "airport" },
                { "车费类型", "feeType" },
                { "是否收现", "isCash" },
                { "超时是否收现", "isOutTimeCash" },
                { "超时收现金额", "outTimeAmount" },
                { "请求书地址", "billingAddress" },
                { "連絡方法1", "contactMethod1" },
                { "連絡内容1", "contactContent1" },
                { "連絡方法2", "contactMethod1" },
                { "連絡内容2", "contactContent1" },
                { "連絡方法3", "contactMethod1" },
                { "連絡内容3", "contactContent1" },
                { "お客様要望", "customerRemark" },
                { "ステータス", "orderStatusName" }
        };

        Specification<OrderEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<OrderEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                OrderVOEntityMapStruct.INSTANCE.toVOs(list));
    }

    // #endregion

    // #region insert and update

    public OrderVO addOrEdit(IUOrderVO iuVo) {
        return OrderVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public OrderEntity save(OrderEntity entity) {
        if (null != entity) {
            return repository.save(entity);
        } else {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
    }

    public OrderEntity insertOrUpdate(IUOrderVO iuVo) {
        OrderEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                // 客人 订单来源等
                entity.setOrderNo(iuVo.getOrderNo());
                entity.setOrderSource(iuVo.getOrderSource());
                entity.setOrderSourceCode(iuVo.getOrderSourceCode());
                entity.setOrderKey(iuVo.getOrderKey());
                entity.setCity(iuVo.getCity());
                entity.setCustomerName(iuVo.getCustomerName());
                entity.setBillingAddress(iuVo.getBillingAddress());

                // 联系方式
                entity.setContactMethod1(iuVo.getContactMethod1());
                entity.setContactContent1(iuVo.getContactContent1());
                entity.setContactMethod2(iuVo.getContactMethod2());
                entity.setContactContent2(iuVo.getContactContent2());
                entity.setContactMethod3(iuVo.getContactMethod3());
                entity.setContactContent3(iuVo.getContactContent3());

                // 销售
                entity.setSellerId(iuVo.getSellerId());
                entity.setSellerName(iuVo.getSellerName());
                entity.setSellerNo(iuVo.getSellerNo());

            } else {
                // 追加
                entity = OrderVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                if (null == entity.getOrderNo() || entity.getOrderNo().isEmpty()
                        || entity.getOrderNo().equals(iuVo.getOrderSourceCode())) {
                    String type = "";
                    if (iuVo.getOrderType().equals(EnumOrderType.Airport_S)) {
                        type = "S";
                    } else if (iuVo.getOrderType().equals(EnumOrderType.Airport_Y)) {
                        type = "Y";
                    }
                    String orderNo = CommonUtils.getNewOrderNo(iuVo.getOrderSourceCode(), type,
                            repository.countByToday());
                    entity.setOrderNo(orderNo);
                    entity.setIsLodgingTips(true);
                }
                entity.setOrderStatus(EnumOrderStatus.Filling);
                entity.setOrderStatusName(EnumOrderStatus.Filling.getMessage());
                entity.setOrderTypeName(iuVo.getOrderType().getMessage());
            }
            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public OrderVO setCustomer(IUOrderVO iuVo) {
        return OrderVOEntityMapStruct.INSTANCE.toVO(saveCustomer(iuVo));
    }

    public OrderEntity saveCustomer(IUOrderVO iuVo) {
        OrderEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                // 客人 订单来源等
                entity.setOrderNo(iuVo.getOrderNo());
                entity.setOrderSource(iuVo.getOrderSource());
                entity.setOrderSourceCode(iuVo.getOrderSourceCode());
                entity.setOrderKey(iuVo.getOrderKey());
                entity.setCity(iuVo.getCity());
                entity.setCustomerName(iuVo.getCustomerName());
                entity.setBillingAddress(iuVo.getBillingAddress());

                // 联系方式
                entity.setContactMethod1(iuVo.getContactMethod1());
                entity.setContactContent1(iuVo.getContactContent1());
                entity.setContactMethod2(iuVo.getContactMethod2());
                entity.setContactContent2(iuVo.getContactContent2());
                entity.setContactMethod3(iuVo.getContactMethod3());
                entity.setContactContent3(iuVo.getContactContent3());

                // 销售
                entity.setSellerId(iuVo.getSellerId());
                entity.setSellerName(iuVo.getSellerName());
                entity.setSellerNo(iuVo.getSellerNo());

            } else {
                // 追加
                entity = OrderVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                entity.setOrderStatus(EnumOrderStatus.Filling);
                entity.setOrderStatusName(EnumOrderStatus.Filling.getMessage());
            }

            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public OrderVO setOrder(IUOrderVO iuVo) {
        return OrderVOEntityMapStruct.INSTANCE.toVO(saveOrder(iuVo));
    }

    public OrderEntity saveOrder(IUOrderVO iuVo) {
        OrderEntity entity = null;
        try {
            if (null == iuVo.getId() || iuVo.getId().isEmpty()) {
                throw new BaseException(EnumIOUCheck.ERROR, KEY + "Id" + EnumDataCheck.NOT_EXIST.getErrorMsg());
            }
            entity = getEntity(iuVo.getId());

            // 订单情报
            if (null == entity.getOrderNo() || entity.getOrderNo().isEmpty()
                    || entity.getOrderNo().equals(entity.getOrderSourceCode())) {
                String type = "";
                if (iuVo.getOrderType().equals(EnumOrderType.Airport_S)) {
                    type = "S";
                } else if (iuVo.getOrderType().equals(EnumOrderType.Airport_Y)) {
                    type = "Y";
                }
                String orderNo = CommonUtils.getNewOrderNo(entity.getOrderSourceCode(), type,
                        repository.countByToday());
                entity.setOrderNo(orderNo);
                entity.setIsLodgingTips(true);
            }

            entity.setOrderType(iuVo.getOrderType());
            entity.setOrderTypeName(iuVo.getOrderType().getMessage());
            entity.setOrderDays(iuVo.getOrderDays());

            /*
             * 金额转换
             */
            entity.setCurrency(iuVo.getCurrency());
            entity.setCurrencyCode(iuVo.getCurrencyCode());
            entity.setCurrencyAmount(iuVo.getCurrencyAmount());

            if (null == iuVo.getAmount() || iuVo.getAmount() == 0) {
                QueryDictItemVO qVo = new QueryDictItemVO();
                qVo.setDictCode("pay_exchange_rate");
                if (entity.getCurrencyCode().equals("jpy")) {
                    entity.setAmount(entity.getCurrencyAmount());
                } else if (entity.getCurrencyCode().equals("cny")) {
                    qVo.setItemCode("cny_jpy");
                    DictItemVO itemVO = itemService.getByCode(qVo);
                    entity.setAmount(entity.getCurrencyAmount() * Double.parseDouble(itemVO.getItemName()));
                } else if (entity.getCurrencyCode().equals("usd")) {
                    qVo.setItemCode("usd_jpy");
                    DictItemVO itemVO = itemService.getByCode(qVo);
                    entity.setAmount(entity.getCurrencyAmount() * Double.parseDouble(itemVO.getItemName()));
                }
            }

            entity.setOrderFrom(iuVo.getOrderFrom());
            entity.setOrderTo(iuVo.getOrderTo());
            entity.setOrderFromDetails(iuVo.getOrderFromDetails());
            entity.setOrderToDetails(iuVo.getOrderToDetails());
            entity.setFlightNo(iuVo.getFlightNo());
            entity.setAirport(iuVo.getAirport());
            entity.setAirportCode(iuVo.getAirportCode());
            entity.setTerminal(iuVo.getTerminal());

            entity.setLuggageNum(iuVo.getLuggageNum());
            entity.setAdultNum(iuVo.getAdultNum());
            entity.setChildrenNum(iuVo.getChildrenNum());
            entity.setSpecifyCarType(iuVo.getSpecifyCarType());
            entity.setStartTime(iuVo.getStartTime());
            entity.setEndTime(iuVo.getEndTime());

            entity.setFeeType(iuVo.getFeeType());
            entity.setIsCash(iuVo.getIsCash());
            entity.setIsOutTimeCash(iuVo.getIsOutTimeCash());
            entity.setOutTimeAmount(iuVo.getOutTimeAmount());

            entity.setIsLodgingTips(true);
            entity.setIsETC(false);

            // ドライバー
            entity.setDriverId(iuVo.getDriverId());
            entity.setDriverName(iuVo.getDriverName());
            entity.setDriverNo(iuVo.getDriverNo());

            // 車両
            entity.setCarId(iuVo.getCarId());
            entity.setCarName(iuVo.getCarName());
            entity.setCarNo(iuVo.getCarNo());
            entity.setCarSeat(iuVo.getCarSeat());
            entity.setCarType(iuVo.getCarType());

            /*
             * 車両&司机支配
             */
            OrderDeployVO deployVO = new OrderDeployVO();
            if (null != entity.getCarId() && !entity.getCarId().isEmpty()) {
                // 車両
                deployVO.setOrderId(entity.getId());
                deployVO.setOrderNo(entity.getOrderNo());
                deployVO.setCarId(entity.getCarId());
                deployVO.setCarName(entity.getCarName());
                deployVO.setCarNo(entity.getCarNo());
                deployVO.setCarSeat(entity.getCarSeat());
                deployVO.setCarType(entity.getCarType());
                carDeploy(deployVO, entity);
            }
            if (null != entity.getDriverId()
                    && !entity.getDriverId().isEmpty()) {
                // 司机
                deployVO.setDriverId(entity.getDriverId());
                deployVO.setDriverName(entity.getDriverName());
                deployVO.setDriverNo(entity.getDriverNo());
                driverDeploy(deployVO, entity);
            }
            if (null != entity.getCarId() && !entity.getCarId().isEmpty()
                    && null != entity.getDriverId()
                    && !entity.getDriverId().isEmpty()) {
                entity.setOrderStatus(EnumOrderStatus.Sending);
                entity.setOrderStatusName(EnumOrderStatus.Sending.getMessage());
            } else {
                entity.setOrderStatus(EnumOrderStatus.Assigning);
                entity.setOrderStatusName(EnumOrderStatus.Assigning.getMessage());
            }
            entity = repository.save(entity);

            // 写入立替精算表
            IUAdvanceVO iuAdvanceVO = new IUAdvanceVO();
            iuAdvanceVO.setOrderId(entity.getId());
            iuAdvanceVO.setOrder(entity);
            iuAdvanceVO.setOrderNo(entity.getOrderNo());
            iuAdvanceVO.setBillingAddress(entity.getBillingAddress());
            iuAdvanceVO.setStatus(EnumFinanceStatus.Waiting);
            iuAdvanceVO.setStatusName(EnumFinanceStatus.Waiting.getMessage());
            advanceService.addOrEdit(iuAdvanceVO);

            // 写入营业额表
            IUEarningsVO iuEarningsVO = new IUEarningsVO();
            iuEarningsVO.setOrderId(entity.getId());
            iuEarningsVO.setOrder(entity);
            iuEarningsVO.setOrderNo(entity.getOrderNo());
            earningsService.addOrEdit(iuEarningsVO);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public OrderVO setOther(IUOrderVO iuVo) {
        return OrderVOEntityMapStruct.INSTANCE.toVO(saveOther(iuVo));
    }

    public OrderEntity saveOther(IUOrderVO iuVo) {
        OrderEntity entity = null;
        try {
            if (null == iuVo.getId() || iuVo.getId().isEmpty()) {
                throw new BaseException(EnumIOUCheck.ERROR, KEY + "Id" + EnumDataCheck.NOT_EXIST.getErrorMsg());
            }
            entity = getEntity(iuVo.getId());
            entity.setCustomerRemark(iuVo.getCustomerRemark());
            entity.setCompanyRemark(iuVo.getCompanyRemark());
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    // #endregion

    // #region delete and audit

    public Boolean delete(String id) {
        OrderEntity entity = getEntity(id);
        if (entity.getOrderStatus() == EnumOrderStatus.Filling
                || entity.getOrderStatus() == EnumOrderStatus.Assigning
                || entity.getOrderStatus() == EnumOrderStatus.Sending
                || entity.getOrderStatus() == EnumOrderStatus.Check
                || entity.getOrderStatus() == EnumOrderStatus.Cancelled) {
            // キャンセルと受付待ち
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
        } else if (entity.getOrderStatus() == EnumOrderStatus.Working
                || entity.getOrderStatus() == EnumOrderStatus.Booked
                || entity.getOrderStatus() == EnumOrderStatus.Completed) {
            // 削除不可
            throw new BaseException(EnumOrderCheck.DEL_CANT);
        }
        repository.save(entity);
        return true;
    }

    /*
     * 物理削除
     */
    public Boolean deletePhysics(@NonNull String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    /*
     * 有効審査 true 有効 false 無効
     */
    public Boolean audit(String id) {
        try {
            OrderEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
    // #endregion

    // #region cancel and recover

    public Boolean cancelByUser(OrderCancelVO vo) {
        OrderEntity entity = getEntity(vo.getId());
        if (entity.getOrderStatus() == EnumOrderStatus.Filling
                || entity.getOrderStatus() == EnumOrderStatus.Assigning
                || entity.getOrderStatus() == EnumOrderStatus.Sending
                || entity.getOrderStatus() == EnumOrderStatus.Check
                || entity.getOrderStatus() == EnumOrderStatus.Booked) {
            // 受付待ちと予約済みの場合
            entity.setOrderStatus(EnumOrderStatus.Cancelled);
            entity.setOrderStatusName(EnumOrderStatus.Cancelled.getMessage());

            // 先删除日程表
            scheduleService.deletePhysicsByOrderId(entity.getId(), EnumTargetType.Driver);
            scheduleService.deletePhysicsByOrderId(entity.getId(), EnumTargetType.Car);
        } else if (entity.getOrderStatus() == EnumOrderStatus.Working
                || entity.getOrderStatus() == EnumOrderStatus.Completed
                || entity.getOrderStatus() == EnumOrderStatus.Cancelled) {
            // キャンセル不可
            throw new BaseException(EnumOrderCheck.CANCEL_CANT);
        }
        entity.setCompanyRemark(vo.getCompanyRemark());
        repository.save(entity);
        scheduleService.deletePhysicsByOrderId(vo.getId());
        return true;
    }

    public Boolean cancelByCompany(OrderCancelVO vo) {
        OrderEntity entity = getEntity(vo.getId());
        if (entity.getOrderStatus() == EnumOrderStatus.Filling
                || entity.getOrderStatus() == EnumOrderStatus.Assigning
                || entity.getOrderStatus() == EnumOrderStatus.Sending
                || entity.getOrderStatus() == EnumOrderStatus.Check
                || entity.getOrderStatus() == EnumOrderStatus.Booked) {
            // 受付待ちと予約済みの場合
            entity.setOrderStatus(EnumOrderStatus.Cancelled);
            entity.setOrderStatusName(EnumOrderStatus.Cancelled.getMessage());

            // 先删除日程表
            scheduleService.deletePhysicsByOrderId(entity.getId(), EnumTargetType.Driver);
            scheduleService.deletePhysicsByOrderId(entity.getId(), EnumTargetType.Car);
        } else if (entity.getOrderStatus() == EnumOrderStatus.Working
                || entity.getOrderStatus() == EnumOrderStatus.Completed
                || entity.getOrderStatus() == EnumOrderStatus.Cancelled) {
            // キャンセル不可
            throw new BaseException(EnumOrderCheck.CANCEL_CANT);
        }
        entity.setCompanyRemark(vo.getCompanyRemark());
        repository.save(entity);
        scheduleService.deletePhysicsByOrderId(vo.getId());

        return true;
    }

    public Boolean recover(String id) {
        OrderEntity entity = getEntity(id);
        if (null != entity.getCarId() && !entity.getCarId().isEmpty()) {
            // 注文 車両 スケジュール
            List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Date d = entity.getStartTime();
            Calendar calendar = Calendar.getInstance();
            ScheduleEntity scheduleEntity = null;
            int index = 0;
            do {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity.setTargetId(entity.getCarId());
                scheduleEntity.setTargetNo(entity.getCarNo());
                scheduleEntity.setTargetName(entity.getCarName());
                scheduleEntity.setActionId(entity.getId());
                scheduleEntity.setTargetType(EnumTargetType.Car);
                scheduleEntity.setActionType(EnumActionType.Order);
                scheduleEntity
                        .setRemark(String.format("%S %S", entity.getOrderType().getMessage(), entity.getOrderSource()));
                calendar.setTime(d);
                calendar.add(Calendar.DATE, index);
                scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
                scheduleEntity.setWorkTime(calendar.getTime());
                scheduleEntities.add(scheduleEntity);
                index++;
            } while (index < entity.getOrderDays());
            scheduleService.saveAll(scheduleEntities);

            // 如果服务时间是当天，改变车辆状态
            calendar.setTime(entity.getStartTime());
            Date today = new Date();
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(today);
            if (todayCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
                carService.resetStatus(entity.getCarId(), EnumCarStatus.Working);
            }
        }
        if (null != entity.getDriverId()
                && !entity.getDriverId().isEmpty()) {

            // 注文 ドライバー スケジュール
            List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Date d = entity.getStartTime();
            Calendar calendar = Calendar.getInstance();
            ScheduleEntity scheduleEntity = null;
            int index = 0;
            do {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity.setTargetId(entity.getDriverId());
                scheduleEntity.setTargetNo(entity.getDriverNo());
                scheduleEntity.setTargetName(entity.getDriverName());
                scheduleEntity.setActionId(entity.getId());
                scheduleEntity.setTargetType(EnumTargetType.Driver);
                scheduleEntity.setActionType(EnumActionType.Order);
                scheduleEntity.setRemark(String.format("%S %S",
                        entity.getOrderType().getMessage(), entity.getOrderSource()));
                calendar.setTime(d);
                calendar.add(Calendar.DATE, index);
                scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
                scheduleEntity.setWorkTime(calendar.getTime());
                scheduleEntities.add(scheduleEntity);
                index++;
            } while (index < entity.getOrderDays());
            scheduleService.saveAll(scheduleEntities);

            // 如果服务时间是当天，改变司机状态
            calendar.setTime(entity.getStartTime());
            Date today = new Date();
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(today);
            if (todayCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
                userService.resetStatus(entity.getDriverId(), EnumUserStatus.Working);
            }
        }
        if (null == entity.getStartTime()) {
            entity.setOrderStatus(EnumOrderStatus.Filling);
            entity.setOrderStatusName(EnumOrderStatus.Filling.getMessage());
        } else if (null != entity.getCarId() && !entity.getCarId().isEmpty()
                && null != entity.getDriverId()
                && !entity.getDriverId().isEmpty()) {
            entity.setOrderStatus(EnumOrderStatus.Sending);
            entity.setOrderStatusName(EnumOrderStatus.Sending.getMessage());
        } else {
            entity.setOrderStatus(EnumOrderStatus.Assigning);
            entity.setOrderStatusName(EnumOrderStatus.Sending.getMessage());
        }
        repository.save(entity);
        return true;
    }

    // #endregion

    /**
     * Placard
     * 
     * @param id
     * @return
     */
    public String defaultPlacard(String id) {
        OrderEntity entity = getEntity(id);
        Map<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("customerName", entity.getCustomerName());
        dataMap.put("flightNo", entity.getFlightNo());
        dataMap.put("orderTo", entity.getOrderTo());
        try {
            return PdfUtils.defaultPlacard(dataMap, "defaultPlacard_" + entity.getOrderNo() + ".pdf");
        } catch (DocumentException e) {
            throw new BaseException(EnumDataCheck.ERROR, e.getMessage());
        } catch (IOException e) {
            throw new BaseException(EnumDataCheck.ERROR, e.getMessage());
        }
    }

    /**
     * Company Remark
     * 
     * @param iuVo
     * @return
     */
    public Boolean setCompanyRemark(IUOrderVO iuVo) {
        return saveCompanyRemark(iuVo);
    }

    public Boolean saveCompanyRemark(IUOrderVO iuVo) {
        OrderEntity entity = null;
        try {
            if (null == iuVo.getId() || iuVo.getId().isEmpty()) {
                throw new BaseException(EnumIOUCheck.ERROR, KEY + "Id" + EnumDataCheck.NOT_EXIST.getErrorMsg());
            }
            entity = getEntity(iuVo.getId());
            entity.setCompanyRemark(iuVo.getCompanyRemark());
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    /**
     * Lodging Tips
     * 
     * @param id
     * @param flag
     * @return
     */
    public Boolean isLodgingTips(String id, Boolean flag) {
        repository.isLodgingTips(id, flag);
        return true;
    }

    /*
     * 毎日の工作日程を自動的に登録 Scheduled
     * s m h day month week year
     */
    @Scheduled(cron = "0 30 0 * * ?")
    public Boolean workAutoInsert() {
        try {
            List<UserEntity> userEntities = userService.findByIsAuditAndIsDelete(true, false);
            List<CarEntity> carEntities = carService.findByIsAuditAndIsDelete(true, false);

            List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            ScheduleEntity scheduleEntity = null;
            for (UserEntity userEntity : userEntities) {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity.setTargetId(userEntity.getId());
                scheduleEntity.setTargetNo(userEntity.getUserNo());
                scheduleEntity.setTargetName(userEntity.getUserName());
                scheduleEntity.setActionId("");
                scheduleEntity.setTargetType(EnumTargetType.Driver);
                scheduleEntity.setActionType(null);
                scheduleEntity.setRemark("");
                scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
                scheduleEntity.setWorkTime(null);
                scheduleEntities.add(scheduleEntity);
            }
            for (CarEntity carEntity : carEntities) {
                scheduleEntity = new ScheduleEntity();
                scheduleEntity.setTargetId(carEntity.getId());
                scheduleEntity.setTargetNo(carEntity.getCarNo());
                scheduleEntity.setTargetName(carEntity.getCarName());
                scheduleEntity.setActionId("");
                scheduleEntity.setTargetType(EnumTargetType.Car);
                scheduleEntity.setActionType(null);
                scheduleEntity.setRemark("");
                scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
                scheduleEntity.setWorkTime(null);
                scheduleEntities.add(scheduleEntity);
            }
            scheduleService.saveAll(scheduleEntities);
        } catch (Exception e) {
            throw new BaseException(EnumInsertCheck.ERROR, KEY + ":" +
                    e.getMessage());
        }
        return true;
    }

    /*
     * 出发前3天提醒 Scheduled
     */
    // @Scheduled(cron = "9 0 0 * * ?")
    // public Boolean noticeOn3Days() {
    // try {
    // List<Map> orders = repository.queryBefore3Days();
    // for (Map map : orders) {
    // String id = map.get("id").toString();
    // OrderEntity orderEntity = getEntity(id);
    // String to = map.get("customer_email").toString();
    // if (null != to && !to.isEmpty()) {
    // emailUtils.sendEmail3DayToUser(to, orderEntity);
    // }
    // }
    // } catch (Exception e) {
    // throw new BaseException(EnumMailCheck.SEND_ERROR, KEY + ":" +
    // e.getMessage());
    // }
    // return true;
    // }

    /*
     * 出发前1天提醒 Scheduled
     */
    // @Scheduled(cron = "9 0 0 * * ?")
    // public Boolean noticeOn1Days() {
    // try {
    // List<Map> orders = repository.queryBefore1Days();
    // for (Map map : orders) {
    // String id = map.get("id").toString();
    // OrderEntity orderEntity = getEntity(id);
    // String to = map.get("customer_email").toString();
    // if (null != to && !to.isEmpty()) {
    // emailUtils.sendEmail1DayToUser(to, orderEntity);
    // }
    // }
    // } catch (Exception e) {
    // throw new BaseException(EnumMailCheck.SEND_ERROR, KEY + ":" +
    // e.getMessage());
    // }
    // return true;
    // }

    // #endregion

    // #region pay record

    public PayRecordVO adminPay(IUPayRecordVO iuVo) {
        OrderEntity entity = getEntity(iuVo.getOrderId());
        PayRecordVO payRecordVO = new PayRecordVO();
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            iuVo.setStatus(EnumFinanceStatus.Waiting);
            iuVo.setStatusName(EnumFinanceStatus.Waiting.getMessage());
            payRecordVO = payRecordService.addOrEdit(iuVo);
        } else {
            iuVo.setOrder(entity);
            UserEntity userEntity = userService.getEntity(LoginUtils.getLoginUserId());
            iuVo.setCreateByName(userEntity.getUserName());
            iuVo.setAuditByName(entity.getSellerName());
            iuVo.setStatus(EnumFinanceStatus.Waiting);
            iuVo.setStatusName(EnumFinanceStatus.Waiting.getMessage());
            payRecordVO = payRecordService.addOrEdit(iuVo);
        }

        // 立替表
        addAdvance(entity, iuVo);

        return payRecordVO;
    }

    // 立替表追加
    public void addAdvance(OrderEntity entity, IUPayRecordVO iuVo) {
        try {
            List<AdvanceEntity> advanceEntities = advanceService.getByOrderNo(entity.getOrderNo());
            if (advanceEntities.size() > 0) {
                AdvanceEntity advanceEntity = advanceEntities.get(0);
                String code = iuVo.getPayItemCode();
                if (iuVo.getFinanceType() == EnumFinanceType.InAdvance) {
                    advanceEntity.setInAmount(advanceEntity.getInAmount() + iuVo.getAmount());
                } else if (iuVo.getFinanceType() == EnumFinanceType.OutAdvance) {
                    switch (code) {
                        case "advance_park":
                            advanceEntity.setParkingAmount(advanceEntity.getParkingAmount() + iuVo.getAmount());
                            break;
                        case "advance_meal":
                            advanceEntity.setMealAmount(advanceEntity.getMealAmount() + iuVo.getAmount());
                            break;
                        case "advance_overtime":
                            advanceEntity.setOtherAmount(advanceEntity.getOtherAmount() + iuVo.getAmount());
                            break;
                        case "advance_water":
                            advanceEntity.setWaterAmount(advanceEntity.getWaterAmount() + iuVo.getAmount());
                            break;
                        case "advance_road":
                            advanceEntity.setRoadAmount(advanceEntity.getRoadAmount() + iuVo.getAmount());
                            break;
                        case "advance_ticket":
                            advanceEntity.setTicketAmount(advanceEntity.getTicketAmount() + iuVo.getAmount());
                            break;
                        case "advance_bath":
                            advanceEntity.setBathTaxAmount(advanceEntity.getBathTaxAmount() + iuVo.getAmount());
                            break;
                        case "advance_hotel":
                            advanceEntity.setHotelAmount(advanceEntity.getHotelAmount() + iuVo.getAmount());
                            break;
                        case "advance_other1":
                        case "advance_other2":
                            advanceEntity.setOtherAmount(advanceEntity.getOtherAmount() + iuVo.getAmount());
                            break;
                        case "advance_etc":
                            advanceEntity.setEtcAmount(advanceEntity.getEtcAmount() + iuVo.getAmount());
                            break;
                        default:
                            advanceEntity.setOtherAmount(advanceEntity.getOtherAmount() + iuVo.getAmount());
                            break;
                    }
                } else if (iuVo.getFinanceType() == EnumFinanceType.InEarnings) {
                    advanceEntity.setProfitAmount(advanceEntity.getProfitAmount() + iuVo.getAmount());
                } else if (iuVo.getFinanceType() == EnumFinanceType.InTemp) {
                    advanceEntity.setInAmount(advanceEntity.getInAmount() + iuVo.getAmount());
                }
                advanceEntity.setAdvanceAmount(advanceEntity.getBathTaxAmount() + advanceEntity.getEtcAmount()
                        + advanceEntity.getHotelAmount() + advanceEntity.getMealAmount()
                        + advanceEntity.getParkingAmount()
                        + advanceEntity.getRoadAmount() + advanceEntity.getTicketAmount()
                        + advanceEntity.getWaterAmount()
                        + advanceEntity.getOvertimeAmount() + advanceEntity.getOtherAmount());
                advanceEntity.setBillingAddress(entity.getBillingAddress());
                advanceService.addOrEdit(advanceEntity);
            }
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
    }

    // 利润表追加
    public void addEarnings(OrderEntity entity, IUPayRecordVO iuVo) {
        try {
            List<EarningsEntity> earningsEntities = earningsService.getByOrderNo(entity.getOrderNo());
            if (earningsEntities.size() > 0) {
                EarningsEntity earningsEntity = earningsEntities.get(0);
                String code = iuVo.getPayItemCode();

                if (iuVo.getFinanceType() == EnumFinanceType.OutCost) {
                    switch (code) {
                        case "handlingFee":
                            earningsEntity.setHandlingFee(earningsEntity.getHandlingFee() + iuVo.getAmount());
                            break;
                        case "oilFee":
                            earningsEntity.setOilFee(earningsEntity.getOilFee());
                            break;
                        case "etc":
                            earningsEntity.setEtc(earningsEntity.getEtc() + iuVo.getAmount());
                            break;
                        case "entrustSalary":
                            earningsEntity.setEntrustSalary(earningsEntity.getEntrustSalary() + iuVo.getAmount());
                            break;
                        case "salary":
                            earningsEntity.setSalary(earningsEntity.getSalary() + iuVo.getAmount());
                            break;
                        case "entrust":
                            earningsEntity.setEntrust(earningsEntity.getEntrust() + iuVo.getAmount());
                            break;
                        case "parking":
                            earningsEntity.setParking(earningsEntity.getParking() + iuVo.getAmount());
                            break;
                        default:
                            break;
                    }
                }
                double price = entity.getAmount() <= 0 ? 1 : entity.getAmount();
                earningsEntity
                        .setProfit(
                                entity.getAmount() - (earningsEntity.getHandlingFee() + earningsEntity.getOilFee()
                                        + earningsEntity.getEtc() + earningsEntity.getEntrustSalary()
                                        + earningsEntity.getSalary()
                                        + earningsEntity.getEntrust() + earningsEntity.getParking()));
                earningsEntity.setProfitRate(earningsEntity.getProfit() / price);
                earningsService.addOrEdit(earningsEntity);
            }
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
    }

    // 営業確認済
    public Boolean confirmPay(String payId) {
        UserEntity userEntity = userService.getEntity(LoginUtils.getLoginUserId());
        payRecordService.audit(payId, userEntity.getUserName());
        PayRecordEntity payRecordEntity = payRecordService.getEntity(payId);
        OrderEntity entity = getEntity(payRecordEntity.getOrderId());
        IUPayRecordVO iuVo = PayRecordVOEntityMapStruct.INSTANCE.toIuVO(payRecordEntity);

        if (payRecordEntity.getIsAudit()) {
            // 立替表
            addAdvance(entity, iuVo);
        }
        return true;
    }

    /*
     * 财务確認済
     */
    public Boolean paid(SettlementVO settlementVO) {
        payRecordService.paid(settlementVO.getIds(), settlementVO.getPayMethod(), settlementVO.getPayMethodCode(),
                settlementVO.getBank());
        return true;
    }

    /*
     * 财务現金確認済
     */
    public Boolean cashPaid(SettlementVO settlementVO) {
        payRecordService.cashPaid(settlementVO.getIds());
        return true;
    }

    /*
     * 财务決算済
     */
    public Boolean settlement(SettlementVO settlementVO) {
        payRecordService.settlement(settlementVO.getIds());

        for (String payId : settlementVO.getIds()) {
            PayRecordEntity payRecordEntity = payRecordService.getEntity(payId);
            OrderEntity entity = getEntity(payRecordEntity.getOrderId());
            IUPayRecordVO iuVo = PayRecordVOEntityMapStruct.INSTANCE.toIuVO(payRecordEntity);

            // 利润表
            addEarnings(entity, iuVo);
        }

        return true;
    }

    // #endregion

    // #region deploy

    public DeployDetailsVO getCarDeployDetails(String id) {
        OrderEntity entity = getEntity(id);
        QueryDeployRecordVO queryDeployRecordVO = new QueryDeployRecordVO();
        queryDeployRecordVO.setPageSize(9999);
        queryDeployRecordVO.setOrderId(id);
        queryDeployRecordVO.setTargetType(EnumTargetType.Car);
        ListVO<DeployRecordVO> deployRecordVOs = deployRecordService.getPages(queryDeployRecordVO);
        DeployDetailsVO deployDetailsVOs = new DeployDetailsVO();
        deployDetailsVOs.setId(id);
        deployDetailsVOs.setOrderNo(entity.getOrderNo());
        deployDetailsVOs.setDeployRecordVOs(deployRecordVOs.getList());
        return deployDetailsVOs;
    }

    public DeployDetailsVO getDriverDeployDetails(String id) {
        OrderEntity entity = getEntity(id);
        QueryDeployRecordVO queryDeployRecordVO = new QueryDeployRecordVO();
        queryDeployRecordVO.setPageSize(9999);
        queryDeployRecordVO.setOrderId(id);
        queryDeployRecordVO.setTargetType(EnumTargetType.Driver);
        ListVO<DeployRecordVO> deployRecordVOs = deployRecordService.getPages(queryDeployRecordVO);
        DeployDetailsVO deployDetailsVOs = new DeployDetailsVO();
        deployDetailsVOs.setId(id);
        deployDetailsVOs.setOrderNo(entity.getOrderNo());
        deployDetailsVOs.setDeployRecordVOs(deployRecordVOs.getList());
        return deployDetailsVOs;
    }

    /*
     * ドライバー&車両支配
     */

    public Boolean deploy(OrderDeployVO deployVO) {
        try {
            OrderEntity entity = getEntity(deployVO.getOrderId());
            // ドライバー
            entity.setDriverId(deployVO.getDriverId());
            entity.setDriverName(deployVO.getDriverName());
            entity.setDriverNo(deployVO.getDriverNo());

            // 車両
            entity.setCarId(deployVO.getCarId());
            entity.setCarName(deployVO.getCarName());
            entity.setCarNo(deployVO.getCarNo());
            entity.setCarSeat(deployVO.getCarSeat());
            entity.setCarType(deployVO.getCarType());

            if (null != entity.getCarId() && !entity.getCarId().isEmpty()) {
                // 車両
                deployVO.setOrderId(entity.getId());
                deployVO.setOrderNo(entity.getOrderNo());
                deployVO.setCarId(entity.getCarId());
                deployVO.setCarName(entity.getCarName());
                deployVO.setCarNo(entity.getCarNo());
                deployVO.setCarSeat(entity.getCarSeat());
                deployVO.setCarType(entity.getCarType());
                carDeploy(deployVO, entity);
            }
            if (null != entity.getDriverId()
                    && !entity.getDriverId().isEmpty()) {
                // 司机
                deployVO.setDriverId(entity.getDriverId());
                deployVO.setDriverName(entity.getDriverName());
                deployVO.setDriverNo(entity.getDriverNo());
                driverDeploy(deployVO, entity);
            }

            if (null != entity.getCarId() && !entity.getCarId().isEmpty()
                    && null != entity.getDriverId()
                    && !entity.getDriverId().isEmpty()) {
                entity.setOrderStatus(EnumOrderStatus.Sending);
                entity.setOrderStatusName(EnumOrderStatus.Sending.getMessage());
            } else {
                entity.setOrderStatus(EnumOrderStatus.Assigning);
                entity.setOrderStatusName(EnumOrderStatus.Assigning.getMessage());
            }
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean driverDeploy(OrderDeployVO deployVO, OrderEntity entity) {

        // ドライバー支配履歴
        DeployRecordEntity deployRecordEntity = new DeployRecordEntity();
        deployRecordEntity.setOrderId(deployVO.getOrderId());
        deployRecordEntity.setOrderNo(deployVO.getOrderNo());
        deployRecordEntity.setNewId(deployVO.getDriverId());
        deployRecordEntity.setNewNo(deployVO.getDriverNo());
        deployRecordEntity.setNewName(deployVO.getDriverName());
        deployRecordEntity.setTargetType(EnumTargetType.Driver);
        deployRecordEntity.setRemark(deployVO.getDriverRemark());
        deployRecordService.insertOrUpdate(deployRecordEntity);

        // 先删除日程表
        scheduleService.deletePhysicsByOrderId(deployVO.getOrderId(), EnumTargetType.Driver);

        // 注文 ドライバー スケジュール
        List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Date d = entity.getStartTime();
        Calendar calendar = Calendar.getInstance();
        ScheduleEntity scheduleEntity = null;
        int index = 0;
        do {
            scheduleEntity = new ScheduleEntity();
            scheduleEntity.setTargetId(deployVO.getDriverId());
            scheduleEntity.setTargetNo(deployVO.getDriverNo());
            scheduleEntity.setTargetName(deployVO.getDriverName());
            scheduleEntity.setActionId(entity.getId());
            scheduleEntity.setTargetType(EnumTargetType.Driver);
            scheduleEntity.setActionType(EnumActionType.Order);
            scheduleEntity.setRemark(String.format("%S %S",
                    entity.getOrderType().getMessage(), entity.getOrderSource()));
            calendar.setTime(d);
            calendar.add(Calendar.DATE, index);
            scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
            scheduleEntity.setWorkTime(calendar.getTime());
            scheduleEntities.add(scheduleEntity);
            index++;
        } while (index < entity.getOrderDays());
        scheduleService.saveAll(scheduleEntities);

        // 如果服务时间是当天，改变司机状态
        calendar.setTime(entity.getStartTime());
        Date today = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(today);
        if (todayCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            userService.resetStatus(deployVO.getDriverId(), EnumUserStatus.Working);
        }
        return true;
    }

    public Boolean carDeploy(OrderDeployVO deployVO, OrderEntity entity) {

        // 車両支配履歴
        DeployRecordEntity deployRecordEntity = new DeployRecordEntity();
        deployRecordEntity = new DeployRecordEntity();
        deployRecordEntity.setOrderId(deployVO.getOrderId());
        deployRecordEntity.setOrderNo(deployVO.getOrderNo());
        deployRecordEntity.setNewId(deployVO.getCarId());
        deployRecordEntity.setNewNo(deployVO.getCarNo());
        deployRecordEntity.setNewName(deployVO.getCarNo());
        deployRecordEntity.setTargetType(EnumTargetType.Car);
        deployRecordEntity.setRemark(deployVO.getCarRemark());
        deployRecordService.insertOrUpdate(deployRecordEntity);

        // 先删除日程表
        scheduleService.deletePhysicsByOrderId(deployVO.getOrderId(), EnumTargetType.Car);

        // 注文 車両 スケジュール
        List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Date d = entity.getStartTime();
        Calendar calendar = Calendar.getInstance();
        ScheduleEntity scheduleEntity = null;
        int index = 0;
        do {
            scheduleEntity = new ScheduleEntity();
            scheduleEntity.setTargetId(deployVO.getCarId());
            scheduleEntity.setTargetNo(deployVO.getCarNo());
            scheduleEntity.setTargetName(deployVO.getCarNo());
            scheduleEntity.setActionId(entity.getId());
            scheduleEntity.setTargetType(EnumTargetType.Car);
            scheduleEntity.setActionType(EnumActionType.Order);
            scheduleEntity
                    .setRemark(String.format("%S %S", entity.getOrderType().getMessage(), entity.getOrderSource()));
            calendar.setTime(d);
            calendar.add(Calendar.DATE, index);
            scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
            scheduleEntity.setWorkTime(calendar.getTime());
            scheduleEntities.add(scheduleEntity);
            index++;
        } while (index < entity.getOrderDays());
        scheduleService.saveAll(scheduleEntities);

        // 如果服务时间是当天，改变车辆状态
        calendar.setTime(entity.getStartTime());
        Date today = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(today);
        if (todayCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            carService.resetStatus(deployVO.getCarId(), EnumCarStatus.Working);
        }
        return true;
    }

    public Boolean send() {
        repository.send();
        return true;
    }

    // #endregion

}
