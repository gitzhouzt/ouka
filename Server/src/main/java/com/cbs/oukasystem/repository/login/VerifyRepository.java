package com.cbs.oukasystem.repository.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.entity.login.VerifyEntity;

@Repository
public interface VerifyRepository extends JpaRepository<VerifyEntity, String>, JpaSpecificationExecutor<VerifyEntity> {

    VerifyEntity findByContentAndVerifyTypeAndVerifyMethod(String content, EnumVerifyType veiVerifyType,
            EnumVerifyMethod verifyMethod);

    @Modifying
    @Query(value = "delete from sys_verify where expiration is not null and FROM_UNIXTIME(expiration/1000,'%Y-%m-%d %H:%i:%s') < CURRENT_DATE", nativeQuery = true)
    void deleteByExpiration();
}
