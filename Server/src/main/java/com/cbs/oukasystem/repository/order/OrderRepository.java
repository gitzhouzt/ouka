package com.cbs.oukasystem.repository.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.order.OrderEntity;

@Repository
public interface OrderRepository
                extends JpaRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {

        @Query(value = "select o.id,o.customer_email from order_master o " +
                        " where order_status = 2 " +
                        " and DATEDIFF(start_time,CURRENT_DATE) = 3 ", nativeQuery = true)
        List<Map<String, String>> queryBefore3Days();

        @Query(value = "select o.id,o.customer_email from order_master o " +
                        " where order_status = 2 " +
                        " and DATEDIFF(start_time,CURRENT_DATE) = 1 ", nativeQuery = true)
        List<Map<String, String>> queryBefore1Days();

        @Query(value = "SELECT start_time as startTime,car_id as carId,car_no as carNo,order_status as orderStatus,order_no as orderNo FROM order_master as o "
                        + " where order_status in (3,4,5,6,7) "
                        + " and driver_id = :driverId"
                        + " and DATEDIFF(start_time,CURRENT_DATE) <= 1 and  DATEDIFF(start_time,CURRENT_DATE) >= 0", nativeQuery = true)
        List<Map<String, String>> queryTodayOrders(@Param("driverId") String driverId);

        @Modifying
        @Query(value = "update OrderEntity set orderStatus = 3,orderStatusName = 'チェック待ち' where orderStatus = 2")
        void send();

        @Modifying
        @Query(value = "update OrderEntity set isLodgingTips = :isLodgingTips where id = :id")
        void isLodgingTips(@Param("id") String id, @Param("isLodgingTips") Boolean isLodgingTips);

        @Query(value = "select count(*) from order_master o " +
                        " where DATE(create_time) = CURDATE()", nativeQuery = true)
        Long countByToday();

}