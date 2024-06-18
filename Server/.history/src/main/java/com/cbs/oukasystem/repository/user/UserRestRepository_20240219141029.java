package com.cbs.oukasystem.repository.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.user.UserRestEntity;

@Repository
public interface UserRestRepository
        extends JpaRepository<UserRestEntity, String>, JpaSpecificationExecutor<UserRestEntity> {

    @Query(nativeQuery = true, value = "select target_id as targetId,target_no as targetNo,"
            + "target_name as targetName,target_type as targetType,action_id as actionId,"
            + "action_type as actionType,work_date as workDate,work_time as workTime,remark"
            + " from schedule_master s "
            + " where s.target_type = :targetType "
            + " and s.work_date like :workDate% "
            + " and if(:keyword is null or :keyword = '',1=1, s.target_no like %:keyword% "
            + " or s.target_name like %:keyword%)")
    List<Map> queryBySchedule(@Param("keyword") String keyword,
            @Param("workDate") String workDate);

    @Query(nativeQuery = true, value = "select count(*) from schedule_master s "
            + " where s.target_type = :targetType "
            + " and s.work_date like :workDate% "
            + " and if(:keyword is null or :keyword = '',1=1, s.target_no like %:keyword% "
            + " or s.target_name like %:keyword%)")
    long countBySchedule(@Param("keyword") String keyword,
            @Param("workDate") String workDate);

}