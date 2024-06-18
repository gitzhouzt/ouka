package com.cbs.oukasystem.repository.finance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.finance.AdvanceEntity;

@Repository
public interface AdvanceRepository
        extends JpaRepository<AdvanceEntity, String>, JpaSpecificationExecutor<AdvanceEntity> {

    List<AdvanceEntity> findByOrderNo(String orderNo);
}