package com.cbs.oukasystem.service.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.Constants;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.FileUploadUtil;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.BusinessEnum.EnumActionType;
import com.cbs.oukasystem.common.BusinessEnum.EnumTargetType;
import com.cbs.oukasystem.common.CommonEnum.EnumUserRole;
import com.cbs.oukasystem.common.CommonEnum.EnumUserStatus;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumPwdCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumRegisterCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumResponseCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumUploadCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumVerifyCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.base.DictItemEntity;
import com.cbs.oukasystem.entity.login.VerifyEntity;
import com.cbs.oukasystem.entity.operate.ScheduleEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.entity.user.UserRestEntity;
import com.cbs.oukasystem.mapstruct.user.UserDriveVOEntityMapStruct;
import com.cbs.oukasystem.mapstruct.user.UserVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserRepository;
import com.cbs.oukasystem.service.login.VerifyService;
import com.cbs.oukasystem.service.operate.ScheduleService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.login.IUVerifyVO;
import com.cbs.oukasystem.vo.in.user.IUUserDriveVO;
import com.cbs.oukasystem.vo.in.user.IUUserLogVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.in.user.IUUserTrainVO;
import com.cbs.oukasystem.vo.in.user.IUUserVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.in.user.QueryUserVO;
import com.cbs.oukasystem.vo.in.user.ResetPwdVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;
import com.cbs.oukasystem.vo.out.user.UserRestVO;
import com.cbs.oukasystem.vo.out.user.UserVO;

@Service
@Transactional
public class UserService {

    String KEY = "ユーザー";

    @Autowired
    private UserRepository repository;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private UserRestService restService;

    @Autowired
    private UserDriveService driveService;

    @Autowired
    private UserLogService logService;

    @Autowired
    private UserTrainService trainService;

    @Autowired
    private ScheduleService scheduleService;

    /*
     * ユーザーを得る
     */

    public UserVO getById(String id) {
        return UserVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public UserEntity getByLoginName(String loginName) {
        return repository.findByLoginName(loginName);
    }

    public UserVO getByLogin() {
        return UserVOEntityMapStruct.INSTANCE.toVO(getEntity(LoginUtils.getLoginUserId()));
    }

    public UserEntity findOne(Specification<UserEntity> spec) {
        Optional<UserEntity> uOptional = repository.findOne(spec);
        UserEntity entity = null;
        if (uOptional.isPresent()) {
            entity = uOptional.get();
        }
        return entity;
    }

    public UserEntity getEntity(String id) {
        Optional<UserEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全てのユーザー
     */

    public List<UserVO> getAll() {
        return UserVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public List<UserEntity> findAll(Specification<UserEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * ユーザーのリスト
     */

    public ListVO<UserVO> getPages(QueryUserVO qVo) {
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
        Specification<UserEntity> specification = Search(qVo);
        Page<UserEntity> pages = repository.findAll(specification, pageable);
        List<UserVO> list = UserVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<UserVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    public ListVO<UserVO> getPagesByCallDriver(QueryUserVO qVo) {
        List<Map> pages = repository.queryByCallDriver((qVo.getPage() - 1) * qVo.getPageSize(),
                qVo.getPageSize());
        Object data = pages;
        List<UserVO> list = (List<UserVO>) data;
        ListVO<UserVO> listDto = new ListVO<>();
        long itemCnt = repository.countByCallDriver();
        int pageCnt = (int) itemCnt / qVo.getPageSize() + (itemCnt % qVo.getPageSize() == 0 ? 0 : 1);
        listDto.setItemCount(itemCnt);
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pageCnt);
        listDto.setList(list);
        return listDto;
    }

    public ListVO<UserVO> getPagesByDeployDriver(QueryUserVO qVo) {
        List<Map> pages = repository.queryByCallDriver((qVo.getPage() - 1) * qVo.getPageSize(),
                qVo.getPageSize());
        Object data = pages;
        List<UserVO> list = (List<UserVO>) data;
        ListVO<UserVO> listDto = new ListVO<>();
        long itemCnt = repository.countByCallDriver();
        int pageCnt = (int) itemCnt / qVo.getPageSize() + (itemCnt % qVo.getPageSize() == 0 ? 0 : 1);
        listDto.setItemCount(itemCnt);
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pageCnt);
        listDto.setList(list);
        return listDto;
    }

    public UserDriveVO getUserDrier(String id) {
        return UserDriveVOEntityMapStruct.INSTANCE.toVO(driveService.getByUserId(id));
    }

    public ListVO<UserLogVO> getUserLogs(String id) {
        QueryUserLogVO queryUserLogVO = new QueryUserLogVO();
        queryUserLogVO.setUserId(id);
        return logService.getPages(queryUserLogVO);
    }

    /*
     * ユーザー検索
     */
    public Specification<UserEntity> Search(QueryUserVO qVo) {
        return new Specification<UserEntity>() {

            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preUserName = cb.like(root.get("userName"), "%" + qVo.getKeyword() + "%");
                    Predicate preUserEmail = cb.like(root.get("userEmail"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preUserName, preUserEmail));
                }
                if (null != qVo.getUserRole()) {
                    predicate.add(cb.like(root.get("userRoles"), "%" + qVo.getUserRole() + "%"));
                }
                Calendar calendar = Calendar.getInstance();
                if (null != qVo.getBeginTime() && null != qVo.getEndTime()) {
                    calendar.setTime(qVo.getBeginTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date startTime = calendar.getTime();
                    Predicate preBeginTime = cb.greaterThanOrEqualTo(root.get("createTime"), startTime);
                    calendar.setTime(qVo.getEndTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    Date endTime = calendar.getTime();
                    Predicate preEndTime = cb.lessThanOrEqualTo(root.get("createTime"), endTime);
                    predicate.add(cb.and(preBeginTime, preEndTime));
                }
                if (null != qVo.getIsAudit()) {
                    predicate.add(cb.equal(root.get("isAudit"), qVo.getIsAudit()));
                }
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public UserVO insertOrUpdate(IUUserVO iuVo) {
        List<String> roles = new ArrayList<String>();
        roles.add("admin");
        roles.add("administrator");
        if (roles.contains(iuVo.getUserName().toLowerCase())) {
            throw new BaseException(EnumRegisterCheck.CANT_ADMIN);
        }
        UserEntity newUser = UserVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        return UserVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(newUser));
    }

    public UserEntity insertOrUpdate(UserEntity newUser) {
        UserEntity entity = null;
        if (null != newUser.getId() && !newUser.getId().isEmpty()) {
            // 更新
            entity = getEntity(newUser.getId());
            entity.setUserNo(newUser.getUserNo());
            entity.setUserAvatar(newUser.getUserAvatar());
            entity.setUserName(newUser.getUserName());
            entity.setRealName(newUser.getRealName());
            entity.setCountryNum(newUser.getCountryNum());
            entity.setUserPhone(newUser.getUserPhone());
            entity.setUserRoles(newUser.getUserRoles());
            entity.setUserRolesName(newUser.getUserRolesName());
            entity.setUserAddress(newUser.getUserAddress());
            entity.setUserPost(newUser.getUserPost());
            entity.setRemark(newUser.getRemark());

            entity.setUserWechat(newUser.getUserWechat());
            entity.setUserLine(newUser.getUserLine());
            entity.setUserWhatsApp(newUser.getUserWhatsApp());

        } else {
            List<UserEntity> list = repository
                    .findAll(isExist(newUser.getLoginName(), newUser.getId()));
            if (!list.isEmpty()) {
                throw new BaseException(EnumDataCheck.USER_EXISTED);
            }
            // 追加
            entity = new UserEntity();
            if (null != newUser.getLoginPass() && !newUser.getLoginPass().isEmpty()) {
                entity.setLoginPass(CommonUtils.toSHA256(newUser.getLoginPass()));
            } else {
                // DEFAULT PASSWORD - SHA256
                entity.setLoginPass(CommonUtils.toSHA256(Constants.DEFAULT_PASSWORD));
            }
            entity.setLoginName(newUser.getUserEmail());
            entity.setUserNo(newUser.getUserNo());
            if (null == newUser.getUserNo() || newUser.getUserNo().isEmpty()) {
                String userNo = CommonUtils.getNewDateEquipmentNo(Constants.DEFAULT_STAFF_PREFIX,
                        repository.count());
                entity.setUserNo(userNo);
            }
            entity.setUserAvatar(newUser.getUserAvatar());
            entity.setUserEmail(newUser.getUserEmail());
            if (null != newUser.getUserName() && !newUser.getUserName().isEmpty()) {
                entity.setUserName(newUser.getUserName());
            } else {
                entity.setUserName(CommonUtils.getRandomName());
            }
            entity.setStatus(EnumUserStatus.Working);
            entity.setStatusName(EnumUserStatus.Working.getMessage());
            entity.setRealName(newUser.getRealName());
            entity.setCountryNum(newUser.getCountryNum());
            entity.setUserPhone(newUser.getUserPhone());
            entity.setUserRoles(newUser.getUserRoles());
            entity.setUserAddress(newUser.getUserAddress());
            entity.setUserPost(newUser.getUserPost());
            entity.setRemark(newUser.getRemark());

            entity.setUserWechat(newUser.getUserWechat());
            entity.setUserLine(newUser.getUserLine());
            entity.setUserWhatsApp(newUser.getUserWhatsApp());
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }

        if (entity.getUserRoles().contains(EnumUserRole.Driver.getCode())) {
            // 如果是司机，插入司机表
            if (null == driveService.getByUserId(entity.getId())) {
                IUUserDriveVO iuUserDriveVO = new IUUserDriveVO();
                iuUserDriveVO.setUserId(entity.getId());
                iuUserDriveVO.setUser(entity);
                driveService.addOrEdit(iuUserDriveVO);
            }
            // 如果是司机，插入司机培训表
            if (null == trainService.getByUserId(entity.getId())) {
                IUUserTrainVO iuUserTrainVO = new IUUserTrainVO();
                iuUserTrainVO.setUserId(entity.getId());
                iuUserTrainVO.setUser(entity);
                iuUserTrainVO.setTrainYear(CommonUtils.getFormatDate(new Date(), "yyyy"));
                trainService.addOrEdit(iuUserTrainVO);
            }
        }
        return entity;
    }

    /*
     * 休暇
     */
    public Boolean rest(IUUserRestVO iVo) {
        UserEntity entity = null;
        if (null == iVo.getId() || iVo.getId().isEmpty()) {
            entity = repository.findByUserNo(iVo.getUserNo());
            iVo.setUserId(entity.getId());
            iVo.setUserName(entity.getUserName());
            iVo.setUserRoles(entity.getUserRoles());
        } else {
            entity = getEntity(iVo.getId());
        }
        UserRestVO userRestVO = restService.addOrEdit(iVo);

        // 先删除日程表
        scheduleService.deletePhysicsByRestId(userRestVO.getId(), EnumTargetType.Driver);

        // 休暇 ドライバー スケジュール
        List<ScheduleEntity> scheduleEntities = new ArrayList<ScheduleEntity>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Date sd = userRestVO.getStartTime();
        Date ed = userRestVO.getEndTime();
        Calendar sCalendar = Calendar.getInstance();
        Calendar dCalendar = Calendar.getInstance();
        sCalendar.setTime(sd);
        dCalendar.setTime(ed);
        long diff = dCalendar.getTimeInMillis() - sCalendar.getTimeInMillis();
        long days = diff / (1000 * 60 * 60 * 24);
        ScheduleEntity scheduleEntity = null;
        int index = 0;
        do {
            scheduleEntity = new ScheduleEntity();
            scheduleEntity.setTargetId(userRestVO.getUserId());
            scheduleEntity.setTargetNo(userRestVO.getUserNo());
            scheduleEntity.setTargetName(userRestVO.getUserName());
            scheduleEntity.setActionId(userRestVO.getId());
            scheduleEntity.setTargetType(EnumTargetType.Driver);
            scheduleEntity.setActionType(EnumActionType.Rest);
            scheduleEntity.setRemark(String.format("%S",
                    userRestVO.getRestType()));
            sCalendar.setTime(sd);
            sCalendar.add(Calendar.DATE, index);
            scheduleEntity.setWorkDate(sf.format(sCalendar.getTime()));
            scheduleEntity.setWorkTime(sCalendar.getTime());
            scheduleEntities.add(scheduleEntity);
            index++;
        } while (index <= days);
        scheduleService.saveAll(scheduleEntities);

        // 如果休息时间是当天，改变司机状态
        sCalendar.setTime(userRestVO.getStartTime());
        Date today = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(today);
        if (todayCalendar.get(Calendar.DATE) == sCalendar.get(Calendar.DATE)) {
            entity.setStatus(EnumUserStatus.Resting);
            entity.setStatusName(EnumUserStatus.Resting.getMessage());
        }
        repository.save(entity);
        return true;
    }

    /*
     * ユーザ BATCH
     */
    public Boolean batch(String key, MultipartFile... files) {
        try {
            String suffix = FileUploadUtil.getSuffixName(files);
            String path = FileUploadUtil.upload(key, LoginUtils.getLoginUserId(), files);
            List<List<String>> rows = null;
            InputStream inputStream = new FileInputStream(FileUploadUtil.uploadPath + path);
            if (suffix.equals(".xls") || suffix.equals(".csv")) {
                rows = CsvUtils.readCsvFile(inputStream);
            } else {
                new BaseException(EnumUploadCheck.IMAGE_FORMAT);
            }
            int i = 0;
            if (null == rows) {
                return true;
            }
            for (List<String> cols : rows) {
                if (i != 0 && cols.size() > 0) {
                    String userNo = cols.get(0).replace(" ", "");
                    if (null == userNo || userNo.isEmpty()) {
                        // userNo = CommonUtils.get
                    }
                    if (null != userNo && !userNo.isEmpty()) {
                        String dictCode = cols.get(1).replace(" ", "");
                        String itemName = cols.get(2).replace(" ", "");
                        String itemCode = cols.get(3).replace(" ", "");
                        String remark = "";
                        if (cols.size() == 5) {
                            remark = cols.get(4).replace(" ", "");
                        }
                        UserEntity entity = new UserEntity();
                        // entity.setDictCode(dictCode);
                        // entity.setDictCode(dictCode);
                        // entity.setDictId(dictId);
                        // entity.setItemCode(itemCode);
                        // entity.setItemName(itemName);
                        // entity.setRemark(remark);
                        repository.save(entity);
                    }
                }
                i++;
            }
        } catch (Exception e) {
            throw new BaseException(EnumUploadCheck.ERROR, e.getMessage());
        }
        return true;
    }

    /*
     * 休暇 BATCH
     */
    public Boolean batchRest(String key, MultipartFile... files) {
        try {
            String suffix = FileUploadUtil.getSuffixName(files);
            String path = FileUploadUtil.upload(key, LoginUtils.getLoginUserId(), files);

            List<List<String>> rows = null;
            InputStream inputStream = new FileInputStream(FileUploadUtil.uploadPath + path);
            if (suffix.equals(".xls") || suffix.equals(".csv")) {
                rows = CsvUtils.readCsvFile(inputStream);
            }
            // else if (suffix.equals(".xlsx")) {
            // rows = CsvUtils.readXlsxFile(inputStream);
            // }
            for (List<String> cols : rows) {
                String userNo = cols.get(0).replace(" ", "");
                if (!userNo.equals("司机编号") && !userNo.equals("userNo")) {
                    String startTime = cols.get(1).replace(" ", "");
                    String endTime = cols.get(2).replace(" ", "");
                    String restType = cols.get(3).replace(" ", "");
                    String restTypeCode = cols.get(4).replace(" ", "");
                    String remark = cols.get(5).replace(" ", "");

                    IUUserRestVO iuUserRestVO = new IUUserRestVO();
                    iuUserRestVO.setUserNo(userNo);
                    iuUserRestVO.setStartTime(CommonUtils.getDateStr(startTime, "yyyy/MM/dd"));
                    iuUserRestVO.setEndTime(CommonUtils.getDateStr(endTime, "yyyy/MM/dd"));
                    iuUserRestVO.setRestType(restType);
                    iuUserRestVO.setRestTypeCode(restTypeCode);
                    iuUserRestVO.setRemark(remark);
                    rest(iuUserRestVO);
                }
            }
        } catch (Exception e) {
            throw new BaseException(EnumUploadCheck.ERROR, e.getMessage());
        }

        return true;
    }

    public Boolean restCancel(String restId) {
        UserRestEntity restEntity = restService.getEntity(restId);

        // 如果休息时间是当天，改变司机状态
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(restEntity.getStartTime());
        Date today = new Date();
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTime(today);
        if (todayCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            resetStatus(restEntity.getUserId(), EnumUserStatus.Working);
        }
        restService.delete(restId);
        scheduleService.deletePhysicsByRestId(restId, EnumTargetType.Driver);
        return true;
    }

    /*
     * ステータス の 変更
     */
    public Boolean resetStatus(String id, EnumUserStatus userStatus) {
        UserEntity user = getEntity(id);
        user.setStatus(userStatus);
        user.setStatusName(userStatus.getMessage());
        repository.save(user);
        return true;
    }

    /*
     * Admin パスワード変更
     */
    public Boolean resetPwd(ResetPwdVO resetPwdVO) {
        try {
            UserEntity user = null;
            if (null == resetPwdVO.getUserId() || resetPwdVO.getUserId().isEmpty()) {
                user = getEntity(LoginUtils.getLoginUserId());
            } else {
                UserEntity actionUser = getEntity(LoginUtils.getLoginUserId());
                if (actionUser.getUserRoles().contains(EnumUserRole.Admin.getCode())) {
                    user = getEntity(resetPwdVO.getUserId());
                } else {
                    throw new BaseException(EnumResponseCheck.USER_NOT_ALLOWED);
                }
            }
            if (null == resetPwdVO.getPwd() || resetPwdVO.getPwd().isEmpty()) {
                throw new BaseException(EnumPwdCheck.INPUT_OLD_NULL);
            }
            if (null == resetPwdVO.getNewPwd() || resetPwdVO.getNewPwd().isEmpty()) {
                throw new BaseException(EnumPwdCheck.INPUT_NEW_NULL);
            }
            if (null == resetPwdVO.getConfirmPwd() || resetPwdVO.getConfirmPwd().isEmpty()) {
                throw new BaseException(EnumPwdCheck.INPUT_CONFIRM_NULL);
            }
            // パスワード 間違い
            if (!user.getLoginPass().equals(CommonUtils.toSHA256(resetPwdVO.getPwd()))) {
                throw new BaseException(EnumPwdCheck.INPUT_ERROR);
            }
            // パスワード 違い
            if (!resetPwdVO.getNewPwd().equals(resetPwdVO.getConfirmPwd())) {
                throw new BaseException(EnumPwdCheck.INPUT_NOT_SAME);
            }
            user.setLoginPass(CommonUtils.toSHA256(resetPwdVO.getNewPwd()));
            repository.save(user);
        } catch (Exception e) {
            throw new BaseException(EnumIOUCheck.ERROR, e.getMessage());
        }
        return true;
    }

    public Boolean resetPwdByApp(ResetPwdVO iVo) {
        if (null != iVo.getCode() && !iVo.getCode().isEmpty()) {
            VerifyEntity verifyEntity = verifyService.findByContent(iVo.getContent(), iVo.getVerifyType(),
                    iVo.getVerifyMethod());
            if (null != verifyEntity) {
                Date expiration = new Date(verifyEntity.getExpiration());
                Date now = new Date();
                if (now.after(expiration)) {
                    // 过期
                    throw new BaseException(EnumVerifyCheck.EXPIRED);
                } else {
                    if (!iVo.getCode().equals(verifyEntity.getCode())) {
                        // 確認コード間違い
                        throw new BaseException(EnumVerifyCheck.INPUT_ERROR);
                    }
                }
            } else {
                throw new BaseException(EnumVerifyCheck.EXPIRED);
            }
        }
        UserEntity user = getByLoginName(iVo.getUserEmail());
        // パスワード 違い
        if (!iVo.getNewPwd().equals(iVo.getConfirmPwd())) {
            throw new BaseException(EnumPwdCheck.INPUT_NOT_SAME);
        }
        user.setLoginPass(CommonUtils.toSHA256(iVo.getNewPwd()));
        repository.save(user);
        return true;
    }

    public Boolean sendVerifyCode(IUVerifyVO iVo) {
        if (null != iVo.getIsPwd() && iVo.getIsPwd() == false) {
            List<UserEntity> list = repository
                    .findAll(isExist(iVo.getContent(), ""));
            if (!list.isEmpty()) {
                throw new BaseException(EnumDataCheck.USER_EXISTED);
            }
        }
        return verifyService.sendVerifyCode(iVo);
    }

    /*
     * ユーザー存在検証
     * email and name
     */
    public Specification<UserEntity> isExist(String loginName, String userId) {
        return new Specification<UserEntity>() {

            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preLoginName = cb.equal(root.get("loginName"), loginName);
                Predicate preUserNo = cb.equal(root.get("userNo"), loginName);
                predicate.add(preLoginName);
                predicate.add(preUserNo)
                if (null != userId && !userId.isEmpty()) {
                    predicate.add(cb.notEqual(root.get("id"), userId));
                }
                predicate.add(cb.notEqual(root.get("isDelete"), true));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    public void export(QueryUserVO qVo) {
        String screenName = "ユーザー";
        String[][] dataMap = new String[][] {
                { "番号", "index" },
                { "ユーザー番号", "userNo" },
                { "ユーザー名", "userName" },
                { "アカウント", "loginName" },
                { "メール", "userEmail" },
                { "携帯", "userPhone" },
                { "住所", "userAddress" },
                { "役職", "userRoles" },
                { "ステータス", "statusName" },
                { "備考", "remark" },
        };

        Specification<UserEntity> specification = Search(qVo);
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        List<UserEntity> list = repository.findAll(specification, sort);

        CsvUtils.downLoadCsvFile(screenName, dataMap,
                UserVOEntityMapStruct.INSTANCE.toVOs(list));
    }

    /*
     * 削除
     */

    public Boolean delete(String id) {
        try {
            UserEntity entity = getEntity(id);
            entity.setIsDelete(true);
            entity.setDeleteBy(LoginUtils.getLoginUserId());
            entity.setDeleteTime(new Date());
            repository.save(entity);

            // 如果包括司机，删除司机信息，培训信息
            if (entity.getUserRoles().contains(EnumUserRole.Driver.getCode())) {
                // 删除司机信息
                driveService.deleteByUserId(id);
                // 物理删除培训信息
                trainService.deletePhysicsByUserId(id);
            }

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

    public Boolean deleteByAdminPhysics() {
        try {
            repository.deleteByUserName("admin");
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, "Admin:" + e.getMessage());
        }
        return true;
    }

    /*
     * 有効審査 true 有効 false 無効
     */

    public Boolean audit(String id) {
        UserEntity entity = getEntity(id);
        entity.setIsAudit(!entity.getIsAudit());
        if (null == repository.save(entity)) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return true;
    }

}
