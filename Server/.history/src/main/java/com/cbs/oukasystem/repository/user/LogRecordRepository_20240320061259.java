package com.cbs.oukasystem.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.user.LogRecordEntity;

@Repository
public interface LogRecordRepository
        extends JpaRepository<LogRecordEntity, String>, JpaSpecificationExecutor<LogRecordEntity> {

    List<LogRecordEntity> findByUserIdAndLogId(String userId, String logId);

}