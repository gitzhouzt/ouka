package com.cbs.oukasystem.repository.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.car.CarRepairEntity;

@Repository
public interface CarRepairRepository
        extends JpaRepository<CarRepairEntity, String>, JpaSpecificationExecutor<CarRepairEntity> {

}