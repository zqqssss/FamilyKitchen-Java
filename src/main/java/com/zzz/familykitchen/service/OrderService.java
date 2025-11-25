package com.zzz.familykitchen.service;

import com.zzz.familykitchen.pojo.dto.CancelOrderDTO;
import com.zzz.familykitchen.pojo.dto.OrderResponseDTO;
import com.zzz.familykitchen.pojo.dto.OrderSubmitDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {
    Long submitOrder(OrderSubmitDTO orderDTO);

    /**
     * 获取订单列表（管理端）
     * @param status 订单状态 null-全部 0-待完成 1-已完成 2-已取消
     */
    List<OrderResponseDTO> getOrderList(Integer status);

    /**
     * 接单操作（管理端）
     */
    void acceptOrder(Long orderId);

    /**
     * 取消订单（管理端）
     */
    void cancelOrder(CancelOrderDTO cancelDTO);
}
