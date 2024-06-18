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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.EmailUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumMailCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.user.UserTrainEntity;
import com.cbs.oukasystem.mapstruct.user.UserTrainVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserTrainRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.IUUserTrainVO;
import com.cbs.oukasystem.vo.in.user.QueryUserTrainVO;
import com.cbs.oukasystem.vo.out.user.UserTrainVO;

@Service
@Transactional
public class UserTrainService {

    String KEY = "トレーニング";

    @Autowired
    private UserTrainRepository repository;

    @Autowired
    private EmailUtils emailUtils;

    /*
     * トレーニングを得る
     */

    public UserTrainVO getById(String id) {
        return UserTrainVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public UserTrainEntity getByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public UserTrainEntity findOne(Specification<UserTrainEntity> spec) {
        Optional<UserTrainEntity> uOptional = repository.findOne(spec);
        UserTrainEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public UserTrainEntity getEntity(String id) {
        Optional<UserTrainEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全てのトレーニング
     */

    public List<UserTrainVO> getAll() {
        return UserTrainVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<UserTrainEntity> findAll() {
        return repository.findAll();
    }

    public List<UserTrainEntity> findAll(Specification<UserTrainEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * トレーニングのリスト
     */

    public ListVO<UserTrainVO> getPages(QueryUserTrainVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "trainYear");
        Specification<UserTrainEntity> specification = Search(qVo);
        Page<UserTrainEntity> pages = repository.findAll(specification, pageable);
        List<UserTrainVO> list = UserTrainVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<UserTrainVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * トレーニング検索
     */
    public Specification<UserTrainEntity> Search(QueryUserTrainVO qVo) {
        return new Specification<UserTrainEntity>() {

            public Predicate toPredicate(Root<UserTrainEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preTrainrName = cb.like(root.get("driverName"), "%" + qVo.getKeyword() + "%");
                    Predicate preTrainrEmail = cb.like(root.get("driverNo"), "%" + qVo.getKeyword() + "%");
                    Predicate preTrainrLicense = cb.like(root.get("driverLicense"), "%" + qVo.getKeyword() + "%");
                    Predicate preLicenseType = cb.like(root.get("licenseType"), "%" + qVo.getKeyword() + "%");
                    Predicate preTrainrType = cb.like(root.get("driverType"), "%" + qVo.getKeyword() + "%");
                    predicate
                            .add(cb.or(preTrainrName, preTrainrEmail, preTrainrLicense, preLicenseType, preTrainrType));
                }

                if (null != qVo.getTrainYear() && !qVo.getTrainYear().isEmpty()) {
                    predicate.add(cb.equal(root.get("trainYear"), qVo.getTrainYear()));
                }

                // Calendar calendar = Calendar.getInstance();
                // if (null != qVo.getBeginTime() && null != qVo.getEndTime()) {
                // calendar.setTime(qVo.getBeginTime());
                // calendar.set(Calendar.HOUR_OF_DAY, 0);
                // calendar.set(Calendar.MINUTE, 0);
                // calendar.set(Calendar.SECOND, 0);
                // Date startTime = calendar.getTime();
                // Predicate preBeginTime = cb.greaterThanOrEqualTo(root.get("expiryDate"),
                // startTime);
                // calendar.setTime(qVo.getEndTime());
                // calendar.set(Calendar.HOUR_OF_DAY, 23);
                // calendar.set(Calendar.MINUTE, 59);
                // calendar.set(Calendar.SECOND, 59);
                // Date endTime = calendar.getTime();
                // Predicate preEndTime = cb.lessThanOrEqualTo(root.get("expiryDate"), endTime);
                // predicate.add(cb.and(preBeginTime, preEndTime));
                // }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public UserTrainVO addOrEdit(IUUserTrainVO iuVo) {
        return UserTrainVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public UserTrainEntity insertOrUpdate(IUUserTrainVO iuVo) {
        UserTrainEntity entity = null;
        try {
            // 更新
            if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
                entity = getEntity(iuVo.getId());
                entity.setTrainYear(iuVo.getTrainYear());
                entity.setTrainDate1(iuVo.getTrainDate1());
                entity.setTrainDate2(iuVo.getTrainDate2());
                entity.setTrainDate3(iuVo.getTrainDate3());
                entity.setTrainDate4(iuVo.getTrainDate4());
            } else {
                // 追加
                entity = UserTrainVOEntityMapStruct.INSTANCE.toEntity(iuVo);
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
    // @Scheduled(cron = "9 0 0 * * ?")
    // public Boolean noticeOnExpiryDate7Days() {
    // try {
    // StringBuilder textBuilder = new StringBuilder();
    // textBuilder.append("<html><body>");
    // textBuilder.append("<br/>");
    // textBuilder.append("<div>＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝</div>");
    // textBuilder.append("<br/>");
    // List<Map> drives = repository.noticeOnExpiryDate7Days();
    // for (Map map : drives) {
    // String id = map.get("id").toString();
    // UserTrainEntity entity = getEntity(id);

    // }
    // textBuilder.append("</html></body>");
    // String to = "";
    // // if (null != to && !to.isEmpty()) {
    // // emailUtils.sendExpiryDate7Days(to, entity);
    // // }
    // } catch (Exception e) {
    // throw new BaseException(EnumMailCheck.SEND_ERROR, KEY + ":" +
    // e.getMessage());
    // }
    // return true;
    // }

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
