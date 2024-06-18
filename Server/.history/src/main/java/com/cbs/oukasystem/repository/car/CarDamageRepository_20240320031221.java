package com.cbs.oukasystem.repository.car;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.car.CarDamageDEntity;

@Repository
public interface CarDamageRepository
                extends JpaRepository<CarDamageDEntity, String>, JpaSpecificationExecutor<CarDamageDEntity> {

}