package com.cbs.oukasystem.repository.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.DictEntity;

@Repository
public interface DictRepository extends JpaRepository<DictEntity, String>, JpaSpecificationExecutor<DictEntity> {

    @Query(nativeQuery = true, value = "select u.* from user_master u  "
            + " join operate_call c on u.id <> c.driver_id "
            + " where u.user_roles like '%Driver%' and u.status = 1 "
            + " and u.is_audit = 1 and u.is_delete = 0 "
            + " order by u.update_time desc "
            + " limit :page,:pageSize ")
    List<Map> queryByCallDriver(@Param("page") int page, @Param("pageSize") int pageSize);
}