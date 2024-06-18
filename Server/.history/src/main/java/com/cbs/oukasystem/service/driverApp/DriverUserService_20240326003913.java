package com.cbs.oukasystem.service.driverApp;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.user.UserDriveVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserRepository;
import com.cbs.oukasystem.service.user.LogRecordService;
import com.cbs.oukasystem.service.user.UserDriveService;
import com.cbs.oukasystem.service.user.UserLogService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.IULogRecordVO;
import com.cbs.oukasystem.vo.in.user.IUUserLogVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
import com.cbs.oukasystem.vo.out.user.LogDetailsVO;
import com.cbs.oukasystem.vo.out.user.LogRecordVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

@Service
@Transactional
public class DriverUserService {

    String KEY = "司机端-User";

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserDriveService driveService;

    @Autowired
    private UserLogService logService;

    @Autowired
    private LogRecordService logRecordService;

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

    public UserDriveVO getUserDrier(String id) {
        return UserDriveVOEntityMapStruct.INSTANCE.toVO(driveService.getByUserId(id));
    }

    public UserDriveVO getDriverInfo() {
        String userId = LoginUtils.getLoginUserId();
        return getUserDrier(userId);
    }

    public ListVO<UserLogVO> getUserLogs(QueryUserLogVO queryUserLogVO) {
        return logService.getPages(queryUserLogVO);
    }

    public ListVO<UserLogVO> getLogs(QueryUserLogVO queryUserLogVO) {
        String userId = LoginUtils.getLoginUserId();
        queryUserLogVO.setUserId(userId);
        ListVO<UserLogVO> listDto = getUserLogs(queryUserLogVO);
        return listDto;
    }

    public UserLogVO writeLog(IUUserLogVO iuUserLogVO) {
        UserEntity entity = getEntity(LoginUtils.getLoginUserId());
        iuUserLogVO.setDriver1Id(entity.getId());
        iuUserLogVO.setDriver1Name(entity.getUserName());
        iuUserLogVO.setDriver1No(entity.getUserNo());
        return logService.addOrEdit(iuUserLogVO);
    }

    public LogRecordVO writeLogRecord(IULogRecordVO iuLogRecordVO) {
        UserEntity entity = getEntity(LoginUtils.getLoginUserId());
        iuLogRecordVO.setUserId(entity.getId());
        return logRecordService.addOrEdit(iuLogRecordVO);
    }

    public LogDetailsVO getLogDetails(String logId) {
        String userId = LoginUtils.getLoginUserId();
        LogDetailsVO logDetailsVO = new LogDetailsVO();
        // List<UserLogVO> userLogVOs = logService.findByDriver1IdAndDate(userId,
        // CommonUtils.getCurrentDate("yyyy/MM/dd"));

        logDetailsVO.setUserLogVO(logService.getById(logId));
        List<LogRecordVO> logRecordVOs = logRecordService.findByUserIdAndLogId(userId,
                logId);
        logDetailsVO.setLogRecordVOs(logRecordVOs);
        return logDetailsVO;
    }

}
