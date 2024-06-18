package com.cbs.oukasystem.service.car;

import java.util.ArrayList;
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
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.car.CarRepairEntity;
import com.cbs.oukasystem.mapstruct.car.CarRepairVOEntityMapStruct;
import com.cbs.oukasystem.repository.car.CarRepairRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.IUCarRepairVO;
import com.cbs.oukasystem.vo.in.car.QueryCarRepairVO;
import com.cbs.oukasystem.vo.out.car.CarRepairVO;

@Service
@Transactional
public class CarRepairService {

    String KEY = "車両修理";

    @Autowired
    private CarRepairRepository repository;

    // region CarRepair

    // region 検索

    public CarRepairVO getById(String id) {
        return CarRepairVOEntityMapStruct.INSTANCE.toCarRepairVO(getEntity(id));
    }

    public CarRepairEntity getEntity(String id) {
        Optional<CarRepairEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<CarRepairVO> getAll() {
        List<CarRepairEntity> entities = repository.findAll();
        List<CarRepairVO> vos = CarRepairVOEntityMapStruct.INSTANCE.toCarRepairVOs(entities);
        return vos;
    }

    public List<CarRepairEntity> findAll(Specification<CarRepairEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<CarRepairVO> getPages(QueryCarRepairVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<CarRepairEntity> specification = Search(qVo);
        Page<CarRepairEntity> pages = repository.findAll(specification, pageable);
        List<CarRepairVO> list = CarRepairVOEntityMapStruct.INSTANCE.toCarRepairVOs(pages.toList());
        ListVO<CarRepairVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<CarRepairEntity> Search(QueryCarRepairVO qVo) {
        return new Specification<CarRepairEntity>() {

            public Predicate toPredicate(Root<CarRepairEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preCarRepairNo = cb.like(root.get("carNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preCarRepairName = cb.like(root.get("carName"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preCarRepairNo, preCarRepairName));
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
    public CarRepairVO addOrEdit(IUCarRepairVO iuVo) {
        return CarRepairVOEntityMapStruct.INSTANCE.toCarRepairVO(insertOrUpdate(iuVo));
    }

    public CarRepairEntity insertOrUpdate(CarRepairEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public CarRepairEntity insertOrUpdate(IUCarRepairVO iuVo) {
        CarRepairEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setCarId(iuVo.getCarId());
                entity.setCarName(iuVo.getCarName());
                entity.setCarNo(iuVo.getCarNo());
                entity.setRepairType(iuVo.getRepairType());
                entity.setRepairTime(iuVo.getRepairTime());
                entity.setStatus(iuVo.getStatus());
                entity.setRemark(iuVo.getRemark());
            } else {
                // 追加
                entity = CarRepairVOEntityMapStruct.INSTANCE.toCarRepairEntity(iuVo);
            }
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public Boolean audit(String id) {
        try {
            CarRepairEntity entity = getEntity(id);
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
            CarRepairEntity entity = getEntity(id);
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

    // endregion

    // region スケジュール

    // region 追加、更新

    // endregion

    // endregion
}
