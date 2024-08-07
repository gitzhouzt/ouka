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

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.PayItemEntity;
import com.cbs.oukasystem.mapstruct.order.PayItemVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.PayItemRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUPayItemVO;
import com.cbs.oukasystem.vo.in.order.QueryPayItemVO;
import com.cbs.oukasystem.vo.out.order.PayItemVO;

import lombok.NonNull;

@Service
@Transactional
public class PayItemService {

    String KEY = "收现项目";

    @Autowired
    private PayItemRepository repository;

    /*
     * idによって收现项目を得る
     */

    public PayItemVO getById(@NonNull String id) {
        return PayItemVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /*
     * 收现项目を得る
     */
    public PayItemEntity getEntity(@NonNull String id) {
        Optional<PayItemEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての收现项目
     */

    public List<PayItemVO> getAll() {
        return PayItemVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * 收现项目のリスト
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
     * 收现项目検索
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
