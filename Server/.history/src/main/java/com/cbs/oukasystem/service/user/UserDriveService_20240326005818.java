package com.cbs.oukasystem.service.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.CommonEnum.EnumUserRole;
import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.EmailUtils;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumMailCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.car.CarEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.user.UserDriveEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.car.CarVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.user.UserDriveVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserDriveRepository;
import com.cbs.oukasystem.service.order.OrderService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.QueryCarVO;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.in.user.QueryUserDriveVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driverApp.TodayVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;

@Service
@Transactional
public class UserDriveService {

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
        String sort = "updateTime";
        Direction dir = Sort.Direction.DESC;
        if (null != qVo.getSort() && !qVo.getSort().isEmpty()) {
            sort = qVo.getSort();
        }
        if (null != qVo.getSortType()) {
            if (qVo.getSortType().equals("ascend")) {
                dir = Sort.Direction.ASC;
            } else if (qVo.getSortType().equals("descend")) {
                dir = Sort.Direction.DESC;
            } else {
                sort = "updateTime";
                dir = Sort.Direction.DESC;
            }
        }
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                dir, sort);
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
                    Predicate preDriverName = cb.like(root.get("user").get("userName"),
                            "%" + qVo.getKeyword() + "%");
                    Predicate preDriverNo = cb.like(root.get("user").get("userNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preDriverLicense = cb.like(root.get("driverLicense"), "%" + qVo.getKeyword() + "%");
                    predicate
                            .add(cb.or(preDriverName, preDriverNo, preDriverLicense));
                }
                if (null != qVo.getLicenseType() && !qVo.getLicenseType().isEmpty()) {
                    predicate.add(cb.equal(root.get("licenseType"), qVo.getLicenseType()));
                }
                if (null != qVo.getDriverType() && !qVo.getDriverType().isEmpty()) {
                    predicate.add(cb.equal(root.get("driverType"), qVo.getDriverType()));
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

    public void export(QueryUserDriveVO qVo) {
        String screenName = "ドライバー";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "ドライバー番号", "userNo" },
                { "ドライバー名", "userName" },
                { "運転免許証番号", "driverLicense" },
                { "運転免許証タイプ", "licenseType" },
                { "運転免許証有効期限日", "expiryDate" },
                { "ドライバータイプ", "driverType" },
                { "前回健康診断日", "healthyDate" },
        };

        Specification<UserDriveEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<UserDriveEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                UserDriveVOEntityMapStruct.INSTANCE.toVOs(list));
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
     * 削除
     */
    public Boolean delete(String id) {
        try {
            UserDriveEntity entity = getEntity(id);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }

    public Boolean deleteByUserId(String userId) {
        try {
            UserDriveEntity entity = getByUserId(userId);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
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
