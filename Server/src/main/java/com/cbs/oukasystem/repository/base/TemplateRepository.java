package com.cbs.oukasystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.TemplateEntity;

@Repository
public interface TemplateRepository
        extends JpaRepository<TemplateEntity, String>, JpaSpecificationExecutor<TemplateEntity> {

}