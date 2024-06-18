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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.car.CarCheckEntity;
import com.cbs.oukasystem.mapstruct.car.CarCheckVOEntityMapStruct;
import com.cbs.oukasystem.repository.car.CarCheckRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.IUCarCheckVO;
import com.cbs.oukasystem.vo.in.car.QueryCarCheckVO;
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
        return CarCheckVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
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
        List<CarCheckVO> vos = CarCheckVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<CarCheckEntity> findAll(Specification<CarCheckEntity> spec) {
        return repository.findAll(spec);
    }

    public List<CarCheckVO> findAllByCarId(String carId) {
        return CarCheckVOEntityMapStruct.INSTANCE
                .toVOs(repository.findAllByCarId(carId));
    }

    public ListVO<CarCheckVO> getPages(QueryCarCheckVO qVo) {
        String sort = "carNo";
        Direction dir = Sort.Direction.DESC;
        if (null != qVo.getSort() && !qVo.getSort().isEmpty()) {
            sort = qVo.getSort();
        }
        if (null != qVo.getSortType()) {
            if (qVo.getSortType().equals("ascend")) {
                dir = Sort.Direction.ASC;
            } else if (qVo.getSortType().equals("descend")) {
                dir = Sort.Direction.DESC;
            } else {
                sort = "carNo";
                dir = Sort.Direction.DESC;
            }
        }
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                dir, sort);
        Specification<CarCheckEntity> specification = Search(qVo);
        Page<CarCheckEntity> pages = repository.findAll(specification, pageable);
        List<CarCheckVO> list = CarCheckVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
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
                if (null != qVo.getYear() && !qVo.getYear().isEmpty()) {
                    predicate.add(cb.equal(root.get("year"), qVo.getYear()));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    // endregion

    // region 追加、更新
    public CarCheckVO addOrEdit(IUCarCheckVO iuVo) {
        return CarCheckVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public CarCheckEntity insertOrUpdate(IUCarCheckVO iuVo) {
        CarCheckEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setYear(iuVo.getYear());

                entity.setYearCheckStatus(iuVo.getYearCheckStatus());
                entity.setYearCheckDate(iuVo.getYearCheckDate());
                entity.setMonthCheckDate1(iuVo.getMonthCheckDate1());
                entity.setMonthCheckDate2(iuVo.getMonthCheckDate2());
                entity.setMonthCheckDate3(iuVo.getMonthCheckDate3());
                entity.setCheckStatus1(iuVo.getCheckStatus1());
                entity.setCheckStatus2(iuVo.getCheckStatus2());
                entity.setCheckStatus3(iuVo.getCheckStatus3());

                if (iuVo.getYearCheckStatus()) {
                    entity.setLastYearCheck(entity.getYearCheckDate());
                }
                if (iuVo.getCheckStatus1()) {
                    entity.setLastMonthCheck(entity.getMonthCheckDate1());
                }
                if (iuVo.getCheckStatus2()) {
                    entity.setLastMonthCheck(entity.getMonthCheckDate2());
                }
                if (iuVo.getCheckStatus3()) {
                    entity.setLastMonthCheck(entity.getMonthCheckDate3());
                }
            } else {
                // 追加
                entity = CarCheckVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public CarCheckEntity setLastCheckDate(IUCarCheckVO iuVo) {
        CarCheckEntity entity = null;
        try {
            if (null == iuVo.getId() || iuVo.getId().isEmpty()) {
                throw new BaseException(EnumDataCheck.EMPTY);
            }
            entity = getEntity(iuVo.getId());
            entity.setLastMonthCheck(iuVo.getLastMonthCheck());
            entity.setLastYearCheck(iuVo.getLastYearCheck());
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }
    // endregion

    // region 削除

    public Boolean deletePhysics(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysicsByCarId(String carId) {
        try {
            repository.deleteByCarId(carId);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    // endregion

    // endregion

}
