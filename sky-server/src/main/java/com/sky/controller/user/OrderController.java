package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.PageOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = {"订单接口"})
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    //用户下单
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO osv = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(osv);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    //查询历史订单
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(Integer page,Integer pageSize,Integer status){
        PageResult pageResult = orderService.page(page,pageSize,status);
        return Result.success(pageResult);
    }

    //查询订单详细(订单id)
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> details(@PathVariable Long id){
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    //用户取消订单
    @PutMapping("/cancel/{id}")
    public Result cancle(@PathVariable Long id) throws Exception {
        orderService.cancle(id);
        return Result.success();
    }

    //再来一单
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }
}
