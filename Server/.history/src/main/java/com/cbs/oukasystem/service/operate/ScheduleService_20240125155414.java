package com.cbs.oukasystem.service.operate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.mapstruct.operate.ScheduleVOEntityMapStruct;
import com.cbs.oukasystem.repository.operate.ScheduleRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.operate.IUScheduleVO;
import com.cbs.oukasystem.vo.in.operate.QueryScheduleVO;
import com.cbs.oukasystem.vo.out.operate.ScheduleVO;

@Service
@Transactional
public class ScheduleService {

    String KEY = "スケジュール";

    @Autowired
    private ScheduleRepository repository;

    /*
     * idによって会社を得る
     */

    public ScheduleVO getById(String id) {
        return ScheduleVOEntityMapStruct.INSTANCE.toScheduleVO(getEntity(id));
    }

    /*
     * 会社を得る
     */
    public ScheduleEntity getEntity(String id) {
        Optional<ScheduleEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY.getErrorMsg(), KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての会社
     */

    public List<ScheduleVO> getAll() {
        List<ScheduleEntity> entities = repository.findAll();
        List<ScheduleVO> vos = ScheduleVOEntityMapStruct.INSTANCE.toScheduleVOs(entities);
        return vos;
    }

    public List<ScheduleEntity> findAll(Specification<ScheduleEntity> spec) {
        return repository.findAll(spec);
    }

    public ListVO<ScheduleVO> getPagesBySchedule(QueryScheduleVO qVo) {
        int target_type = qVo.getTargetType() == EnumTargetType.Car ? 1 : 0;
        List<Map> pages = repository.queryBySchedule(qVo.getKeyword(), target_type, qVo.getWorkDate(),
                (qVo.getPage() - 1) * qVo.getPageSize(),
                qVo.getPageSize());
        Object data = pages;
        List<ScheduleVO> list = (List<ScheduleVO>) data;
        ListVO<ScheduleVO> listDto = new ListVO<>();
        long itemCnt = repository.countBySchedule(qVo.getKeyword(), target_type, qVo.getWorkDate());
        int pageCnt = (int) itemCnt / qVo.getPageSize() + (itemCnt % qVo.getPageSize() == 0 ? 0 : 1);
        listDto.setItemCount(itemCnt);
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pageCnt);
        listDto.setList(list);
        return listDto;
    }

    /*
     * 追加 or 編集
     */

    public ScheduleEntity insertOrUpdate(IUScheduleVO iuVo) {
        ScheduleEntity entity = null;
        try {
            entity = ScheduleVOEntityMapStruct.INSTANCE.toScheduleEntity(iuVo);
            entity = repository.save(entity);
        } catch (Exception e) {

            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    public Boolean saveAll(List<ScheduleEntity> scheduleEntities) {
        try {
            repository.saveAll(scheduleEntities);
        } catch (Exception e) {

            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
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

    public Boolean deletePhysicsByOrderId(String orderId, EnumTargetType targetType) {
        try {
            repository.deleteByActionIdAndTargetTypeAndActionType(orderId, targetType,
                    EnumActionType.Order);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysicsByOrderId(String orderId) {
        try {
            repository.deleteByActionIdAndActionType(orderId,
                    EnumActionType.Order);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysicsByRepairId(String repairId, EnumTargetType targetType) {
        try {
            repository.deleteByActionIdAndTargetTypeAndActionType(repairId, targetType,
                    EnumActionType.Repair);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysicsByCheckId(String checkId, EnumTargetType targetType) {
        try {
            repository.deleteByActionIdAndTargetTypeAndActionType(checkId, targetType,
                    EnumActionType.Check);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysicsByRestId(String restId, EnumTargetType targetType) {
        try {
            repository.deleteByActionIdAndTargetTypeAndActionType(restId, targetType,
                    EnumActionType.Rest);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
