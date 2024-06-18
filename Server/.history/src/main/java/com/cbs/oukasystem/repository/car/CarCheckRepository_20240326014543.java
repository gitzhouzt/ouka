package com.cbs.oukasystem.repository.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.car.CarCheckEntity;

@Repository
public interface CarCheckRepository
        extends JpaRepository<CarCheckEntity, String>, JpaSpecificationExecutor<CarCheckEntity> {

    void deleteByCarId(String carId);
}