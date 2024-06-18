package com.cbs.oukasystem.service.finance;

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
import org.springframework.data.domain.Sort.Direction;
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
import com.cbs.oukasystem.entity.finance.PayRecordEntity;
import com.cbs.oukasystem.mapstruct.finance.PayRecordVOEntityMapStruct;
import com.cbs.oukasystem.repository.finance.PayRecordRepository;
import com.cbs.oukasystem.service.base.DictItemService;
import com.cbs.oukasystem.service.common.CommonService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.QueryDictItemVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.finance.QueryPayRecordVO;
import com.cbs.oukasystem.vo.out.base.DictItemVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;

import lombok.NonNull;

@Service
@Transactional
public class PayRecordService {

    String KEY = "料金記録履歴";

    @Autowired
    private PayRecordRepository repository;

    @Autowired
    private DictItemService itemService;
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

                if (null != qVo.getFinanceType() && qVo.getFinanceType().length > 0) {
                    Predicate[] type = new Predicate[qVo.getFinanceType().length];
                    int index = 0;
                    for (EnumFinanceType financeType : qVo.getFinanceType()) {
                        type[index] = cb.equal(root.get("financeType"), financeType);
                        index++;
                    }
                    predicate.add(cb.or(type));
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
                { "タイプ", "financeType" },
                { "ツアー日", "startTime" },
                { "責任人", "sellerName" },
                { "团号", "orderNo" },
                { "ツアー内容", "orderType" },
                { "ドライバー", "driverName" },
                { "車両", "carNo" },
                { "通貨", "currency" },
                { "金額", "currencyAmount" },
                { "日元金额", "expectedAmount" },
                { "已收金额", "amount" },
                { "支払方法", "payMethod" },
                { "金融機関", "bank" },
                { "備考", "remark" },
                { "Test", "orderVO.driverName" },
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
        return PayRecordVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public PayRecordEntity insertOrUpdate(IUPayRecordVO iuVo) {
        PayRecordEntity entity = new PayRecordEntity();
        try {
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setPayItem(iuVo.getPayItem());
                entity.setPayItemCode(iuVo.getPayItemCode());
                entity.setFinanceType(iuVo.getFinanceType());
                entity.setAmount(iuVo.getAmount());
                entity.setCurrency(iuVo.getCurrency());
                entity.setCurrencyCode(iuVo.getCurrencyCode());
                entity.setCurrencyAmount(iuVo.getCurrencyAmount());
                entity.setRemark(iuVo.getRemark());
            } else {
                entity = PayRecordVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }
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
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * 決算
     */
    public Boolean settlement(List<String> ids, String payMethod, String payMethodCode, String bank) {
        repository.settlementByIds(ids, payMethod, payMethodCode, bank);
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
