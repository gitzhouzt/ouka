package com.cbs.oukasystem.repository.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.sys.MenuEntity;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String>, JpaSpecificationExecutor<MenuEntity> {

}
