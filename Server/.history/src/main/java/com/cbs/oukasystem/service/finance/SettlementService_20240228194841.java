package com.cbs.oukasystem.service.finance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.finance.SettlementEntity;
import com.cbs.oukasystem.mapstruct.finance.SettlementVOEntityMapStruct;
import com.cbs.oukasystem.repository.finance.SettlementRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.finance.IUAdvanceVO;
import com.cbs.oukasystem.vo.in.finance.QuerySettlementVO;
import com.cbs.oukasystem.vo.out.finance.AdvanceVO;

@Service
@Transactional
public class SettlementService {

    String KEY = "事故";

    @Autowired
    private SettlementRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    // region Settlement

    // region 検索

    public AdvanceVO getById(String id) {
        return SettlementVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public SettlementEntity getEntity(String id) {
        Optional<SettlementEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<AdvanceVO> getAll() {
        List<SettlementEntity> entities = repository.findAll();
        List<AdvanceVO> vos = SettlementVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<SettlementEntity> findAll(Specification<SettlementEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<AdvanceVO> getPages(QuerySettlementVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<SettlementEntity> specification = Search(qVo);
        Page<SettlementEntity> pages = repository.findAll(specification, pageable);
        List<AdvanceVO> list = SettlementVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<AdvanceVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<SettlementEntity> Search(QuerySettlementVO qVo) {
        return new Specification<SettlementEntity>() {

            public Predicate toPredicate(Root<SettlementEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preOrderNo = cb.like(root.get("orderNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preFinanceName = cb.like(root.get("financeByName"), "%" + qVo.getKeyword() + "%");
                    Predicate preRemark = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preOrderNo, preFinanceName, preRemark));
                }
                if (null != qVo.getOrderId() && !qVo.getOrderId().isEmpty()) {
                    predicate.add(cb.equal(root.get("orderId"), qVo.getOrderId()));
                }
                if (null != qVo.getStatus()) {
                    predicate.add(cb.equal(root.get("status"), qVo.getStatus()));
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
                predicate.add(cb.equal(root.get("isAudit"), true));
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    // endregion

    // region 追加、更新
    public AdvanceVO addOrEdit(IUAdvanceVO iuVo) {
        return SettlementVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public SettlementEntity insertOrUpdate(SettlementEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public SettlementEntity insertOrUpdate(IUAdvanceVO iuVo) {
        SettlementEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setAmount1(iuVo.getAmount1());
                entity.setAmount2(iuVo.getAmount2());
                entity.setAmount3(iuVo.getAmount3());
                entity.setAmount4(iuVo.getAmount4());
                entity.setAmount5(iuVo.getAmount5());
                entity.setAmount6(iuVo.getAmount6());
                entity.setAmount7(iuVo.getAmount7());
                entity.setAmount8(iuVo.getAmount8());
                entity.setAmount9(iuVo.getAmount9());
                entity.setAmount99(iuVo.getAmount99());

                entity.setBillingAddress(iuVo.getBillingAddress());
                entity.setFinanceByName(iuVo.getFinanceByName());
                entity.setStatus(iuVo.getStatus());
                entity.setRemark(iuVo.getRemark());

            } else {
                // 追加
                entity = SettlementVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }

            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    // public Boolean settlement(String id) {

    // }

    public Boolean audit(String id) {
        try {
            SettlementEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    // endregion

    // region 削除
    public Boolean delete(String id) {
        try {
            SettlementEntity entity = getEntity(id);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysics(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    // endregion

}
