package com.cbs.oukasystem.repository.finance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.finance.AdvanceEntity;

@Repository
public interface AdvanceRepository
        extends JpaRepository<AdvanceEntity, String>, JpaSpecificationExecutor<AdvanceEntity> {

    List<AdvanceEntity> findByOrderNo(String orderNo);

    @Modifying
    @Query(nativeQuery = true, value = "update finance_advance set status = :status, status_name= :statusName where id in(:ids)")
    void settlementByIds(@Param("ids") List<String> ids,
            @Param("status") int status, @Param("statusName") String statusName);
}