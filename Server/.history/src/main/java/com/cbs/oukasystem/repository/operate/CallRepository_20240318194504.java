package com.cbs.oukasystem.repository.operate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.operate.CallEntity;

@Repository
public interface CallRepository
                extends JpaRepository<CallEntity, String>, JpaSpecificationExecutor<CallEntity> {



                    @Query(value = "",nativeQuery = true)

}