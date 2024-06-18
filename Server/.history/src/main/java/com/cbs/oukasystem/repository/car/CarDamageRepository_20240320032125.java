package com.cbs.oukasystem.repository.car;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.car.CarDamageEntity;
import com.cbs.oukasystem.entity.finance.PayRecordEntity;

@Repository
public interface CarDamageRepository
        extends JpaRepository<CarDamageEntity, String>, JpaSpecificationExecutor<CarDamageEntity> {

    List<PayRecordEntity> findAllByOrderIdAndPayMethodCode(String orderId, String payMethodCode);
}