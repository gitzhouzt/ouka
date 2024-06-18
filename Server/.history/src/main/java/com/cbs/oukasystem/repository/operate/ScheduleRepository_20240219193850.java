package com.cbs.oukasystem.repository.operate;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ScheduleRepository
                extends JpaRepository<ScheduleEntity, String>, JpaSpecificationExecutor<ScheduleEntity> {

        @Query(nativeQuery = true, value = "select target_id as targetId,target_no as targetNo,"
                        + "target_name as targetName,target_type as targetType,action_id as actionId,"
                        + "action_type as actionType,work_date as workDate,work_time as workTime,remark"
                        + " from schedule_master s "
                        + " where s.target_type = :targetType "
                        + " and s.work_date like :workDate% "
                        + " and if(:keyword is null or :keyword = '',1=1, s.target_no like %:keyword% "
                        + " or s.target_name like %:keyword%) desc by workTime")
        List<Map> queryBySchedule(@Param("keyword") String keyword,
                        @Param("targetType") int targetType,
                        @Param("workDate") String workDate);

        @Query(nativeQuery = true, value = "select count(*) from schedule_master s "
                        + " where s.target_type = :targetType "
                        + " and s.work_date like :workDate% "
                        + " and if(:keyword is null or :keyword = '',1=1, s.target_no like %:keyword% "
                        + " or s.target_name like %:keyword%)")
        long countBySchedule(@Param("keyword") String keyword,
                        @Param("targetType") int targetType,
                        @Param("workDate") String workDate);

        void deleteByActionIdAndTargetTypeAndActionType(String actionId, EnumTargetType targetType,
                        EnumActionType actionType);

        void deleteByActionIdAndActionType(String actionId,
                        EnumActionType actionType);
}