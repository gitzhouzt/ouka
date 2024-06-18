package com.cbs.oukasystem.service.driverApp;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.BusinessEnum.EnumOrderStatus;
import com.cbs.oukasystem.common.CommonEnum.EnumStatus;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.car.CarEntity;
import com.cbs.oukasystem.entity.order.OrderEntity;
import com.cbs.oukasystem.entity.user.UserEntity;
import com.cbs.oukasystem.mapstruct.car.CarVOEntityMapStruct;
import com.cbs.oukasystem.repository.car.CarRepository;
import com.cbs.oukasystem.service.car.CarDamageService;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.car.IUCarDamageVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.out.car.CarDamageVO;
import com.cbs.oukasystem.vo.out.car.CarVO;
import com.cbs.oukasystem.vo.out.driverApp.CarDetailsVO;
import com.cbs.oukasystem.vo.out.driverApp.OrderDetailsVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;

@Service
@Transactional
public class DriverCarService {

    String KEY = "司机端-Car";

    @Autowired
    private CarRepository repository;

    @Autowired
    private CarDamageService damageService;

    public CarVO getById(String id) {
        return CarVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public CarEntity getEntity(String id) {
        Optional<CarEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(
                    EnumDataCheck.EMPTY, KEY + " Id:" + id);
        }
        return optional.get();
    }

    public List<CarVO> queryTodayCars(String userId) {
        List<Map<String, String>> maps = repository.queryTodayCars(userId);
        Object data = maps;
        List<CarVO> list = (List<CarVO>) data;
        return list;
    }

    public ListVO<CarVO> getTodayCars() {
        String userId = LoginUtils.getLoginUserId();
        ListVO<CarVO> listDto = new ListVO<>();
        List<CarVO> carVOs = queryTodayCars(userId);
        listDto.setItemCount(carVOs.size());
        listDto.setPage(1);
        listDto.setPageSize(carVOs.size());
        listDto.setPageCount(1);
        listDto.setList(carVOs);
        return listDto;
    }

    public CarDetailsVO getDetails(String id) {
        CarDetailsVO detailsVO = new CarDetailsVO();
        detailsVO.setId(id);
        List<CarDamageVO> carDamageVOs = damageService.getByCarId(id);
        detailsVO.setCarVO(getById(id));
        detailsVO.setCarDamageVOs(carDamageVOs);
        return detailsVO;
    }

    public CarDamageVO report(IUCarDamageVO iuVo) {
        CarDamageVO carDamageVO = new CarDamageVO();
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            iuVo.seta(null);
            iuVo.setIsAudit(false);
            iuVo.setStatus(EnumStatus.Waiting);
            iuVo.setStatusName(EnumStatus.Waiting.getMessage());
            payRecordVO = payRecordService.addOrEdit(iuVo);
        } else {
            OrderEntity entity = getEntity(iuVo.getOrderId());
            iuVo.setOrder(entity);
            UserEntity userEntity = userService.getEntity(LoginUtils.getLoginUserId());
            iuVo.setCreateByName(userEntity.getUserName());
            iuVo.setAuditByName(null);
            iuVo.setIsAudit(false);
            iuVo.setStatus(EnumStatus.Waiting);
            iuVo.setStatusName(EnumStatus.Waiting.getMessage());
            payRecordVO = payRecordService.addOrEdit(iuVo);
        }
        return payRecordVO;
    }

}
