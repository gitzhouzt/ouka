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
import com.cbs.oukasystem.entity.order.OrderGoodsEntity;
import com.cbs.oukasystem.mapstruct.order.OrderGoodsVOEntityMapStruct;
import com.cbs.oukasystem.repository.order.OrderGoodsRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.order.IUOrderGoodsVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;

@Service
@Transactional
public class OrderGoodsService {

    String KEY = "備品";

    @Autowired
    private OrderGoodsRepository repository;

    /*
     * idによって備品を得る
     */

    public OrderGoodsVO getById(String id) {
        return OrderGoodsVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    /*
     * 備品を得る
     */
    public OrderGoodsEntity getEntity(String id) {
        Optional<OrderGoodsEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }
    /*
     * 全ての備品
     */

    public List<OrderGoodsVO> getAll() {
        return OrderGoodsVOEntityMapStruct.INSTANCE.toVOs(repository.findAll());
    }

    /*
     * 備品のリスト
     */

    public ListVO<OrderGoodsVO> getPages(QueryOrderGoodsVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<OrderGoodsEntity> specification = Search(qVo);
        Page<OrderGoodsEntity> pages = repository.findAll(specification, pageable);
        List<OrderGoodsVO> list = OrderGoodsVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<OrderGoodsVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public List<OrderGoodsVO> getByOrderId(String orderId) {
        return OrderGoodsVOEntityMapStruct.INSTANCE.toVOs(repository.findAllByOrderId(orderId));
    }

    /*
     * 備品検索
     */
    public Specification<OrderGoodsEntity> Search(QueryOrderGoodsVO qVo) {
        return new Specification<OrderGoodsEntity>() {
            public Predicate toPredicate(Root<OrderGoodsEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                // keyword
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preOrderNo = cb.like(root.get("orderNo"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preGoodsType = cb.like(root.get("goodsType"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preRemark = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(
                            cb.or(preOrderNo, preGoodsType, preRemark));
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

    public OrderGoodsVO addOrEdit(IUOrderGoodsVO iuVo) {
        return OrderGoodsVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public OrderGoodsEntity save(OrderGoodsEntity entity) {
        return repository.save(entity);
    }

    public OrderGoodsEntity insertOrUpdate(IUOrderGoodsVO iuVo) {
        OrderGoodsEntity entity = null;
        try {
            // 更新
            entity = OrderGoodsVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    /*
     * 削除
     */
    public Boolean delete(String id) {
        OrderGoodsEntity entity = getEntity(id);
        entity.setIsDelete(true);
        entity.setDeleteBy(LoginUtils.getLoginUserId());
        entity.setDeleteTime(new Date());
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
            OrderGoodsEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
