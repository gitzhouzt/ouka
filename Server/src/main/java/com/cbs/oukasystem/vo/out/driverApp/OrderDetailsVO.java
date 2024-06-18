package com.cbs.oukasystem.vo.out.driverApp;

import java.util.List;

import com.cbs.oukasystem.vo.NormalVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.order.OrderFileVO;
import com.cbs.oukasystem.vo.out.order.OrderGoodsVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "OrderDetailsVO")
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsVO extends NormalVO {

    @Schema(name = "orderVO")
    private OrderVO orderVO = new OrderVO();

    @Schema(name = "orderGoodsVOs")
    private List<OrderGoodsVO> orderGoodsVOs;

    @Schema(name = "payRecordVOs")
    private List<PayRecordVO> payRecordVOs;

    @Schema(name = "orderFileVOs")
    private List<OrderFileVO> orderFileVOs;
}
