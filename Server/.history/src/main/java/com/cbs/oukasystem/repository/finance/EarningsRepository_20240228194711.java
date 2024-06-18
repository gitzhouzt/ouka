package com.cbs.oukasystem.repository.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.finance.EarningsEntity;

@Repository
public interface EarningsRepository
                extends JpaRepository<EarningsEntity, String>, JpaSpecificationExecutor<EarningsEntity> {

}