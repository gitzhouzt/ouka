package com.cbs.oukasystem.service.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.EmailUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumMailCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.user.UserDriveEntity;
import com.cbs.oukasystem.mapstruct.user.UserDriveVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserDriveRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.in.user.QueryUserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

@Service
@Transactional
public class UserTrainService {

    String KEY = "ドライブ";

    @Autowired
    private UserDriveRepository repository;

    @Autowired
    private EmailUtils emailUtils;

    /*
     * ドライブを得る
     */

    public UserDriveVO getById(String id) {
        return UserDriveVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public UserDriveEntity getByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public UserDriveEntity findOne(Specification<UserDriveEntity> spec) {
        Optional<UserDriveEntity> uOptional = repository.findOne(spec);
        UserDriveEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public UserDriveEntity getEntity(String id) {
        Optional<UserDriveEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全てのドライブ
     */

    public List<UserDriveVO> getAll() {
        return UserDriveVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<UserDriveEntity> findAll() {
        return repository.findAll();
    }

    public List<UserDriveEntity> findAll(Specification<UserDriveEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * ドライブのリスト
     */

    public ListVO<UserDriveVO> getPages(QueryUserDriveVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "expiryDate");
        Specification<UserDriveEntity> specification = Search(qVo);
        Page<UserDriveEntity> pages = repository.findAll(specification, pageable);
        List<UserDriveVO> list = UserDriveVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<UserDriveVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * ドライブ検索
     */
    public Specification<UserDriveEntity> Search(QueryUserDriveVO qVo) {
        return new Specification<UserDriveEntity>() {

            public Predicate toPredicate(Root<UserDriveEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preDriverName = cb.like(root.get("driverName"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverEmail = cb.like(root.get("driverNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverLicense = cb.like(root.get("driverLicense"), "%" + qVo.getKeyword() + "%");
                    Predicate preLicenseType = cb.like(root.get("licenseType"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverType = cb.like(root.get("driverType"), "%" + qVo.getKeyword() + "%");
                    predicate
                            .add(cb.or(preDriverName, preDriverEmail, preDriverLicense, preLicenseType, preDriverType));
                }

                if (null != qVo.getLicenseTypeCode() && !qVo.getLicenseTypeCode().isEmpty()) {
                    predicate.add(cb.equal(root.get("licenseTypeCode"), qVo.getLicenseTypeCode()));
                }
                if (null != qVo.getDriverTypeCode() && !qVo.getDriverTypeCode().isEmpty()) {
                    predicate.add(cb.equal(root.get("driverTypeCode"), qVo.getDriverTypeCode()));
                }

                Calendar calendar = Calendar.getInstance();
                if (null != qVo.getBeginTime() && null != qVo.getEndTime()) {
                    calendar.setTime(qVo.getBeginTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date startTime = calendar.getTime();
                    Predicate preBeginTime = cb.greaterThanOrEqualTo(root.get("expiryDate"), startTime);
                    calendar.setTime(qVo.getEndTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    Date endTime = calendar.getTime();
                    Predicate preEndTime = cb.lessThanOrEqualTo(root.get("expiryDate"), endTime);
                    predicate.add(cb.and(preBeginTime, preEndTime));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public UserDriveVO addOrEdit(IUUserDriveVO iuVo) {
        return UserDriveVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public UserDriveEntity insertOrUpdate(IUUserDriveVO iuVo) {
        UserDriveEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setDriverType(iuVo.getDriverType());
                entity.setDriverTypeCode(iuVo.getDriverTypeCode());
                entity.setLicenseType(iuVo.getLicenseType());
                entity.setLicenseTypeCode(iuVo.getLicenseTypeCode());
                entity.setDriverLicense(iuVo.getDriverLicense());
                entity.setExpiryDate(iuVo.getExpiryDate());
                entity.setHealthyDate(iuVo.getHealthyDate());
            } else {
                // 追加
                entity = UserDriveVOEntityMapStruct.INSTANCE.toEntity(iuVo);
            }
            entity = repository.save(entity);
        } catch (

        Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return entity;
    }

    /*
     * 驾照过期前7天提醒 Scheduled
     */
    @Scheduled(cron = "9 0 0 * * ?")
    public Boolean noticeOnExpiryDate7Days() {
        try {
            StringBuilder textBuilder = new StringBuilder();
            textBuilder.append("<html><body>");
            textBuilder.append("<br/>");
            textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
            textBuilder.append("<br/>");
            List<Map> drives = repository.noticeOnExpiryDate7Days();
            for (Map map : drives) {
                String id = map.get("id").toString();
                UserDriveEntity entity = getEntity(id);

            }
            textBuilder.append("</html></body>");
            String to = "";
            // if (null != to && !to.isEmpty()) {
            // emailUtils.sendExpiryDate7Days(to, entity);
            // }
        } catch (Exception e) {
            throw new BaseException(EnumMailCheck.SEND_ERROR, KEY + ":" +
                    e.getMessage());
        }
        return true;
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

}
