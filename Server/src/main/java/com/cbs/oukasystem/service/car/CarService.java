package com.cbs.oukasystem.service.car;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.CommonEnum.EnumUserRole;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.Constants;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumMailCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.car.CarEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.car.CarVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.car.CarRepository;
import com.cbs.oukasystem.service.operate.AccidentService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.IUCarCheckVO;
import com.cbs.oukasystem.vo.in.car.IUCarRepairVO;
import com.cbs.oukasystem.vo.in.car.IUCarVO;
import com.cbs.oukasystem.vo.in.car.QueryCarVO;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.out.car.CarCheckVO;
import com.cbs.oukasystem.vo.out.car.CarVO;

@Service
@Transactional
public class CarService {

    String KEY = "車両";

    @Autowired
    private CarRepository repository;

    @Autowired
    private CheckService checkService;

    // region 検索

    public CarVO getById(String id) {
        return CarVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public CarEntity getEntity(String id) {
        Optional<CarEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<CarVO> getAll() {
        List<CarEntity> entities = repository.findAll();
        List<CarVO> vos = CarVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<CarEntity> findAll() {
        return repository.findAll();
    }

    public List<CarEntity> findAll(Specification<CarEntity> spec) {
        return repository.findAll(spec);
    }

    public List<CarEntity> findByIsAuditAndIsDelete(Boolean isAudit, Boolean isDelete) {
        return repository.findByIsAuditAndIsDelete(isAudit, isDelete);
    }

    public List<CarVO> queryTodayCars(String userId) {
        List<Map<String, String>> maps = repository.queryTodayCars(userId);
        Object data = maps;
        List<CarVO> list = (List<CarVO>) data;
        return list;
    }

    public ListVO<CarVO> getPages(QueryCarVO qVo) {
        String sort = "updateTime";
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
                sort = "updateTime";
                dir = Sort.Direction.DESC;
            }
        }
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                dir, sort);
        Specification<CarEntity> specification = Search(qVo);
        Page<CarEntity> pages = repository.findAll(specification, pageable);
        List<CarVO> list = CarVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<CarVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<CarEntity> Search(QueryCarVO qVo) {
        return new Specification<CarEntity>() {
            public Predicate toPredicate(Root<CarEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate carNo = cb.like(root.get("carNo"), "%" + qVo.getKeyword() + "%");
                    Predicate carName = cb.like(root.get("carName"), "%" + qVo.getKeyword() + "%");
                    Predicate plateNum = cb.like(root.get("plateNum"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(carNo, carName, plateNum));
                }
                if (null != qVo.getCarType() && !qVo.getCarType().isEmpty()) {
                    predicate.add(cb.equal(root.get("carType"), qVo.getCarType()));
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
    public CarVO addOrEdit(IUCarVO iuVo) {
        return CarVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public CarEntity insertOrUpdate(CarEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public CarEntity insertOrUpdate(IUCarVO iuVo) {
        CarEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setCarNo(iuVo.getCarNo());
                entity.setCarName(iuVo.getCarName());
                entity.setCarPark(iuVo.getCarPark());
                entity.setImages(iuVo.getImages());
                entity.setCarSeat(iuVo.getCarSeat());
                entity.setCarType(iuVo.getCarType());
                entity.setParkingFee(iuVo.getParkingFee());
                entity.setPlateNum(iuVo.getPlateNum());
                entity.setRemark(iuVo.getRemark());
            } else {
                // 追加
                entity = CarVOEntityMapStruct.INSTANCE.toEntity(iuVo);
                if (null == entity.getCarNo() || entity.getCarNo().isEmpty()) {
                    String carNo = CommonUtils.getNewDateEquipmentNo(Constants.DEFAULT_CAR_PREFIX,
                            repository.count());
                    entity.setCarNo(carNo);
                }
            }
            if (null == entity.getCarName() || entity.getCarName().isEmpty()) {
                entity.setCarName(entity.getCarNo());
            }
            entity = repository.save(entity);

            // 插入点检表
            List<CarEntity> list = new ArrayList<>();
            list.add(entity);
            addCheck(list);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public void export(QueryCarVO qVo) {
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

        Specification<CarEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<CarEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                CarVOEntityMapStruct.INSTANCE.toVOs(list));
    }

    public Boolean audit(String id) {
        try {
            CarEntity entity = getEntity(id);
            entity.setIsAudit(!entity.getIsAudit());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    /*
     * ステータス の 変更
     */
    public Boolean resetStatus(String id, EnumCarStatus carStatus) {
        CarEntity car = getEntity(id);
        car.setStatus(carStatus);
        car.setStatusName(carStatus.getMessage());
        repository.save(car);
        return true;
    }

    // endregion

    // region 削除
    public Boolean delete(String id) {
        try {
            CarEntity entity = getEntity(id);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);

            // 删除车辆点检信息
            checkService.deletePhysicsByCarId(id);

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

    /*
     * 插入点检表
     */
    public void addCheck(List<CarEntity> entities) {
        for (CarEntity entity : entities) {
            List<CarCheckVO> carCheckVOs = checkService.findAllByCarId(entity.getId());
            if (carCheckVOs.size() <= 0) {
                IUCarCheckVO iuCarCheckVO = new IUCarCheckVO();
                iuCarCheckVO.setCarId(entity.getId());
                iuCarCheckVO.setCarNo(entity.getCarNo());
                iuCarCheckVO.setYear(CommonUtils.getCurrentDate("yyyy"));
                checkService.addOrEdit(iuCarCheckVO);
            }
        }
    }

    /*
     * 12月生成新年点检表 Scheduled
     * s m h day month week year
     */
    @Scheduled(cron = "0 0 1 12 * ?")
    public Boolean month12check() {
        List<CarEntity> entities = findByIsAuditAndIsDelete(true, false);
        addCheck(entities);
        return true;
    }

}
