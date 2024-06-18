package com.cbs.oukasystem.service.operate;

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
import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.operate.CallEntity;
import com.cbs.oukasystem.mapstruct.operate.CallVOEntityMapStruct;
import com.cbs.oukasystem.repository.operate.CallRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.operate.IUCallVO;
import com.cbs.oukasystem.vo.in.operate.QueryCallVO;
import com.cbs.oukasystem.vo.out.operate.CallVO;

@Service
@Transactional
public class CallService {

    String KEY = "点呼";

    @Autowired
    private CallRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    // region Call

    // region 検索

    public CallVO getById(String id) {
        return CallVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public CallEntity getEntity(String id) {
        Optional<CallEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<CallVO> getAll() {
        List<CallEntity> entities = repository.findAll();
        List<CallVO> vos = CallVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<CallVO> getByDriverIdAndDate(String userId, String date) {
        return CallVOEntityMapStruct.INSTANCE.toVOs(repository.findByDriverIdAndDate(userId, date));
    }

    public List<CallEntity> findByDriverIdAndDate(String userId, String date) {
        return repository.findByDriverIdAndDate(userId, date);
    }

    public List<CallEntity> findByUserId(String userId) {
        return repository.findByDriverId(userId);
    }

    public List<CallEntity> findAll(Specification<CallEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<CallVO> getPages(QueryCallVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<CallEntity> specification = Search(qVo);
        Page<CallEntity> pages = repository.findAll(specification, pageable);
        List<CallVO> list = CallVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<CallVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<CallEntity> Search(QueryCallVO qVo) {
        return new Specification<CallEntity>() {

            public Predicate toPredicate(Root<CallEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preCarNo = cb.like(root.get("carNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preCarName = cb.like(root.get("carName"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverNo = cb.like(root.get("driverNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverName = cb.like(root.get("driverName"), "%" + qVo.getKeyword() + "%");
                    Predicate preManager = cb.like(root.get("manager"), "%" + qVo.getKeyword() + "%");
                    Predicate preHelper = cb.like(root.get("helper"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preCarNo, preCarName, preDriverNo, preDriverName, preManager, preHelper));
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
    public CallVO addOrEdit(IUCallVO iuVo) {
        return CallVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public CallEntity insertOrUpdate(CallEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public CallEntity insertOrUpdate(IUCallVO iuVo) {
        CallEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setAmCallMethod(iuVo.getAmCallMethod());
                entity.setAmCallTime(iuVo.getAmCallTime());
                entity.setAmConfirmBy(iuVo.getAmConfirmBy());
                entity.setAmRemark(iuVo.getAmRemark());
                entity.setAm1(iuVo.getAm1());
                entity.setAm2(iuVo.getAm2());
                entity.setAm3(iuVo.getAm3());
                entity.setAm4(iuVo.getAm4());

                entity.setPmCallMethod(iuVo.getPmCallMethod());
                entity.setPmCallTime(iuVo.getPmCallTime());
                entity.setPmConfirmBy(iuVo.getPmConfirmBy());
                entity.setPmRemark(iuVo.getPmRemark());
                entity.setPm1(iuVo.getPm1());
                entity.setPm2(iuVo.getPm2());
                entity.setPm3(iuVo.getPm3());
                entity.setPm4(iuVo.getPm4());

                entity.setWeather(iuVo.getWeather());
                entity.setManager(iuVo.getManager());
                entity.setHelper(iuVo.getHelper());

            } else {
                // 追加
                entity = CallVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }
            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public CallVO setAmCall(IUCallVO iuVo) {
        CallEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setAmCallMethod(iuVo.getAmCallMethod());
                entity.setAmCallTime(iuVo.getAmCallTime());
                entity.setAmConfirmBy(iuVo.getAmConfirmBy());
                entity.setAmRemark(iuVo.getAmRemark());
                entity.setAm1(iuVo.getAm1());
                entity.setAm2(iuVo.getAm2());
                entity.setAm3(iuVo.getAm3());
                entity.setAm4(iuVo.getAm4());

                entity.setWeather(iuVo.getWeather());
                entity.setManager(iuVo.getManager());
                entity.setHelper(iuVo.getHelper());

            } else {
                // 追加
                entity = CallVOEntityMapStruct.INSTANCE.toEntity(iuVo);

                // ステータス の 変更
                userService.resetStatus(iuVo.getDriverId(), EnumUserStatus.Working);
                carService.resetStatus(iuVo.getCarId(), EnumCarStatus.Working);
            }
            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return CallVOEntityMapStruct.INSTANCE.toVO(entity);
    }

    public CallVO setPmCall(IUCallVO iuVo) {
        CallEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setPmCallMethod(iuVo.getPmCallMethod());
                entity.setPmCallTime(iuVo.getPmCallTime());
                entity.setPmConfirmBy(iuVo.getPmConfirmBy());
                entity.setPmRemark(iuVo.getPmRemark());
                entity.setPm1(iuVo.getPm1());
                entity.setPm2(iuVo.getPm2());
                entity.setPm3(iuVo.getPm3());
                entity.setPm4(iuVo.getPm4());

                // ステータス の 変更
                userService.resetStatus(entity.getDriverId(), EnumUserStatus.Resting);
                carService.resetStatus(entity.getCarId(), EnumCarStatus.Standing);

            }
            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return CallVOEntityMapStruct.INSTANCE.toVO(entity);
    }

    public Boolean audit(String id) {

        try {
            CallEntity entity = getEntity(id);
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
            CallEntity entity = getEntity(id);
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
