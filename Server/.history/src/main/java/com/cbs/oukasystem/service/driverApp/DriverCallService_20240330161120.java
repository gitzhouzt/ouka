package com.cbs.oukasystem.service.driverApp;

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
public class DriverCallService {

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

    public List<CallVO> getByDriverIdAndDate(String driverId, String date) {
        return CallVOEntityMapStruct.INSTANCE.toVOs(repository.findByDriverIdAndDate(driverId, date));
    }

    public List<CallEntity> findByDriverIdAndDate(String driverId, String date) {
        return repository.findByDriverIdAndDate(driverId, date);
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

    // endregion

}
