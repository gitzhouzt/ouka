package com.cbs.oukasystem.service.login;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.CommonEnum.EnumUserRole;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumLoginCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumPwdCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumResponseCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.login.TokenEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.login.TokenVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.user.UserVOEntityMapStruct;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.in.login.LoginVO;
import com.cbs.oukasystem.vo.out.login.LoginUserVO;
import com.cbs.oukasystem.vo.out.login.TokenVO;
import com.cbs.oukasystem.vo.out.user.UserVO;

@Service
@Transactional
public class LoginService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    /*
     * ログイン && ユーザー情報 && トークンを得る
     */

    public LoginUserVO login(LoginVO loginVO) {
        List<UserEntity> list = userService
                .findAll(userService.isExist(loginVO.getLoginName(), ""));
        if (list.isEmpty()) {
            throw new BaseException(EnumDataCheck.USER_NOT_EXIST);
        }
        UserEntity user = userService.findOne(loginCheck(loginVO));
        if (null == user) {
            throw new BaseException(EnumPwdCheck.INPUT_ERROR);
        }
        if (!user.getIsAudit()) {
            throw new BaseException(EnumLoginCheck.USER_DISABLED);
        }
        if (loginVO.getIsAdmin()) {
            if (user.getUserRoles().contains(EnumUserRole.Customer.getCode())) {
                throw new BaseException(EnumResponseCheck.USER_NOT_ALLOWED);
            }
        } else {
            if (user.getUserRoles().contains(EnumUserRole.Driver.getCode())) {
                throw new BaseException(EnumResponseCheck.USER_NOT_ALLOWED);
            }
        }
        UserVO userVO = UserVOEntityMapStruct.INSTANCE.toVO(user);
        /*
         * token
         */
        TokenEntity token = tokenService.findByUserId(user.getId());
        token = LoginUtils.sign(user.getId(), token);
        token.setUserId(user.getId());
        token = tokenService.insertOrUpdate(token);
        LoginUtils.initLoginToken(token);
        LoginUtils.initLoginUserId(user.getId());
        LoginUserVO loginUserVO = new LoginUserVO(userVO,
                TokenVOEntityMapStruct.INSTANCE.toTokenVO(token));
        return loginUserVO;
    }

    /*
     * ログアウト
     */

    public Boolean logout() {
        try {
            tokenService.deletePhysicsByUserId(LoginUtils.getLoginUserId());
            LoginUtils.removeLoginUserId();
            LoginUtils.removeLoginToken();
        } catch (Exception e) {
            throw new BaseException(EnumResponseCheck.SYSTEM_ERROR, e.getMessage());
        }
        return true;
    }

    /*
     * トークンを更新
     */
    public TokenVO updateToken(String tokenRefresh) {
        TokenEntity tokenEntity = null;
        try {
            tokenEntity = tokenService.findOne(searchToken(tokenRefresh));
            if (null == tokenEntity) {
                throw new BaseException(EnumLoginCheck.TOKEN_EXPIRED);
            }
            tokenEntity = LoginUtils.sign(tokenEntity.getUserId(), tokenEntity);
            tokenService.insertOrUpdate(tokenEntity);
        } catch (Exception e) {
            throw new BaseException(EnumLoginCheck.TOKEN_ERROR, e.getMessage());
        }
        return TokenVOEntityMapStruct.INSTANCE.toTokenVO(tokenEntity);
    }

    /*
     * ログイン検証 ユーザーもメールもログイン可能
     * email and name
     */
    public Specification<UserEntity> loginCheck(LoginVO loginVO) {
        return new Specification<UserEntity>() {

            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query,
                    CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preLoginName = cb.equal(root.get("loginName"),
                        loginVO.getLoginName());
                        Predicate preLoginName = cb.equal(root.get("loginName"),
                        loginVO.getLoginName());
                Predicate preUserPass = cb.equal(root.get("loginPass"),
                        CommonUtils.toSHA256(loginVO.getLoginPass()));
                predicate.add(preLoginName);
                predicate.add(preUserPass);

                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * トークン検索
     */
    public Specification<TokenEntity> searchToken(String tokenRefresh) {
        return new Specification<TokenEntity>() {

            public Predicate toPredicate(Root<TokenEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (!tokenRefresh.isEmpty()) {
                    Predicate preTokenRefresh = cb.like(root.get("tokenRefresh"), tokenRefresh);
                    predicate.add(cb.or(preTokenRefresh));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

}
