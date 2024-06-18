package com.cbs.oukasystem.service.operate;

import java.text.SimpleDateFormat;
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

import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumCarStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.operate.AccidentEntity;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.mapstruct.operate.AccidentVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.order.OrderVOEntityMapStruct;
import com.cbs.oukasystem.repository.operate.AccidentRepository;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.operate.IUAccidentVO;
import com.cbs.oukasystem.vo.in.operate.QueryAccidentVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.out.operate.AccidentVO;

@Service
@Transactional
public class AccidentService {

    String KEY = "事故";

    @Autowired
    private AccidentRepository repository;

    @Autowired
    private CarService carService;

    @Autowired
    private ScheduleService scheduleService;

    // region Accident

    // region 検索

    public AccidentVO getById(String id) {
        return AccidentVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public AccidentEntity getEntity(String id) {
        Optional<AccidentEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<AccidentVO> getAll() {
        List<AccidentEntity> entities = repository.findAll();
        List<AccidentVO> vos = AccidentVOEntityMapStruct.INSTANCE.toVOs(entities);
        return vos;
    }

    public List<AccidentEntity> findAll(Specification<AccidentEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<AccidentVO> getPages(QueryAccidentVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1,
                qVo.getPageSize(),
                Sort.Direction.DESC, "updateTime");
        Specification<AccidentEntity> specification = Search(qVo);
        Page<AccidentEntity> pages = repository.findAll(specification, pageable);
        List<AccidentVO> list = AccidentVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<AccidentVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public Specification<AccidentEntity> Search(QueryAccidentVO qVo) {
        return new Specification<AccidentEntity>() {

            public Predicate toPredicate(Root<AccidentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preCarNo = cb.like(root.get("carNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preCarName = cb.like(root.get("carName"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverNo = cb.like(root.get("driverNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverName = cb.like(root.get("driverName"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preCarNo, preCarName, preDriverNo, preDriverName));
                }
                if (null != qVo.getAccidentType() && !qVo.getAccidentType().isEmpty()) {
                    predicate.add(cb.equal(root.get("accidentType"), qVo.getAccidentType()));
                }
                if (null != qVo.getAccidentTypeCode() && !qVo.getAccidentTypeCode().isEmpty()) {
                    predicate.add(cb.equal(root.get("accidentTypeCode"), qVo.getAccidentTypeCode()));
                }
                if (null != qVo.getStatus() && qVo.getStatus().length > 0) {
                    Predicate[] status = new Predicate[qVo.getStatus().length];
                    int index = 0;
                    for (EnumStatus orderStatus : qVo.getStatus()) {
                        status[index] = cb.equal(root.get("status"), orderStatus);
                        index++;
                    }
                    predicate.add(cb.or(status));
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
    public AccidentVO addOrEdit(IUAccidentVO iuVo) {
        return AccidentVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public AccidentEntity insertOrUpdate(AccidentEntity entity) {
        try {
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return entity;
    }

    public AccidentEntity insertOrUpdate(IUAccidentVO iuVo) {
        AccidentEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setCarId(iuVo.getCarId());
                entity.setCarName(iuVo.getCarName());
                entity.setCarNo(iuVo.getCarNo());
                entity.setDriverId(iuVo.getDriverId());
                entity.setDriverName(iuVo.getDriverName());
                entity.setDriverNo(iuVo.getDriverNo());

                entity.setAccidentType(iuVo.getAccidentType());
                entity.setAccidentTypeCode(iuVo.getAccidentTypeCode());
                entity.setDetails(iuVo.getDetails());
                entity.setConfirmBy(iuVo.getConfirmBy());

                entity.setAccidentTime(iuVo.getAccidentTime());
                entity.setNoticeTime(iuVo.getNoticeTime());
                entity.setImages(iuVo.getImages());

                entity.setRepairTime(iuVo.getRepairTime());
                entity.setRepairBy(iuVo.getRepairBy());

                entity.setProportion(iuVo.getProportion());
                entity.setResponsible(iuVo.getResponsible());

                entity.setAmount(iuVo.getAmount());
                entity.setInsuranceAmount(iuVo.getInsuranceAmount());
                entity.setCompanyAmount(iuVo.getCompanyAmount());
                entity.setDriverAmount(iuVo.getDriverAmount());

                entity.setFinanceNoticeTime(iuVo.getFinanceNoticeTime());
                entity.setFinanceAmount(iuVo.getFinanceAmount());
                entity.setFinanceBy(iuVo.getFinanceBy());
                entity.setFinanceByName(iuVo.getFinanceByName());
                entity.setFinanceTime(iuVo.getFinanceTime());

                entity.setStatus(EnumStatus.Filling);
                entity.setRemark(iuVo.getRemark());

            } else {
                // 追加
                entity = AccidentVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }

            entity = repository.save(entity);

        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public AccidentVO setAccident(IUAccidentVO iuVo) {
        AccidentEntity entity = null;
        try {

            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());

                entity.setCarId(iuVo.getCarId());
                entity.setCarName(iuVo.getCarName());
                entity.setCarNo(iuVo.getCarNo());
                entity.setDriverId(iuVo.getDriverId());
                entity.setDriverName(iuVo.getDriverName());
                entity.setDriverNo(iuVo.getDriverNo());

                entity.setAccidentType(iuVo.getAccidentType());
                entity.setAccidentTypeCode(iuVo.getAccidentTypeCode());
                entity.setDetails(iuVo.getDetails());
                entity.setConfirmBy(iuVo.getConfirmBy());

                entity.setAccidentTime(iuVo.getAccidentTime());
                entity.setNoticeTime(iuVo.getNoticeTime());
                entity.setImages(iuVo.getImages());

                entity.setRepairTime(iuVo.getRepairTime());
                entity.setRepairBy(iuVo.getRepairBy());

                entity.setProportion(iuVo.getProportion());
                entity.setResponsible(iuVo.getResponsible());

                entity.setRemark(iuVo.getRemark());
            } else {
                // 追加
                entity = AccidentVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }
            entity.setStatus(EnumStatus.Filling);
            entity = repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return AccidentVOEntityMapStruct.INSTANCE.toVO(entity);
    }

    public AccidentVO setAmount(IUAccidentVO iuVo) {
        AccidentEntity entity = null;
        try {
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {

                entity = getEntity(iuVo.getId());

                entity.setAmount(iuVo.getAmount());
                entity.setInsuranceAmount(iuVo.getInsuranceAmount());
                entity.setCompanyAmount(iuVo.getCompanyAmount());
                entity.setDriverAmount(iuVo.getDriverAmount());

                entity.setFinanceNoticeTime(iuVo.getFinanceNoticeTime());
                entity.setFinanceAmount(iuVo.getFinanceAmount());

                entity.setStatus(EnumStatus.Waiting);

                // 更新 車両ステータス
                carService.resetStatus(entity.getCarId(), EnumCarStatus.Repair);

                // 先删除日程表
                scheduleService.deletePhysicsByAccidentId(entity.getId(), EnumTargetType.Car);

                // 注文 車両 スケジュール
                List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
                Date sd = entity.getNoticeTime();
                Date ed = entity.getRepairTime();
                Calendar sCalendar = Calendar.getInstance();
                Calendar dCalendar = Calendar.getInstance();
                sCalendar.setTime(sd);
                dCalendar.setTime(ed);
                long diff = dCalendar.getTimeInMillis() - sCalendar.getTimeInMillis();
                long days = diff / (1000 * 60 * 60 * 24);
                Calendar calendar = Calendar.getInstance();
                ScheduleEntity scheduleEntity = null;
                int index = 0;
                do {
                    scheduleEntity = new ScheduleEntity();
                    scheduleEntity.setTargetId(entity.getCarId());
                    scheduleEntity.setTargetNo(entity.getCarNo());
                    scheduleEntity.setTargetName(entity.getCarName());
                    scheduleEntity.setActionId(entity.getId());
                    scheduleEntity.setTargetType(EnumTargetType.Car);
                    scheduleEntity.setActionType(EnumActionType.Repair);
                    scheduleEntity
                            .setRemark(String.format("%S", "事故修理"));
                    calendar.setTime(sd);
                    calendar.add(Calendar.DATE, index);
                    scheduleEntity.setWorkDate(sf.format(calendar.getTime()));
                    scheduleEntity.setWorkTime(calendar.getTime());
                    scheduleEntities.add(scheduleEntity);
                    index++;
                } while (index <= days);
                scheduleService.saveAll(scheduleEntities);
                entity = repository.save(entity);
            }
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return AccidentVOEntityMapStruct.INSTANCE.toVO(entity);
    }

    public void export(QueryAccidentVO qVo) {
        String screenName = "事故一覧";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "車両番号", "carNo" },
                { "事故性質", "accidentType" },
                { "事故日時", "accidentTime" },
                { "事故者", "driverName" },
                { "事故詳細", "details" },
                { "事故写真", "images" },
                { "確認者", "confirmBy" },
                { "主责任方", "responsible" },
                { "事故比例", "proportion" },
                { "修理業者", "repairBy" },
                { "修理終了日時", "repairTime" },
                { "総額(円)", "amount" },
                { "会社負担(円)", "companyAmount" },
                { "保険負担(円)", "insuranceAmount" },
                { "個人負担(円)", "driverAmount" },
                { "事故通知日時", "noticeTime" },
                { "財務通知日時", "financeNoticeTime" },
                { "精算日時", "financeTime" },
                { "精算者", "financeByName" },
                { "ステータス", "status" },
        };

        Specification<AccidentEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<AccidentEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                AccidentVOEntityMapStruct.INSTANCE.toVOs(list));
    }

    public Boolean audit(String id) {
        try {
            AccidentEntity entity = getEntity(id);
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
            AccidentEntity entity = getEntity(id);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);

            // 更新 車両ステータス
            carService.resetStatus(entity.getCarId(), EnumCarStatus.Working);

            // 先删除日程表
            scheduleService.deletePhysicsByAccidentId(entity.getId(), EnumTargetType.Car);

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
