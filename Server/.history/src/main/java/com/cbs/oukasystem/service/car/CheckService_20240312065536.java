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
import com.cbs.oukasystem.entity.car.CarCheckEntity;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.mapstruct.car.CarCheckVOEntityMapStruct;
import com.cbs.oukasystem.repository.car.CarCheckRepository;
import com.cbs.oukasystem.service.operate.ScheduleService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.IUCarCheckVO;
import com.cbs.oukasystem.vo.in.car.QueryCarCheckVO;
import com.cbs.oukasystem.vo.in.operate.QueryScheduleVO;
import com.cbs.oukasystem.vo.out.car.CarCheckVO;

@Service
@Transactional
public class CheckService {

    String KEY = "車両点検";

    @Autowired
    private CarCheckRepository repository;

    // region CarCheck

    // region 検索

    public CarCheckVO getById(String id) {
        return CarCheckVOEntityMapStruct.INSTANCE.toCarCheckVO(getEntity(id));
    }

    public CarCheckEntity getEntity(String id) {
        Optional<CarCheckEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<CarCheckVO> getAll() {
        List<CarCheckEntity> entities = repository.findAll();
        List<CarCheckVO> vos = CarCheckVOEntityMapStruct.INSTANCE.toCarCheckVOs(entities);
        return vos;
    }

    public List<CarCheckEntity> findAll(Specification<CarCheckEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<CarCheckVO> getPages(QueryCarCheckVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<CarCheckEntity> specification = Search(qVo);
        Page<CarCheckEntity> pages = repository.findAll(specification, pageable);
        List<CarCheckVO> list = CarCheckVOEntityMapStruct.INSTANCE.toCarCheckVOs(pages.toList());
        ListVO<CarCheckVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<CarCheckEntity> Search(QueryCarCheckVO qVo) {
        return new Specification<CarCheckEntity>() {

            public Predicate toPredicate(Root<CarCheckEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preCarCheckNo = cb.like(root.get("carNo"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preCarCheckNo));
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
    public CarCheckVO addOrEdit(IUCarCheckVO iuVo) {
        return CarCheckVOEntityMapStruct.INSTANCE.toCarCheckVO(insertOrUpdate(iuVo));
    }

    public CarCheckEntity insertOrUpdate(CarCheckEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public CarCheckEntity insertOrUpdate(IUCarCheckVO iuVo) {
        CarCheckEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setCarId(iuVo.getCarId());
                entity.setCarName(iuVo.getCarName());
                entity.setCarNo(iuVo.getCarNo());
                entity.setMonth(iuVo.getMonth());
                entity.setYear(iuVo.getYear());
                entity.setUrl(iuVo.getUrl());
                entity.setRemark(iuVo.getRemark());

            } else {
                // 追加
                entity = CarCheckVOEntityMapStruct.INSTANCE.toCarCheckEntity(iuVo);
            }
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public Boolean audit(String id) {
        try {
            CarCheckEntity entity = getEntity(id);
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
            CarCheckEntity entity = getEntity(id);
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

    // region 検索

    // endregion

    // region 追加、更新

    // endregion

    // endregion
}
