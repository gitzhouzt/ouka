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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.CashItemEntity;
import com.cbs.oukasystem.entity.order.PayRecordEntity;
import com.cbs.oukasystem.mapstruct.order.CashItemVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.PayRecordVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.PayRecordRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryCashItemVO;
import com.cbs.oukasystem.vo.in.order.QueryPayRecordVO;
import com.cbs.oukasystem.vo.out.finance.SettlementVO;
import com.cbs.oukasystem.vo.out.order.PayRecordVO;

import lombok.NonNull;

@Service
@Transactional
public class PayRecordService {

    String KEY = "料金記録履歴";

    @Autowired
    private PayRecordRepository repository;

    /*
     * idによって料金記録履歴を得る
     */

    public PayRecordVO getById(String id) {
        return PayRecordVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /*
     * 料金記録履歴を得る
     */
    public PayRecordEntity getEntity(@NonNull String id) {
        Optional<PayRecordEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての料金記録履歴
     */

    public List<PayRecordVO> getAll() {
        return PayRecordVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * 料金記録履歴のリスト
     */

    public ListVO<PayRecordVO> getPages(QueryPayRecordVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<PayRecordEntity> specification = Search(qVo);
        Page<PayRecordEntity> pages = repository.findAll(specification, pageable);
        List<PayRecordVO> list = PayRecordVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<PayRecordVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public List<PayRecordVO> getByOrderId(String orderId, EnumFinanceType financeType) {
        return PayRecordVOEntityMapStruct.INSTANCE
                .toVOs(repository.findAllByOrderIdAndFinanceType(orderId, financeType));
    }

    /*
     * 料金記録履歴検索
     */
    public Specification<PayRecordEntity> Search(QueryPayRecordVO qVo) {
        return new Specification<PayRecordEntity>() {

            public Predicate toPredicate(Root<PayRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                // keyword
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preRefundTo = cb.like(root.get("refundTo"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preOrderNo = cb.like(root.get("orderNo"),
                            "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preOrderNo, preRefundTo));
                }
                if (null != qVo.getOrderId() && !qVo.getOrderId().isEmpty()) {
                    predicate.add(cb.equal(root.get("orderId"), qVo.getOrderId()));
                }
                if (null != qVo.getPayMethod() && !qVo.getPayMethod().isEmpty()) {
                    predicate.add(cb.equal(root.get("payMethod"), qVo.getPayMethod()));
                }
                if (null != qVo.getPayItem() && !qVo.getPayItem().isEmpty()) {
                    predicate.add(cb.equal(root.get("payItem"), qVo.getPayItem()));
                }
                if (null != qVo.getFinanceType()) {
                    predicate.add(cb.equal(root.get("financeType"), qVo.getFinanceType()));
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
    public void export(QueryPayRecordVO qVo) {
        String screenName = "料金記録";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "ツアー日", "startTime" },
                { "責任人", "sellerName" },
                { "团号", "orderNo" },
                { "ツアー内容", "orderType" },
                { "ドライバー", "driverName" },
                { "車両", "carNo" },
                { "通貨", "currency" },
                { "金額", "currencyAmount" },
                { "日元金额", "amount" },
                { "備考", "remark" },
                { "返金額", "refundAmount" },
                { "返金相手", "refundTo" },
                { "返金日付", "refundTime" },
        };

        Specification<PayRecordEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<PayRecordEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                PayRecordVOEntityMapStruct.INSTANCE.toVOs(list));
    }

    /*
     * 追加 or 編集
     */

    public PayRecordVO addOrEdit(IUPayRecordVO iuVo) {
        PayRecordEntity entity = PayRecordVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return PayRecordVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(entity));
    }

    public PayRecordEntity insertOrUpdate(@NonNull PayRecordEntity newEntity) {
        PayRecordEntity entity = new PayRecordEntity();
        try {
            if (null != newEntity.getId() && !newEntity.getId().isEmpty()) {
                entity = getEntity(newEntity.getId());

                entity.setPayItem(newEntity.getPayItem());
                entity.setPayItemCode(newEntity.getPayItemCode());
                entity.setFinanceType(newEntity.getFinanceType());
                entity.setAmount(newEntity.getAmount());
                entity.setCurrency(newEntity.getCurrency());
                entity.setCurrencyCode(newEntity.getCurrencyCode());
                entity.setCurrencyAmount(newEntity.getCurrencyAmount());
                entity.setRemark(newEntity.getRemark());

            }
            if (entity.getCurrencyCode().equals("jpy")) {
                entity.setCurrencyAmount(entity.getAmount());
            }
            entity = repository.save(newEntity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    public Boolean settlement(SettlementVO settlementVO) {

        repository.settlementByIds(settlementVO.getIds(), settlementVO.getPayMethod(), settlementVO.getPayMethodCode(),
                settlementVO.getBank());
        return true;
    }

    public Boolean updateStatusByIds(List<String> ids) {
        repository.updateStatusByIds(ids);
        return true;
    }

    /*
     * ステータス の 変更
     */
    public Boolean resetStatus(String id, EnumStatus status) {
        PayRecordEntity entity = getEntity(id);
        entity.setStatus(status);
        repository.save(entity);
        return true;
    }

    /*
     * 削除
     */

    public Boolean delete(String id) {
        try {
            PayRecordEntity entity = getEntity(id);
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

    public Boolean audit(String id, String auditByName) {
        try {
            PayRecordEntity entity = getEntity(id);
            entity.setAuditByName(auditByName);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
