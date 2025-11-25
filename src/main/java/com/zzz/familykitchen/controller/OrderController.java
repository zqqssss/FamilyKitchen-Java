package com.zzz.familykitchen.controller;


import com.zzz.familykitchen.pojo.dto.CancelOrderDTO;
import com.zzz.familykitchen.pojo.dto.OrderResponseDTO;
import com.zzz.familykitchen.pojo.dto.OrderSubmitDTO;
import com.zzz.familykitchen.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zzz.familykitchen.common.Result; // 关键：导入自己的Result类

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     */
    @PostMapping("/create")
    public Result<Long> createOrder(@RequestBody OrderSubmitDTO orderDTO) {
        try {
            log.info("接收到订单请求: {}", orderDTO);

            // 参数校验
            if (orderDTO.getUserId() == null) {
                return Result.error("用户ID不能为空");
            }
            if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
                return Result.error("订单明细不能为空");
            }
            if (orderDTO.getConsignee() == null || orderDTO.getPhone() == null) {
                return Result.error("收货信息不完整");
            }

            // 提交订单
            Long orderId = orderService.submitOrder(orderDTO);
            
            log.info("订单创建成功: orderId={}", orderId);
            return Result.success(orderId);

        } catch (Exception e) {
            log.error("创建订单失败", e);
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单列表（管理端）
     * @param status 订单状态 0-待完成 1-已完成 2-已取消 不传则查询全部
     */
    @GetMapping("/list")
    public Result<List<OrderResponseDTO>> getOrderList(
            @RequestParam(required = false) Integer status) {
        try {
            List<OrderResponseDTO> orders = orderService.getOrderList(status);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询订单列表失败", e);
            return Result.error("查询订单列表失败: " + e.getMessage());
        }
    }

    /**
     * 接单操作（管理端）
     */
    @PostMapping("/accept/{orderId}")
    public Result<Void> acceptOrder(@PathVariable Long orderId) {
        try {
            orderService.acceptOrder(orderId);
            return Result.success();
        } catch (Exception e) {
            log.error("接单失败: orderId={}", orderId, e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消订单（管理端）
     */
    @PostMapping("/cancel")
    public Result<Void> cancelOrder(@RequestBody CancelOrderDTO cancelDTO) {
        try {
            if (cancelDTO.getCancelReason() == null || cancelDTO.getCancelReason().trim().isEmpty()) {
                return Result.error("请填写取消原因");
            }
            orderService.cancelOrder(cancelDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("取消订单失败: orderId={}", cancelDTO.getOrderId(), e);
            return Result.error(e.getMessage());
        }
    }
}
