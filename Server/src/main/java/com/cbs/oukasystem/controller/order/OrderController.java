package com.cbs.oukasystem.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cbs.oukasystem.service.order.OrderService;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.finance.IUPayRecordVO;
import com.cbs.oukasystem.vo.in.order.IUOrderVO;
import com.cbs.oukasystem.vo.in.order.OrderCancelVO;
import com.cbs.oukasystem.vo.in.order.QueryOrderVO;
import com.cbs.oukasystem.vo.in.order.OrderDeployVO;
import com.cbs.oukasystem.vo.out.finance.PayRecordVO;
import com.cbs.oukasystem.vo.out.finance.SettlementVO;
import com.cbs.oukasystem.vo.out.order.DeployDetailsVO;
import com.cbs.oukasystem.vo.out.order.OrderVO;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

@Api(tags = "注文 OrderApi")
@RequestMapping("/api/order")
@CrossOrigin
@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @ResponseBody
    @Operation(summary = "OrderDetail -注文情報")
    @GetMapping("/details/{id}")
    public ResultVO<OrderVO> details(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getById(id));
    }

    @ResponseBody
    @Operation(summary = "DeployDetail - ドライバー支配情報")
    @GetMapping("/driverDeployDetails/{id}")
    public ResultVO<DeployDetailsVO> driverDeployDetails(
            @PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getDriverDeployDetails(id));
    }

    @ResponseBody
    @Operation(summary = "send - 配車発送")
    @PostMapping("/send")
    public ResultVO<Boolean> send() {
        return ResultVO.success(service.send());
    }

    @ResponseBody
    @Operation(summary = "isLodgingTips - 住宿提醒")
    @GetMapping("/isLodgingTips")
    public ResultVO<Boolean> isLodgingTips(@Parameter(required = true, description = "id") String id,
            @Parameter(required = true, description = "flag") Boolean flag) {
        return ResultVO.success(service.isLodgingTips(id, flag));
    }

    @ResponseBody
    @Operation(summary = "recover - 注文回復")
    @PostMapping("/recover/{id}")
    public ResultVO<Boolean> recover(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.recover(id));
    }

    @ResponseBody
    @Operation(summary = "DeployDetail - 車両支配情報")
    @GetMapping("/carDeployDetails/{id}")
    public ResultVO<DeployDetailsVO> carDeployDetails(
            @PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.getCarDeployDetails(id));
    }

    @ResponseBody
    @Operation(summary = "All Order - すべての注文")
    @GetMapping("/all")
    public ResultVO<List<OrderVO>> getAll() {
        return ResultVO.success(service.getAll());
    }

    @ResponseBody
    @Operation(summary = "From Order Page List -注文のリスト")
    @PostMapping("/list")
    public ResultVO<ListVO<OrderVO>> getPages(@RequestBody QueryOrderVO queryVO) {
        return ResultVO.success(service.getPages(queryVO));
    }

    @ResponseBody
    @Operation(summary = "Export 注文")
    @PostMapping(value = "/export")
    @Parameters(@Parameter(name = "queryVO", description = "注文", required = true))
    public void export(@Validated @RequestBody QueryOrderVO queryVO) {
        service.export(queryVO);
    }

    @ResponseBody
    @Operation(summary = "defaultPlacard 注文")
    @PostMapping("/defaultPlacard/{id}")
    public ResultVO<String> defaultPlacard(@PathVariable @Parameter(required = true, description = "id") String id) {
        return ResultVO.success(service.defaultPlacard(id));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Order - 追加・編集お客様情報")
    @PostMapping(value = "/setCustomer")
    @Parameters(@Parameter(name = "iuOrderVO", description = "追加・編集お客様情報", required = true))
    public ResultVO<OrderVO> setCustomer(@Validated @RequestBody IUOrderVO iuOrderVO) {
        return ResultVO.success(service.setCustomer(iuOrderVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Order - 追加・編集注文情報")
    @PostMapping(value = "/setOrder")
    @Parameters(@Parameter(name = "iuOrderVO", description = "追加・編集注文情報", required = true))
    public ResultVO<OrderVO> setOrder(@Validated @RequestBody IUOrderVO iuOrderVO) {
        return ResultVO.success(service.setOrder(iuOrderVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Other - 追加・編集他の情報")
    @PostMapping(value = "/setOther")
    @Parameters(@Parameter(name = "iuOrderVO", description = "追加・編集他の情報", required = true))
    public ResultVO<OrderVO> setOther(@Validated @RequestBody IUOrderVO iuOrderVO) {
        return ResultVO.success(service.setOther(iuOrderVO));
    }

    @ResponseBody
    @Operation(summary = "deploy Order - ドライバー&車両支配")
    @PostMapping(value = "/deploy")
    @Parameters(@Parameter(name = "OrderDeployVO", description = "支配", required = true))
    public ResultVO<Boolean> deploy(@Validated @RequestBody OrderDeployVO orderDeployVO) {
        return ResultVO.success(service.deploy(orderDeployVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Order - 追加・編集注文備考")
    @PostMapping(value = "/setCompanyRemark")
    @Parameters(@Parameter(name = "iuOrderVO", description = "追加・編集注文備考", required = true))
    public ResultVO<Boolean> setCompanyRemark(@Validated @RequestBody IUOrderVO iuOrderVO) {
        return ResultVO.success(service.setCompanyRemark(iuOrderVO));
    }

    @ResponseBody
    @Operation(summary = "cancel Order - 注文キャンセル")
    @PutMapping(value = "/cancel")
    @Parameters(@Parameter(name = "orderCancelVO", description = "注文キャンセル", required = true))
    public ResultVO<Boolean> confirm(@Validated @RequestBody OrderCancelVO orderCancelVO) {
        return ResultVO.success(service.cancelByCompany(orderCancelVO));
    }

    @ResponseBody
    @Operation(summary = "Add/Edit Pay - 后台追加・編集料金記録")
    @PostMapping(value = "/adminPay")
    @Parameters(@Parameter(name = "iuPayVO", description = "后台追加・編集料金記録", required = true))
    public ResultVO<PayRecordVO> pay(@Validated @RequestBody IUPayRecordVO iuPayVO) {
        return ResultVO.success(service.adminPay(iuPayVO));
    }

    @ResponseBody
    @Operation(summary = "confirmPay 料金記録確認")
    @PutMapping("/confirmPay/{payId}")
    public ResultVO<Boolean> confirmPay(@PathVariable @Parameter(required = true, description = "payId") String payId) {
        return ResultVO.success(service.confirmPay(payId));
    }

    @ResponseBody
    @Operation(summary = "paid 财务確認済")
    @PostMapping("/paid")
    public ResultVO<Boolean> paid(@RequestBody SettlementVO settlementVO) {
        return ResultVO.success(service.paid(settlementVO));
    }

    @ResponseBody
    @Operation(summary = "cashPaid 财务現金確認済")
    @PostMapping("/cashPaid")
    public ResultVO<Boolean> cashPaid(@RequestBody SettlementVO settlementVO) {
        return ResultVO.success(service.cashPaid(settlementVO));
    }

    @ResponseBody
    @Operation(summary = "settlement 财务決算済")
    @PostMapping("/settlement")
    public ResultVO<Boolean> settlement(@RequestBody SettlementVO settlementVO) {
        return ResultVO.success(service.settlement(settlementVO));
    }

    @ResponseBody
    @Operation(summary = "Delete Order - 削除注文")
    @DeleteMapping("/delete/{id}")
    public ResultVO<Boolean> delete(@PathVariable String id) {
        return ResultVO.success(service.delete(id));
    }

    @ResponseBody
    @Operation(summary = "Delete Order - 物理削除注文")
    @DeleteMapping("/deletePhysics/{id}")
    public ResultVO<Boolean> deletePhysics(@PathVariable String id) {
        return ResultVO.success(service.deletePhysics(id));
    }

    @ResponseBody
    @Operation(summary = "Audit Order - 審査注文")
    @PutMapping("/audit/{id}")
    public ResultVO<Boolean> audit(@PathVariable String id) {
        return ResultVO.success(service.audit(id));
    }
}
