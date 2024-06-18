package com.cbs.oukasystem.repository.car;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.car.CarEntity;

@Repository
public interface CarRepository
                extends JpaRepository<CarEntity, String>, JpaSpecificationExecutor<CarEntity> {

        @Query(value = "SELECT id, c.car_no as carNo,c.car_type as carType, c.car_seat as carSeat,c.car_photo as carPhoto,"
                        + " c.plate_num as plateNum FROM order_master as o "
                        + " join car_master c on c.id = o.car_id "
                        + " where o.order_status in (3,4,5,6,7) "
                        + " and o.driver_id = :driverId"
                        + " and DATEDIFF(start_time,CURRENT_DATE) <= 1 and  DATEDIFF(start_time,CURRENT_DATE) >= 0 GROUP BY o.car_id ", nativeQuery = true)
        List<Map<String, String>> queryTodayCars(@Param("driverId") String driverId);

}