package com.cbs.oukasystem.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.finance.SettlementEntity;

@Repository
public interface SettlementRepository
        extends JpaRepository<SettlementEntity, String>, JpaSpecificationExecutor<SettlementEntity> {

}