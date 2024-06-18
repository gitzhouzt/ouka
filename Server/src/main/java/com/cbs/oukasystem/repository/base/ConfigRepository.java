package com.cbs.oukasystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.ConfigEntity;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, String>, JpaSpecificationExecutor<ConfigEntity> {

}