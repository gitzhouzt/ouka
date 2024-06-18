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
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.entity.user.UserLogEntity;
import com.cbs.oukasystem.entity.user.LogRecordEntity;
import com.cbs.oukasystem.entity.user.UserRestEntity;
import com.cbs.oukasystem.mapstruct.finance.PayRecordVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.user.LogRecordVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.user.UserRestVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.LogRecordRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.IULogRecordVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.in.user.QueryLogRecordVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.user.LogRecordVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;
import com.cbs.oukasystem.vo.out.user.UserRestVO;

@Service
@Transactional
public class LogRecordService {

    String KEY = "仕事日記";

    @Autowired
    private LogRecordRepository repository;

    @Autowired
    private UserLogService logService;

    /*
     * 仕事日記を得る
     */

    public LogRecordVO getById(String id) {
        return LogRecordVOEntityMapStruct.INSTANCE.toVo(getEntity(id));
    }

    public LogRecordEntity findOne(Specification<LogRecordEntity> spec) {
        Optional<LogRecordEntity> uOptional = repository.findOne(spec);
        LogRecordEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public LogRecordEntity getEntity(String id) {
        Optional<LogRecordEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    public List<LogRecordVO> findByUserIdAndLogId(String userId, String logId) {
        return LogRecordVOEntityMapStruct.INSTANCE
                .toVOs(repository.findByUserIdAndLogId(userId, logId));
    }

    /*
     * 全ての仕事日記
     */

    public List<LogRecordVO> getAll() {
        return LogRecordVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<LogRecordEntity> findAll() {
        return repository.findAll();
    }

    public List<LogRecordEntity> findAll(Specification<LogRecordEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * 仕事日記のリスト
     */

    public ListVO<LogRecordVO> getPages(QueryLogRecordVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<LogRecordEntity> specification = Search(qVo);
        Page<LogRecordEntity> pages = repository.findAll(specification, pageable);
        List<LogRecordVO> list = LogRecordVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<LogRecordVO> listDto = new ListVO<>();
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
    public Specification<LogRecordEntity> Search(QueryLogRecordVO qVo) {
        return new Specification<LogRecordEntity>() {

            public Predicate toPredicate(Root<LogRecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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

    public LogRecordVO addOrEdit(IULogRecordVO iuVo) {
        return LogRecordVOEntityMapStruct.INSTANCE.toVo(insertOrUpdate(iuVo));
    }

    public LogRecordEntity insertOrUpdate(IULogRecordVO iuVo) {
        LogRecordEntity entity = null;
        try {

            List<UserLogVO> logVOs = logService.findByDriver1IdAndDate(LoginUtils.getLoginUserId(),
                    iuVo.getDate());
            // 更新
            if (logVOs.size() > 0) {

                // entity = getEntity(iuVo.getId());
                // entity.setStart(iuVo.getStart());
                // entity.setStartTime(iuVo.getStartTime());
                // entity.setEnd(iuVo.getEnd());
                // entity.setEndTime(iuVo.getEndTime());
                // entity.setDistance(iuVo.getDistance());
                // entity.setJissha(iuVo.getJissha());
                // entity.setKaisou(iuVo.getKaisou());
                // entity.setRest(iuVo.getRest());
                // entity.setRestNum(iuVo.getRestNum());
                // entity.setRemark(iuVo.getRemark());

                // 追加
                entity = LogRecordVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                entity.setLogId(logVOs.get(0).getId());
                entity.setUserId(LoginUtils.getLoginUserId());

            } else {

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
            LogRecordEntity entity = getEntity(id);
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
        LogRecordEntity entity = getEntity(id);
        entity.setIsAudit(!entity.getIsAudit());
        if (null == repository.save(entity)) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return true;
    }

}
