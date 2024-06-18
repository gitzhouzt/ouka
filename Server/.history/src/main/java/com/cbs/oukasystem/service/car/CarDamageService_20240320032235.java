package com.cbs.oukasystem.service.car;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.Constants;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.car.CarDamageEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.car.CarDamageVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.finance.PayRecordVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.car.CarDamageRepository;
import com.cbs.oukasystem.service.operate.AccidentService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.IUCarDamageVO;
import com.cbs.oukasystem.vo.in.car.QueryCarDamageVO;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.out.car.CarDamageVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;

@Service
@Transactional
public class CarDamageService {

    String KEY = "車両";

    @Autowired
    private CarDamageRepository repository;

    @Autowired
    private UserService userService;

    // region 検索

    public CarDamageVO getById(String id) {
        return CarDamageVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public CarDamageEntity getEntity(String id) {
        Optional<CarDamageEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<CarDamageVO> getAll() {
        List<CarDamageEntity> entities = repository.findAll();
        List<CarDamageVO> vos = CarDamageVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<PayRecordVO> getByCashOrderId(String orderId) {
        return PayRecordVOEntityMapStruct.INSTANCE
                .toVOs(repository.findAllByOrderIdAndPayMethodCode(orderId, "cash"));
    }

    public List<CarDamageEntity> findAll(Specification<CarDamageEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<CarDamageVO> getPages(QueryCarDamageVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<CarDamageEntity> specification = Search(qVo);
        Page<CarDamageEntity> pages = repository.findAll(specification, pageable);
        List<CarDamageVO> list = CarDamageVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<CarDamageVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<CarDamageEntity> Search(QueryCarDamageVO qVo) {
        return new Specification<CarDamageEntity>() {
            public Predicate toPredicate(Root<CarDamageEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preRemark = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preRemark));
                }
                if (null != qVo.getCarId() && !qVo.getCarId().isEmpty()) {
                    predicate.add(cb.equal(root.get("carId"), "%" + qVo.getCarId() + "%"));
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

    // endregion

    // region 追加、更新
    public CarDamageVO addOrEdit(IUCarDamageVO iuVo) {
        return CarDamageVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public CarDamageEntity insertOrUpdate(CarDamageEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public CarDamageEntity insertOrUpdate(IUCarDamageVO iuVo) {
        CarDamageEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setImage1(iuVo.getImage1());
                entity.setImage2(iuVo.getImage2());
                entity.setImage3(iuVo.getImage3());
                entity.setImage4(iuVo.getImage4());
                entity.setRemark(iuVo.getRemark());
            } else {
                // 追加
                entity = CarDamageVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                UserEntity userEntity = userService.getEntity(LoginUtils.getLoginUserId());
                entity.setCreateBy(userEntity.getUserName());
            }
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public void export(QueryCarDamageVO qVo) {
        String screenName = "車両";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "車両番号", "carNo" },
                { "車両名", "carName" },
                { "タイプ", "carType" },
                { "座席数", "carSeat" },
                { "サービス地域", "city" },
                { "车牌", "plateNum" },
                { "駐車場", "carPark" },
                { "駐車料金", "parkingFee" },
                { "ステータス", "statusName" },
                { "remark", "備考" },
        };

        Specification<CarDamageEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<CarDamageEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                CarDamageVOEntityMapStruct.INSTANCE.toVOs(list));
    }

    public Boolean audit(String id) {
        try {
            CarDamageEntity entity = getEntity(id);
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
            CarDamageEntity entity = getEntity(id);
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
