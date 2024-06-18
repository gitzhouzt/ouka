package com.cbs.oukasystem.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.order.OrderGoodsEntity;

@Repository
public interface OrderGoodsRepository
        extends JpaRepository<OrderGoodsEntity, String>, JpaSpecificationExecutor<OrderGoodsEntity> {

    List<OrderGoodsEntity> findAllByOrderId(String orderId);

}