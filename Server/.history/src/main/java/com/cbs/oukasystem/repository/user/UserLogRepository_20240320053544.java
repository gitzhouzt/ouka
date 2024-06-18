package com.cbs.oukasystem.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.finance.PayRecordEntity;
import com.cbs.oukasystem.entity.user.UserLogEntity;

@Repository
public interface UserLogRepository
        extends JpaRepository<UserLogEntity, String>, JpaSpecificationExecutor<UserLogEntity> {

    List<UserLogEntity> findAllByOrderIdAndPayMethodCode(String orderId, String payMethodCode);

}