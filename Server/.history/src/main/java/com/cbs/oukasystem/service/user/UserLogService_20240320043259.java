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
import com.cbs.oukasystem.common.MessageEnum.EnumRegisterCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.user.UserLogEntity;
import com.cbs.oukasystem.mapstruct.user.UserLogVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserLogRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.IUUserLogVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

@Service
@Transactional
public class UserLogService {

    String KEY = "仕事日記";

    @Autowired
    private UserLogRepository repository;

    /*
     * 仕事日記を得る
     */

    public UserLogVO getById(String id) {
        return UserLogVOEntityMapStruct.INSTANCE.toVo(getEntity(id));
    }

    public UserLogEntity findOne(Specification<UserLogEntity> spec) {
        Optional<UserLogEntity> uOptional = repository.findOne(spec);
        UserLogEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public UserLogEntity getEntity(String id) {
        Optional<UserLogEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    public UserLogEntity findByUserIdAndDate(String id) {
        Optional<UserLogEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての仕事日記
     */

    public List<UserLogVO> getAll() {
        return UserLogVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<UserLogEntity> findAll() {
        return repository.findAll();
    }

    public List<UserLogEntity> findAll(Specification<UserLogEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * 仕事日記のリスト
     */

    public ListVO<UserLogVO> getPages(QueryUserLogVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<UserLogEntity> specification = Search(qVo);
        Page<UserLogEntity> pages = repository.findAll(specification, pageable);
        List<UserLogVO> list = UserLogVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<UserLogVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 仕事日記検索
     */
    public Specification<UserLogEntity> Search(QueryUserLogVO qVo) {
        return new Specification<UserLogEntity>() {

            public Predicate toPredicate(Root<UserLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preDriver1Name = cb.like(root.get("driver1Name"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriver1No = cb.like(root.get("driver1No"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriver2Name = cb.like(root.get("driver2Name"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriver2No = cb.like(root.get("driver2No"), "%" + qVo.getKeyword() + "%");
                    Predicate preCarNo = cb.like(root.get("carNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preCarName = cb.like(root.get("carName"), "%" + qVo.getKeyword() + "%");
                    predicate.add(
                            cb.or(preDriver1Name, preDriver1No, preDriver2Name, preDriver2No, preCarNo, preCarName));
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

    public UserLogVO insertOrUpdate(IUUserLogVO iuVo) {
        List<String> roles = new ArrayList<String>();
        roles.add("admin");
        roles.add("administrator");
        if (roles.contains(iuVo.getUserName().toLowerCase())) {
            throw new BaseException(EnumRegisterCheck.CANT_ADMIN);
        }
        UserLogEntity newUserLog = UserLogVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return UserLogVOEntityMapStruct.INSTANCE.toVo(insertOrUpdate(newUserLog));
    }

    public UserLogEntity insertOrUpdate(UserLogEntity newUserLog) {
        UserLogEntity entity = null;
        if (null != newUserLog.getId() && !newUserLog.getId().isEmpty()) {
            // 更新

        } else {

        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * 削除
     */

    public Boolean delete(String id) {
        try {
            UserLogEntity entity = getEntity(id);
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
        UserLogEntity entity = getEntity(id);
        entity.setIsAudit(!entity.getIsAudit());
        if (null == repository.save(entity)) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return true;
    }

}
