package com.cbs.oukasystem.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.order.OrderFileEntity;

@Repository
public interface OrderFileRepository
        extends JpaRepository<OrderFileEntity, String>, JpaSpecificationExecutor<OrderFileEntity> {

    List<OrderFileEntity> findAllByOrderIdAndShare(String orderId, Boolean share);
}