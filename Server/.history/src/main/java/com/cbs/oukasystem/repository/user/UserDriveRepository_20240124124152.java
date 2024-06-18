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

    @Query(value = "select user_id,driver_license,expiry_date from user_drive" +
            " where DATEDIFF(expiry_date,CURRENT_DATE) <= 7 ", nativeQuery = true)
    List<Map> noticeOnExpiryDate7Days();
}