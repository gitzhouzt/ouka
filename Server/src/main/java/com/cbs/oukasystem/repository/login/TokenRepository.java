package com.cbs.oukasystem.repository.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.login.TokenEntity;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, String>, JpaSpecificationExecutor<TokenEntity> {

    TokenEntity findByUserId(String userId);

    TokenEntity findByToken(String token);

    void deleteByUserId(String userId);
}