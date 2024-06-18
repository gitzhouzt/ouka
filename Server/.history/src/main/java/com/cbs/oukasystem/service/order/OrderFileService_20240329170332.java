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
import com.cbs.oukasystem.entity.order.OrderFileEntity;
import com.cbs.oukasystem.mapstruct.order.OrderFileVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.OrderGoodsVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderFileRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUOrderFileVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderFileVO;
import com.cbs.oukasystem.vo.out.order.OrderFileVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;

import lombok.NonNull;

@Service
@Transactional
public class OrderFileService {

    String KEY = "ファイル";

    @Autowired
    private OrderFileRepository repository;

    /*
     * idによってファイルを得る
     */

    public OrderFileVO getById(String id) {
        return OrderFileVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /*
     * ファイルを得る
     */
    public OrderFileEntity getEntity(@NonNull String id) {
        Optional<OrderFileEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    public List<OrderFileVO> getByOrderId(String orderId) {
        return OrderFileVOEntityMapStruct.INSTANCE.toVOs(repository.findAllByOrderId(orderId));
    }
    /*
     * 全てのファイル
     */

    public List<OrderFileVO> getAll() {
        return OrderFileVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * ファイルのリスト
     */

    public ListVO<OrderFileVO> getPages(QueryOrderFileVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<OrderFileEntity> specification = Search(qVo);
        Page<OrderFileEntity> pages = repository.findAll(specification, pageable);
        List<OrderFileVO> list = OrderFileVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<OrderFileVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * ファイル検索
     */
    public Specification<OrderFileEntity> Search(QueryOrderFileVO qVo) {
        return new Specification<OrderFileEntity>() {
            public Predicate toPredicate(Root<OrderFileEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                // keyword
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preOrderNo = cb.like(root.get("orderNo"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preFileName = cb.like(root.get("fileName"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preRemark = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(
                            cb.or(preOrderNo, preFileName, preRemark));
                }
                if (null != qVo.getOrderId()) {
                    Predicate preOrderId = cb.equal(root.get("orderId"), qVo.getOrderId());
                    predicate.add(preOrderId);
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

    public OrderFileVO addOrEdit(IUOrderFileVO iuVo) {
        OrderFileEntity entity = OrderFileVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return OrderFileVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(entity));
    }

    public OrderFileEntity insertOrUpdate(OrderFileEntity entity) {
        if (null != entity) {
            return repository.save(entity);
        } else {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
    }

    public Boolean setShare(String id) {
        OrderFileEntity entity = getEntity(id);
        entity.setShare(!entity.getShare());
        repository.save(entity);
        return true;
    }

    /*
     * 削除
     */
    public Boolean delete(String id) {
        OrderFileEntity entity = getEntity(id);
        entity.setIsDelete(true);
        entity.setDeleteBy(LoginUtils.getLoginUserId());
        entity.setDeleteTime(new Date());
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
            OrderFileEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
