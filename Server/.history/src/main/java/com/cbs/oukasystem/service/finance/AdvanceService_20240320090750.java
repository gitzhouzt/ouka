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
import com.cbs.oukasystem.entity.finance.AdvanceEntity;
import com.cbs.oukasystem.entity.finance.PayRecordEntity;
import com.cbs.oukasystem.mapstruct.finance.AdvanceVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.finance.PayRecordVOEntityMapStruct;
import com.cbs.oukasystem.repository.finance.AdvanceRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.QueryDictItemVO;
import com.cbs.oukasystem.vo.in.finance.IUAdvanceVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.finance.QueryAdvanceVO;
import com.cbs.oukasystem.vo.out.base.DictItemVO;
import com.cbs.oukasystem.vo.out.finance.AdvanceVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;

@Service
@Transactional
public class AdvanceService {

    String KEY = "営業額";

    @Autowired
    private AdvanceRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    // region Advance

    // region 検索

    public AdvanceVO getById(String id) {
        return AdvanceVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public AdvanceEntity getEntity(String id) {
        Optional<AdvanceEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<AdvanceEntity> getByOrderNo(String orderNo) {
        return repository.findByOrderNo(orderNo);
    }

    public List<AdvanceVO> getAll() {
        List<AdvanceEntity> entities = repository.findAll();
        List<AdvanceVO> vos = AdvanceVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<AdvanceEntity> findAll(Specification<AdvanceEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<AdvanceVO> getPages(QueryAdvanceVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<AdvanceEntity> specification = Search(qVo);
        Page<AdvanceEntity> pages = repository.findAll(specification, pageable);
        List<AdvanceVO> list = AdvanceVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<AdvanceVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<AdvanceEntity> Search(QueryAdvanceVO qVo) {
        return new Specification<AdvanceEntity>() {

            public Predicate toPredicate(Root<AdvanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

    // region 初始化立替精算表

    public AdvanceVO addOrEdit(IUAdvanceVO iuVo) {
        return AdvanceVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public AdvanceEntity insertOrUpdate(IUAdvanceVO iuVo) {
        AdvanceEntity entity = new AdvanceEntity();
        try {
            entity = AdvanceVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    public Boolean audit(String id) {
        try {
            AdvanceEntity entity = getEntity(id);
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
            AdvanceEntity entity = getEntity(id);
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
