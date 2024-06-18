package com.cbs.oukasystem.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.user.UserRestEntity;

@Repository
public interface UserRestRepository
                extends JpaRepository<UserRestEntity, String>, JpaSpecificationExecutor<UserRestEntity> {

}