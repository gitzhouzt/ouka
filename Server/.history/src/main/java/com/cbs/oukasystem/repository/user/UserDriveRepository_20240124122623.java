package com.cbs.oukasystem.repository.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.user.UserDriveEntity;

@Repository
public interface UserDriveRepository
        extends JpaRepository<UserDriveEntity, String>, JpaSpecificationExecutor<UserDriveEntity> {

    UserDriveEntity findByUserId(String findByUserId);

    @Query(value = "select o.id,o.customer_email from order_master o " +
            " where order_status = 2 " +
            " and DATEDIFF(start_time,CURRENT_DATE) = 3 ", nativeQuery = true)
    List<Map> queryBefore3Days();
}