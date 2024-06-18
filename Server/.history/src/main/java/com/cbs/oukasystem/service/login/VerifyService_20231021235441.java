package com.cbs.oukasystem.service.login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.EmailUtils;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyMethod;
import com.cbs.oukasystem.common.BusinessEnum.EnumVerifyType;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumVerifyCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.login.VerifyEntity;
import com.cbs.oukasystem.mapstruct.login.VerifyVOEntityMapStruct;
import com.cbs.oukasystem.repository.login.VerifyRepository;
import com.cbs.oukasystem.vo.in.login.IUVerifyVO;
import com.cbs.oukasystem.vo.out.login.VerifyVO;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@Transactional
public class VerifyService {

    String KEY = "認証コード";

    @Autowired
    private VerifyRepository repository;

    @Autowired
    private EmailUtils emailUtils;

    /*
     * idによって認証を得る
     */

    public VerifyVO getById(String id) {
        return VerifyVOEntityMapStruct.INSTANCE.toVerifyVO(getEntity(id));
    }

    public VerifyEntity findByContent(String content, EnumVerifyType verifyType, EnumVerifyMethod verifyMethod) {
        return repository.findByContentAndVerifyTypeAndVerifyMethod(content, verifyType, verifyMethod);
    }

    /*
     * 全ての認証
     */

    public List<VerifyVO> getAll() {
        List<VerifyEntity> menus = repository.findAll();
        return VerifyVOEntityMapStruct.INSTANCE.toVerifyVOs(menus);
    }

    /*
     * 追加 or 編集
     */

    public Boolean sendVerifyCode(IUVerifyVO iVo) {
        String code = "";
        VerifyEntity entity = null;
        Calendar calendar = Calendar.getInstance();
        try {
            List<VerifyEntity> list = repository
                    .findAll(isExist(iVo));
            if (!list.isEmpty()) {
                entity = list.get(0);
                Date expiration = new Date(entity.getExpiration());
                Date now = new Date();
                // 过期 重新生成验证码
                if (now.after(expiration)) {
                    code = CommonUtils.getSeqNum();
                    entity.setCode(code);
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, 10);
                    entity.setExpiration(calendar.getTime().getTime());
                    if (iVo.getVerifyMethod() == EnumVerifyMethod.EMAIL) {
                        sendVerifyCodeByEmail(iVo.getContent(), code);
                    } else if (iVo.getVerifyMethod() == EnumVerifyMethod.PHONE) {
                        sendVerifyCodeByPhone(iVo.getContent(), code);
                    }
                    repository.save(entity);
                } else {
                    // 没过期 提示
                    throw new BaseException(EnumVerifyCheck.EXPIRED_NOT);
                    // code = entity.getCode();
                    // if (iVo.getVerifyMethod() == EnumVerifyMethod.EMAIL) {
                    // sendVerifyCodeByEmail(iVo.getContent(), code);
                    // } else if (iVo.getVerifyMethod() == EnumVerifyMethod.PHONE) {
                    // sendVerifyCodeByPhone(iVo.getContent(), code);
                    // }
                }
            } else {
                entity = VerifyVOEntityMapStruct.INSTANCE.toVerifyEntity(iVo);
                code = CommonUtils.getSeqNum();
                entity.setCode(code);
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, 10);
                entity.setExpiration(calendar.getTime().getTime());
                repository.save(entity);
                if (iVo.getVerifyMethod() == EnumVerifyMethod.EMAIL) {
                    sendVerifyCodeByEmail(iVo.getContent(), code);
                } else if (iVo.getVerifyMethod() == EnumVerifyMethod.PHONE) {
                    sendVerifyCodeByPhone(iVo.getContent(), code);
                }
            }
        } catch (Exception e) {
            throw new BaseException(KEY + EnumIOUCheck.ERROR.getErrorMsg(), e.getMessage());
        }
        return true;
    }

    // EMAIL メール送信
    private void sendVerifyCodeByEmail(String to, String code) {
        emailUtils.sendEmailVerifyCodeToUser(to, code);
    }

    private void sendVerifyCodeByPhone(String to, String code) {
        // TODO 发送短信
    }

    /*
     * .
     * 削除
     */

    public Boolean delete(String id) {
        try {
            repository.delete(getEntity(id));
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    /*
     * 物理削除 Scheduled
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public Boolean deletePhysicsByExpiration() {
        try {
            repository.deleteByExpiration();
            System.out.println("認証コードを削除しました：" + new Date());
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    /*
     * 認証を得る
     */
    public VerifyEntity getEntity(String id) {
        Optional<VerifyEntity> uOptional = repository.findById(id);
        if (false == uOptional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return uOptional.get();
    }

    /*
     * 認証存在検証
     * email and name
     */
    public Specification<VerifyEntity> isExist(IUVerifyVO iuVerifyVO) {
        return new Specification<VerifyEntity>() {

            public Predicate toPredicate(Root<VerifyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preVerifyContent = cb.equal(root.get("content"), iuVerifyVO.getContent());
                Predicate preVerifyType = cb.equal(root.get("verifyType"), iuVerifyVO.getVerifyType());
                Predicate preVerifyMethod = cb.equal(root.get("verifyMethod"), iuVerifyVO.getVerifyMethod());
                predicate.add(preVerifyContent);
                predicate.add(preVerifyType);
                predicate.add(preVerifyMethod);
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }
}
