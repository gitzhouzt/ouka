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
                        + " or s.target_name like %:keyword%) ORDER BY  workTime asc")
        List<Map> queryBySchedule(@Param("keyword") String keyword,
                        @Param("targetType") int targetType,
                        @Param("workDate") String workDate);

        @Query(nativeQuery = true, value = "select count(*) from (select target_id from schedule_master s "
                        + " where s.target_type = :targetType "
                        + " and s.work_date like :workDate% "
                        + " and if(:keyword is null or :keyword = '',1=1, s.target_no like %:keyword% "
                        + " or s.target_name like %:keyword%) group by target_id) as temptable")
        long countBySchedule(@Param("keyword") String keyword,
                        @Param("targetType") int targetType,
                        @Param("workDate") String workDate);

        @Query(nativeQuery = true, value = "select s.id as id,  target_id as targetId,"
                        + "c.car_no as targetNo,target_type as targetType,action_id as actionId,"
                        + "action_type as actionType,work_date as workDate,work_time as workTime,s.remark"
                        + " from schedule_master s "
                        + " right join car_master c on s.target_id = c.id "
                        + " where s.work_date  = :workDate "
                        + " ORDER BY  workTime asc")
        List<Map> queryWorkByCar(@Param("workDate") String workDate);

        @Query(nativeQuery = true, value = "select s.id as id, target_id as targetId,"
                        + "u.user_name as targetNo,target_type as targetType,action_id as actionId,"
                        + "action_type as actionType,work_date as workDate,work_time as workTime,s.remark"
                        + " from schedule_master s "
                        + " right join user_master u on s.target_id = u.id and u.user_roles like '%Driver%' "
                        + " where s.work_date  = :workDate "
                        + " ORDER BY  workTime asc")
        List<Map> queryWorkByDriver(@Param("workDate") String workDate);

        void deleteByActionIdAndTargetTypeAndActionType(String actionId, EnumTargetType targetType,
                        EnumActionType actionType);

        void deleteByActionIdAndActionType(String actionId,
                        EnumActionType actionType);
}