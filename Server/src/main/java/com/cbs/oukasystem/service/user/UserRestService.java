package com.cbs.oukasystem.service.user;

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
import com.cbs.oukasystem.entity.user.UserRestEntity;
import com.cbs.oukasystem.mapstruct.user.UserRestVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserRestRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.in.user.QueryUserRestVO;
import com.cbs.oukasystem.vo.out.user.UserRestVO;

@Service
@Transactional
public class UserRestService {

    String KEY = "休暇";

    @Autowired
    private UserRestRepository repository;

    /*
     * 休暇を得る
     */

    public UserRestVO getById(String id) {
        return UserRestVOEntityMapStruct.INSTANCE.toVo(getEntity(id));
    }

    public UserRestVO getByLogin() {
        return UserRestVOEntityMapStruct.INSTANCE.toVo(getEntity(LoginUtils.getLoginUserId()));
    }

    public UserRestEntity findOne(Specification<UserRestEntity> spec) {
        Optional<UserRestEntity> uOptional = repository.findOne(spec);
        UserRestEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public UserRestEntity getEntity(String id) {
        Optional<UserRestEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての休暇
     */

    public List<UserRestVO> getAll() {
        return UserRestVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<UserRestEntity> findAll() {
        return repository.findAll();
    }

    public List<UserRestEntity> findAll(Specification<UserRestEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * 休暇のリスト
     */

    public ListVO<UserRestVO> getPages(QueryUserRestVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<UserRestEntity> specification = Search(qVo);
        Page<UserRestEntity> pages = repository.findAll(specification, pageable);
        List<UserRestVO> list = UserRestVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<UserRestVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 休暇検索
     */
    public Specification<UserRestEntity> Search(QueryUserRestVO qVo) {
        return new Specification<UserRestEntity>() {

            public Predicate toPredicate(Root<UserRestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preUserName = cb.like(root.get("userName"), "%" + qVo.getKeyword() + "%");
                    Predicate preUserNo = cb.like(root.get("userNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preRemark = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preUserName, preUserNo, preRemark));
                }
                if (null != qVo.getUserRole() && !qVo.getUserRole().isEmpty()) {
                    predicate.add(cb.like(root.get("userRoles"), "%" + qVo.getUserRole() + "%"));
                }
                if (null != qVo.getRestType() && !qVo.getRestType().isEmpty()) {
                    predicate.add(cb.equal(root.get("restType"), qVo.getRestType()));
                }

                if (null != qVo.getRestTypeCode() && !qVo.getRestTypeCode().isEmpty()) {
                    predicate.add(cb.equal(root.get("restTypeCode"), qVo.getRestTypeCode()));
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

    public UserRestVO addOrEdit(IUUserRestVO iuVo) {
        return UserRestVOEntityMapStruct.INSTANCE.toVo(insertOrUpdate(iuVo));
    }

    public UserRestEntity insertOrUpdate(IUUserRestVO iuVo) {
        UserRestEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setRestType(iuVo.getRestType());
                entity.setRemark(iuVo.getRemark());
                entity.setStartTime(iuVo.getStartTime());
                entity.setEndTime(iuVo.getEndTime());
            } else {
                // 追加
                entity = UserRestVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }

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
        try {
            UserRestEntity entity = getEntity(id);
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
        UserRestEntity entity = getEntity(id);
        entity.setIsAudit(!entity.getIsAudit());
        if (null == repository.save(entity)) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return true;
    }

}
