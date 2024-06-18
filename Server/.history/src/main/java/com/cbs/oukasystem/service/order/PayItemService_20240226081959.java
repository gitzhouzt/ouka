package com.cbs.oukasystem.service.order;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.order.PayItemEntity;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.PayItemVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.PayItemRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUPayItemVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.order.QueryPayItemVO;
import com.cbs.oukasystem.vo.out.order.PayItemVO;

import lombok.NonNull;

@Service
@Transactional
public class PayItemService {

    String KEY = "收现项目";

    @Autowired
    private PayItemRepository repository;

    // #region Query And Export 查询与导出

    public PayItemVO getById(@NonNull @NotEmpty String id) {
        return PayItemVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public PayItemEntity getEntity(@NonNull @NotEmpty String id) {
        Optional<PayItemEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * list
     */
    public List<PayItemVO> getAll() {
        return PayItemVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * pages
     */
    public ListVO<PayItemVO> getPages(QueryPayItemVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<PayItemEntity> specification = Search(qVo);
        Page<PayItemEntity> pages = repository.findAll(specification, pageable);
        List<PayItemVO> list = PayItemVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<PayItemVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public List<PayItemVO> getByOrderId(@NonNull @NotEmpty String orderId) {
        return PayItemVOEntityMapStruct.INSTANCE
                .toVOs(repository.findAllByOrderId(orderId));
    }

    /*
     * 検索
     */
    public Specification<PayItemEntity> Search(QueryPayItemVO qVo) {
        return new Specification<PayItemEntity>() {

            public Predicate toPredicate(Root<PayItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                // keyword
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate prePayItem = cb.like(root.get("payItem"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preOrderNo = cb.like(root.get("orderNo"),
                            "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(prePayItem, preOrderNo));
                }
                if (null != qVo.getOrderId() && !qVo.getOrderId().isEmpty()) {
                    predicate.add(cb.equal(root.get("orderId"), qVo.getOrderId()));
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
                    predicate.add(cb.and(preBeginTime, preEndTime));
                }
                // predicate.add(cb.equal(root.get("isAudit"), true));
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /**
     * export
     * 
     * @param qVo QueryOrderVO
     */
    public void export(QueryPayItemVO qVo) {
        String screenName = "料金記録";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "ツアー日", "startTime" },
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

    // #endregion

    /*
     * 追加 or 編集
     */

    public PayItemVO addOrEdit(IUPayItemVO iuVo) {
        PayItemEntity entity = PayItemVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return PayItemVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(entity));
    }

    public PayItemEntity insertOrUpdate(PayItemEntity entity) {
        if (null != entity) {
            return repository.save(entity);
        } else {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
    }

    /*
     * 削除
     */

    public Boolean delete(@NonNull String id) {
        try {
            PayItemEntity entity = getEntity(id);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
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

    public Boolean audit(@NonNull String id) {
        try {
            PayItemEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
