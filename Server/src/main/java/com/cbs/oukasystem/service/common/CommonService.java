package com.cbs.oukasystem.service.common;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.FileUploadUtil;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.CommonEnum.EnumUserRole;
import com.cbs.oukasystem.service.user.UserService;
import com.cbs.oukasystem.vo.in.common.IUInitAdminVO;
import com.cbs.oukasystem.vo.in.user.IUUserVO;

@Service
@Transactional
public class CommonService {

    @Autowired
    UserService userService;

    public String upload(String key, MultipartFile... files) {
        String path = FileUploadUtil.upload(key, LoginUtils.getLoginUserId(), files);
        return path;
    }

    public Boolean initAdmin(IUInitAdminVO iuVo) {
        userService.deleteByAdminPhysics(); // admin 削除

        /*
         * admin
         */
        IUUserVO iuUserVO = new IUUserVO();
        iuUserVO.setUserName(iuVo.getUserName());
        iuUserVO.setUserEmail(iuVo.getUserEmail());
        iuUserVO.setUserPhone(iuVo.getUserPhone());
        iuUserVO.setUserRoles(EnumUserRole.Admin.getCode());
        userService.insertOrUpdate(iuUserVO);
        return true;
    }

}
