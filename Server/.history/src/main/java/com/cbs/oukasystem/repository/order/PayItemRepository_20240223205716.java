package com.cbs.oukasystem.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.entity.order.PayItemEntity;

@Repository
public interface PayItemRepository
        extends JpaRepository<PayItemEntity, String>, JpaSpecificationExecutor<PayItemEntity> {

    List<PayItemEntity> findAllByOrderId(String orderId);
}