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
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.order.DeployRecordEntity;
import com.cbs.oukasystem.mapstruct.order.DeployRecordVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.DeployRecordRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUDeployRecordVO;
import com.cbs.oukasystem.vo.in.order.QueryDeployRecordVO;
import com.cbs.oukasystem.vo.out.order.DeployRecordVO;

@Service
@Transactional
public class DeployRecordService {

    String KEY = "分配変更履歴";

    @Autowired
    private DeployRecordRepository repository;

    /*
     * idによって分配変更履歴を得る
     */

    public DeployRecordVO getById(String id) {
        return DeployRecordVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /*
     * 分配変更履歴を得る
     */
    public DeployRecordEntity getEntity(String id) {
        Optional<DeployRecordEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての分配変更履歴
     */

    public List<DeployRecordVO> getAll() {
        return DeployRecordVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * 分配変更履歴のリスト
     */

    public ListVO<DeployRecordVO> getPages(QueryDeployRecordVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<DeployRecordEntity> specification = Search(qVo);
        Page<DeployRecordEntity> pages = repository.findAll(specification, pageable);
        List<DeployRecordVO> list = DeployRecordVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<DeployRecordVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 分配変更履歴検索
     */
    public Specification<DeployRecordEntity> Search(QueryDeployRecordVO qVo) {
        return new Specification<DeployRecordEntity>() {

            public Predicate toPredicate(Root<DeployRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                // keyword
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preOrderNo = cb.like(root.get("orderNo"),
                            "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preOrderNo));
                }
                if (null != qVo.getOrderId() && !qVo.getOrderId().isEmpty()) {
                    predicate.add(cb.equal(root.get("orderId"), qVo.getOrderId()));
                }
                if (null != qVo.getTargetType()) {
                    predicate.add(cb.equal(root.get("targetType"), qVo.getTargetType()));
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

    /*
     * 追加 or 編集
     */

    public DeployRecordVO addOrEdit(IUDeployRecordVO iuVo) {
        DeployRecordEntity entity = DeployRecordVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return DeployRecordVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(entity));
    }

    public DeployRecordEntity insertOrUpdate(DeployRecordEntity entity) {
        if (null != entity) {
            return repository.save(entity);
        } else {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
    }

    /*
     * 削除
     */

    public Boolean delete(String id) {
        try {
            DeployRecordEntity entity = getEntity(id);
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
            DeployRecordEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
