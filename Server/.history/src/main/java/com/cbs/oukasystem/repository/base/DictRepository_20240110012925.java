package com.cbs.oukasystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.DictEntity;

@Repository
public interface DictRepository extends JpaRepository<DictEntity, String>, JpaSpecificationExecutor<DictEntity> {

}
