package com.cbs.oukasystem.service.driverApp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.mapstruct.user.UserDriveVOEntityMapStruct;
import com.cbs.oukasystem.repository.user.UserRepository;
import com.cbs.oukasystem.service.user.UserDriveService;
import com.cbs.oukasystem.service.user.UserLogService;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.user.QueryUserLogVO;
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

    public UserDriveVO getUserDrier(String id) {
        return UserDriveVOEntityMapStruct.INSTANCE.toVO(driveService.getByUserId(id));
    }

    public UserDriveVO getDriverInfo() {
        String userId = LoginUtils.getLoginUserId();
        return getUserDrier(userId);
    }

    public ListVO<UserLogVO> getUserLogs(String id) {
        QueryUserLogVO queryUserLogVO = new QueryUserLogVO();
        queryUserLogVO.setUserId(id);
        return logService.getPages(queryUserLogVO);
    }

    public ListVO<UserLogVO> getLogs() {
        String userId = LoginUtils.getLoginUserId();
        ListVO<UserLogVO> listDto = getUserLogs(userId);
        return listDto;
    }

}
