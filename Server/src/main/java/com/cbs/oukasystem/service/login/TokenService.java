package com.cbs.oukasystem.service.login;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.login.TokenEntity;
import com.cbs.oukasystem.mapstruct.login.TokenVOEntityMapStruct;
import com.cbs.oukasystem.repository.login.TokenRepository;
import com.cbs.oukasystem.vo.out.login.TokenVO;

@Service
@Transactional
public class TokenService {

    String KEY = "TOKEN";

    @Autowired
    private TokenRepository repository;

    /*
     * TOKENを得る
     */

    public TokenVO getById(String id) {
        return TokenVOEntityMapStruct.INSTANCE.toTokenVO(getEntity(id));
    }

    public TokenEntity getEntity(String id) {
        Optional<TokenEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id : " + id);
        }
        return optional.get();
    }

    public TokenEntity findOne(Specification<TokenEntity> spec) {
        Optional<TokenEntity> uOptional = repository.findOne(spec);
        TokenEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public TokenEntity findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public TokenEntity findByToken(String token) {
        return repository.findByToken(token);
    }

    /*
     * 追加 or 編集
     */

    public TokenEntity insertOrUpdate(TokenEntity newToken) {
        TokenEntity entity = null;
        try {
            entity = repository.save(newToken);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    /*
     * 物理削除
     */

    public Boolean deletePhysics(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deletePhysicsByUserId(String userId) {
        try {
            repository.deleteByUserId(userId);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
