package com.cbs.oukasystem.service.order;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.EmailUtils;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumOrderCheck;
import com.cbs.oukasystem.common.PdfUtils;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.entity.order.DeployRecordEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.order.PayRecordEntity;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.operate.ScheduleService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUOrderVO;
import com.cbs.oukasystem.vo.in.order.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.OrderCancelVO;
import com.cbs.oukasystem.vo.in.order.OrderDeployVO;
import com.cbs.oukasystem.vo.in.order.QueryDeployRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.order.QueryPayRecordVO;
import com.cbs.oukasystem.vo.out.order.DeployDetailsVO;
import com.cbs.oukasystem.vo.out.order.DeployRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;
import com.cbs.oukasystem.vo.out.order.PayDetailsVO;
import com.cbs.oukasystem.vo.out.order.PayRecordVO;
import com.itextpdf.text.DocumentException;

@Service
@Transactional
public class OrderService {

    String KEY = "注文";

    @Autowired
    private OrderRepository repository;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private DeployRecordService deployRecordService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private EmailUtils emailUtils;

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

    public PayDetailsVO getPayDetails(String id) {
        OrderEntity entity = getEntity(id);
        QueryPayRecordVO queryPayRecordVO = new QueryPayRecordVO();
        queryPayRecordVO.setPageSize(9999);
        queryPayRecordVO.setOrderId(id);
        ListVO<PayRecordVO> payRecordVOs = payRecordService.getPages(queryPayRecordVO);
        List<PayRecordVO> pList = payRecordVOs.getList();
        PayDetailsVO payDetailsVO = new PayDetailsVO();
        payDetailsVO.setId(id);
        payDetailsVO.setOrderNo(entity.getOrderNo());
        payDetailsVO.setOrderPrice(entity.getOrderPrice());
        int depositAmount = 0;
        int expenditureAmount = 0;
        int costAmount = 0;
        for (PayRecordVO payRecordVO : pList) {
            Double amount = payRecordVO.getAmount() == null ? 0 : payRecordVO.getAmount();
            if (payRecordVO.getFinanceType() == EnumFinanceType.Deposit) {
                depositAmount += amount;
            } else if (payRecordVO.getFinanceType() == EnumFinanceType.Expenditure) {
                expenditureAmount += amount;
            } else if (payRecordVO.getFinanceType() == EnumFinanceType.Cost) {
                costAmount += amount;
            }
        }
        payDetailsVO.setDepositAmount(depositAmount);
        payDetailsVO.setExpenditureAmount(expenditureAmount);
        payDetailsVO.setCostAmount(costAmount);
        payDetailsVO.setPayRecordVOs(pList);
        return payDetailsVO;
    }

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
     * 全ての注文
     */

    public List<OrderVO> getAll() {
        return OrderVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * 注文のリスト
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

    public void export(QueryOrderVO qVo) {
        String screenName = "配車注文";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "注文番号", "orderNo" },
                { "責任人", "sellerName" },
                { "订单来源", "orderSource" },
                { "第三方", "orderKey" },
                { "サービス地域", "city" },
                { "タイプ", "orderType" },
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
                { "ステータス", "orderStatus" }
        };

        Specification<OrderEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<OrderEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                OrderVOEntityMapStruct.INSTANCE.toVOs(list));
    }

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

    /*
     * 注文検索
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
                    Predicate preContent1 = cb.like(root.get("contactContent1"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent2 = cb.like(root.get("contactContent2"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preContent3 = cb.like(root.get("contactContent3"),
                            "%" + qVo.getKeyword() + "%");
                    listAnd.add(
                            cb.or(preOrderNo, preCustomerName,
                                    preOrderKey, preContent1, preContent2, preContent3));
                }
                if (null != qVo.getOrderSource() && !qVo.getOrderSource().isEmpty()) {
                    listAnd.add(cb.equal(root.get("orderSource"), qVo.getOrderSource()));
                }
                if (null != qVo.getCarName() && !qVo.getCarName().isEmpty()) {
                    listAnd.add(cb.equal(root.get("carName"), qVo.getCarName()));
                }
                if (null != qVo.getDriverName() && !qVo.getDriverName().isEmpty()) {
                    listAnd.add(cb.equal(root.get("driverName"), qVo.getDriverName()));
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

                Calendar calendar = Calendar.getInstance();
                if (null != qVo.getBeginTime() && null != qVo.getEndTime()) {
                    calendar.setTime(qVo.getBeginTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date startTime = calendar.getTime();
                    Predicate preBeginTime = cb.greaterThanOrEqualTo(root.get("createTime"), startTime);
                    calendar.setTime(qVo.getEndTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    Date endTime = calendar.getTime();
                    Predicate preEndTime = cb.lessThanOrEqualTo(root.get("createTime"), endTime);
                    listAnd.add(cb.and(preBeginTime, preEndTime));
                }
                listAnd.add(cb.equal(root.get("isAudit"), true));
                listAnd.add(cb.notEqual(root.get("isDelete"), true));

                List<Predicate> listOr = new ArrayList<>();
                if (null != qVo.getStartBeginTime() && null != qVo.getStartEndTime()) {
                    calendar.setTime(qVo.getStartBeginTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date startTime = calendar.getTime();
                    Predicate preBeginTime = cb.greaterThanOrEqualTo(root.get("startTime"), startTime);
                    calendar.setTime(qVo.getStartEndTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    Date endTime = calendar.getTime();
                    Predicate preEndTime = cb.lessThanOrEqualTo(root.get("startTime"), endTime);

                    // 此处必须加上记入中的，不然类似单子查不出来了
                    listOr.add(cb.or(cb.isNull(root.get("startTime")), cb.and(preBeginTime, preEndTime)));
                }

                Predicate[] arrayAnd = new Predicate[listAnd.size()];
                Predicate PreAnd = cb.and(listAnd.toArray(arrayAnd));
                Predicate[] arrayOr = new Predicate[listOr.size()];
                Predicate PreOr = cb.or(listOr.toArray(arrayOr));
                return query.where(PreAnd, PreOr).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

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
                entity.setSellerPhoto(iuVo.getSellerPhoto());

            } else {
                // 追加
                entity = OrderVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                if (null == entity.getOrderNo() || entity.getOrderNo().isEmpty()
                        || entity.getOrderNo().equals(entity.getOrderSourceCode())) {
                    String orderNo = CommonUtils.getNewDateEquipmentNo(entity.getOrderSourceCode(),
                            repository.count());
                    entity.setOrderNo(orderNo);
                }
                entity.setOrderStatus(EnumOrderStatus.Filling);
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
                entity.setSellerPhoto(iuVo.getSellerPhoto());

            } else {
                // 追加
                entity = OrderVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                if (null == entity.getOrderNo() || entity.getOrderNo().isEmpty()
                        || entity.getOrderNo().equals(entity.getOrderSourceCode())) {
                    String orderNo = CommonUtils.getNewDateEquipmentNo(entity.getOrderSourceCode(),
                            repository.count());
                    entity.setOrderNo(orderNo);
                }
                entity.setOrderStatus(EnumOrderStatus.Filling);
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
            entity.setOrderType(iuVo.getOrderType());
            entity.setOrderDays(iuVo.getOrderDays());
            entity.setCustomerRemark(iuVo.getCustomerRemark());
            entity.setOrderPrice(iuVo.getOrderPrice());
            entity.setOrderFrom(iuVo.getOrderFrom());
            entity.setOrderTo(iuVo.getOrderTo());
            entity.setOrderFromDetails(iuVo.getOrderFromDetails());
            entity.setOrderToDetails(iuVo.getOrderToDetails());
            entity.setFlightNo(iuVo.getFlightNo());
            entity.setAirport(iuVo.getAirport());

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
            entity.setCompanyRemark(iuVo.getCompanyRemark());

            // ドライバー
            entity.setDriverId(iuVo.getDriverId());
            entity.setDriverName(iuVo.getDriverName());
            entity.setDriverNo(iuVo.getDriverNo());
            entity.setDriverPhoto(iuVo.getDriverPhoto());

            // 車両
            entity.setCarId(iuVo.getCarId());
            entity.setCarName(iuVo.getCarName());
            entity.setCarNo(iuVo.getCarNo());
            entity.setCarSeat(iuVo.getCarSeat());
            entity.setCarType(iuVo.getCarType());

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
                deployVO.setDriverPhoto(entity.getDriverPhoto());
                driverDeploy(deployVO, entity);
            }
            if (null != entity.getCarId() && !entity.getCarId().isEmpty()
                    && null != entity.getDriverId()
                    && !entity.getDriverId().isEmpty()) {
                entity.setOrderStatus(EnumOrderStatus.Sending);
            } else {
                entity.setOrderStatus(EnumOrderStatus.Assigning);
            }
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

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
            entity.setDriverPhoto(deployVO.getDriverPhoto());

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
                deployVO.setDriverPhoto(entity.getDriverPhoto());
                driverDeploy(deployVO, entity);
            }

            if (null != entity.getCarId() && !entity.getCarId().isEmpty()
                    && null != entity.getDriverId()
                    && !entity.getDriverId().isEmpty()) {
                entity.setOrderStatus(EnumOrderStatus.Sending);
            } else {
                entity.setOrderStatus(EnumOrderStatus.Assigning);
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

        // 改变司机状态
        userService.resetStatus(deployVO.getDriverId(), EnumUserStatus.Working);
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
        deployRecordEntity.setNewName(deployVO.getCarName());
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
            scheduleEntity.setTargetName(deployVO.getCarName());
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

        // 改变车辆状态
        carService.resetStatus(deployVO.getCarId(), EnumCarStatus.Working);
        return true;
    }

    // 金額変更 履歴
    public Boolean pay(IUPayRecordVO iVo) {
        PayRecordEntity payRecordEntity = new PayRecordEntity();
        payRecordEntity.setOrderId(iVo.getOrderId());
        payRecordEntity.setOrderNo(iVo.getOrderNo());
        payRecordEntity.setPayNo(iVo.getPayNo());
        payRecordEntity.setPayMethod(iVo.getPayMethod());
        payRecordEntity.setFinanceType(iVo.getFinanceType());
        payRecordEntity.setCurrency(iVo.getCurrency());
        payRecordEntity.setAmount(iVo.getAmount());
        payRecordEntity.setRemark(iVo.getRemark());
        payRecordService.addOrEdit(iVo);
        return true;
    }

    public Boolean cancelByUser(OrderCancelVO vo) {
        OrderEntity entity = getEntity(vo.getId());
        if (entity.getOrderStatus() == EnumOrderStatus.Filling
                || entity.getOrderStatus() == EnumOrderStatus.Assigning
                || entity.getOrderStatus() == EnumOrderStatus.Sending
                || entity.getOrderStatus() == EnumOrderStatus.Check
                || entity.getOrderStatus() == EnumOrderStatus.Booked) {
            // 受付待ちと予約済みの場合
            entity.setOrderStatus(EnumOrderStatus.Cancelled);

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

            // 改变车辆状态
            carService.resetStatus(entity.getCarId(), EnumCarStatus.Working);
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

            // 改变司机状态
            userService.resetStatus(deployVO.getDriverId(), EnumUserStatus.Working);
        }
        if (null != entity.getCarId() && !entity.getCarId().isEmpty()
                && null != entity.getDriverId()
                && !entity.getDriverId().isEmpty()) {
            entity.setOrderStatus(EnumOrderStatus.Sending);
        } else {
            entity.setOrderStatus(EnumOrderStatus.Assigning);
        }
        repository.save(entity);
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

    /*
     * 削除
     */
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
    public Boolean deletePhysics(String id) {
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
}
