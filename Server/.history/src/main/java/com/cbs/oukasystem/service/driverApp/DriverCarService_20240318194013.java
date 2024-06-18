package com.cbs.oukasystem.service.driverApp;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.service.car.CarService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.out.car.CarVO;

@Service
@Transactional
public class DriverCarService {

    String KEY = "司机端-Car";

    @Autowired
    private CarService carService;

    public ListVO<CarVO> getTodayCars() {
        String userId = LoginUtils.getLoginUserId();
        ListVO<CarVO> listDto = new ListVO<>();
        List<CarVO> carVOs = carService.queryTodayCars(userId);
        listDto.setItemCount(carVOs.size());
        listDto.setPage(1);
        listDto.setPageSize(carVOs.size());
        listDto.setPageCount(1);
        listDto.setList(carVOs);
        return listDto;
    }
}
