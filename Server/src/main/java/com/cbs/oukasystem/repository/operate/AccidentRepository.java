package com.cbs.oukasystem.repository.operate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.operate.AccidentEntity;

@Repository
public interface AccidentRepository
        extends JpaRepository<AccidentEntity, String>, JpaSpecificationExecutor<AccidentEntity> {

}