package com.cbs.oukasystem.repository.finance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.common.BusinessEnum.EnumFinanceType;
import com.cbs.oukasystem.entity.finance.PayRecordEntity;

@Repository
public interface PayRecordRepository
                extends JpaRepository<PayRecordEntity, String>, JpaSpecificationExecutor<PayRecordEntity> {

        List<PayRecordEntity> findAllByOrderIdAndFinanceType(String orderId, EnumFinanceType financeType);

        @Modifying
        @Query(nativeQuery = true, value = "update finance_pay_record set pay_method = :payMethod "
                        + ",pay_method_code = :payMethodCode"
                        + ",bank = :bank, status = 2 where id in(:ids)")
        void settlementByIds(@Param("ids") List<String> ids, @Param("payMethod") String payMethod,
                        @Param("payMethodCode") String payMethodCode, @Param("bank") String bank);

}