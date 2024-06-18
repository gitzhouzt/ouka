package com.cbs.oukasystem.service.user;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
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
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.entity.user.UserRestEntity;
import com.cbs.oukasystem.mapstruct.user.UserRestVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserRestRepository;
import com.cbs.oukasystem.service.login.VerifyService;
import com.cbs.oukasystem.service.operate.ScheduleService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.login.IUVerifyVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.in.user.QueryUserRestVO;
import com.cbs.oukasystem.vo.out.user.UserRestVO;

@Service
@Transactional
public class UserRestService {

    String KEY = "休業";

    @Autowired
    private UserRestRepository repository;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private ScheduleService scheduleService;

    /*
     * ユーザーを得る
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
     * 全てのユーザー
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
     * ユーザーのリスト
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
     * ユーザー検索
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
                if (null != qVo.getIsAudit()) {
                    predicate.add(cb.equal(root.get("isAudit"), qVo.getIsAudit()));
                }
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public UserRestVO insertOrUpdate(IUUserRestVO iuVo) {
        List<String> roles = new ArrayList<String>();
        roles.add("admin");
        roles.add("administrator");
        UserRestEntity newUserRest = UserRestVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return UserRestVOEntityMapStruct.INSTANCE.toVo(insertOrUpdate(newUserRest));
    }

    public UserRestEntity insertOrUpdate(UserRestEntity newUserRest) {
        UserRestEntity entity = null;
        if (null != newUserRest.getId() && !newUserRest.getId().isEmpty()) {
            // 更新

        } else {

        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    public Boolean addSchedules(List<ScheduleEntity> scheduleEntities) {
        return scheduleService.saveAll(scheduleEntities);
    }

    public Boolean delSchedulesByOrderId(String orderId) {
        return scheduleService.deletePhysicsByOrderId(orderId);
    }

    public Boolean sendVerifyCode(IUVerifyVO iVo) {
        if (null != iVo.getIsPwd() && iVo.getIsPwd() == false) {
            List<UserRestEntity> list = repository
                    .findAll(isExist(iVo.getContent(), ""));
            if (!list.isEmpty()) {
                throw new BaseException(EnumDataCheck.USER_EXISTED);
            }
        }
        return verifyService.sendVerifyCode(iVo);
    }

    /*
     * ユーザー存在検証
     * email and name
     */
    public Specification<UserRestEntity> isExist(String loginName, String userId) {
        return new Specification<UserRestEntity>() {

            public Predicate toPredicate(Root<UserRestEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preLoginName = cb.equal(root.get("userName"), loginName);
                predicate.add(preLoginName);
                if (null != userId && !userId.isEmpty()) {
                    predicate.add(cb.notEqual(root.get("id"), userId));
                }
                predicate.add(cb.notEqual(root.get("isAudit"), false));
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
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
