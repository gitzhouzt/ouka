package com.cbs.oukasystem.repository.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.user.UserTrainEntity;

@Repository
public interface UserTrainRepository
                extends JpaRepository<UserTrainEntity, String>, JpaSpecificationExecutor<UserTrainEntity> {

        UserTrainEntity findByUserId(String findByUserId);

        @Query(value = "select id,user_id,driver_license,expiry_date from user_drive" +
                        " where DATEDIFF(expiry_date,CURRENT_DATE) <= 7 ", nativeQuery = true)
        List<Map> noticeOnExpiryDate7Days();
}