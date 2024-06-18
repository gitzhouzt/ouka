package com.cbs.oukasystem.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.user.UserDriveEntity;

@Repository
public interface UserDriveRepository
        extends JpaRepository<UserDriveEntity, String>, JpaSpecificationExecutor<UserDriveEntity> {

    UserDriveEntity findByUserId(String findByUserId);
}