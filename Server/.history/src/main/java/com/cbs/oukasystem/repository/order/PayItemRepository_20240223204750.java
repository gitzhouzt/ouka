package com.cbs.oukasystem.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.entity.order.PayRecordEntity;

@Repository
public interface PayRecordRepository
        extends JpaRepository<PayRecordEntity, String>, JpaSpecificationExecutor<PayRecordEntity> {

    List<PayRecordEntity> findAllByOrderIdAndFinanceType(String orderId, EnumFinanceType financeType);
}