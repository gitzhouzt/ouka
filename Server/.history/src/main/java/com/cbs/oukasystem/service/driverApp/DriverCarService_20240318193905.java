package com.cbs.oukasystem.service.driverApp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.out.user.UserDriveVO;
import com.cbs.oukasystem.vo.out.user.UserLogVO;

@Service
@Transactional
public class DriverCarService {

    String KEY = "司机端-Car";

    @Autowired
    private UserService userService;

    public UserDriveVO getDriverInfo() {
        String userId = LoginUtils.getLoginUserId();
        return userService.getUserDrier(userId);
    }

    public ListVO<UserLogVO> getLogs() {
        String userId = LoginUtils.getLoginUserId();
        ListVO<UserLogVO> listDto = userService.getUserLogs(userId);
        return listDto;
    }

}
