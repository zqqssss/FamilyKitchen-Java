package com.zzz.familykitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzz.familykitchen.mapper.OrderItemMapper;
import com.zzz.familykitchen.mapper.OrdersMapper;
import com.zzz.familykitchen.pojo.dto.CancelOrderDTO;
import com.zzz.familykitchen.pojo.dto.OrderResponseDTO;
import com.zzz.familykitchen.pojo.dto.OrderSubmitDTO;
import com.zzz.familykitchen.pojo.entity.OrderItem;
import com.zzz.familykitchen.pojo.entity.Orders;
import com.zzz.familykitchen.service.OrderService;
import com.zzz.familykitchen.util.OrderNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitOrder(OrderSubmitDTO orderDTO) {
        log.info("开始处理订单: userId={}, amount={}", orderDTO.getUserId(), orderDTO.getAmount());

        String orderNo = generateOrderNo();

        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setUserId(orderDTO.getUserId());
        order.setConsignee(orderDTO.getConsignee());
        order.setPhone(orderDTO.getPhone());
        order.setAddress(orderDTO.getAddress());
        order.setAmount(orderDTO.getAmount());
        order.setRemark(orderDTO.getRemark());
        order.setStatus(0);  // 待处理
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        ordersMapper.insert(order);
        Long orderId = order.getId();

        log.info("订单主表插入成功: orderId={}, orderNo={}", orderId, orderNo);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderSubmitDTO.OrderItemDTO itemDTO : orderDTO.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setRecipeId(itemDTO.getDishId());
            orderItem.setName(itemDTO.getName());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setCreateTime(LocalDateTime.now());
            orderItems.add(orderItem);
        }

        orderItems.forEach(item -> orderItemMapper.insert(item));
        log.info("订单明细插入成功: 共{}件商品", orderItems.size());

        return orderId;
    }

    @Override
    public List<OrderResponseDTO> getOrderList(Integer status) {
        log.info("查询订单列表: status={}", status);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        // 如果传入状态，按状态查询；否则查全部
        if (status != null) {
            queryWrapper.eq(Orders::getStatus, status);
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc(Orders::getCreateTime);

        List<Orders> orders = ordersMapper.selectList(queryWrapper);

        // 转换为DTO
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptOrder(Long orderId) {
        log.info("接单操作: orderId={}", orderId);

        Orders order = ordersMapper.selectById(orderId);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态异常，无法接单");
        }

        order.setStatus(1);  // 已完成
        order.setUpdateTime(LocalDateTime.now());

        ordersMapper.updateById(order);
        log.info("订单接单成功: orderId={}, orderNo={}", orderId, order.getOrderNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(CancelOrderDTO cancelDTO) {
        log.info("取消订单: orderId={}, reason={}", cancelDTO.getOrderId(), cancelDTO.getCancelReason());

        Orders order = ordersMapper.selectById(cancelDTO.getOrderId());

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态异常，无法取消");
        }

        order.setStatus(2);  // 已取消
        order.setCancelReason(cancelDTO.getCancelReason());
        order.setUpdateTime(LocalDateTime.now());

        ordersMapper.updateById(order);
        log.info("订单取消成功: orderId={}, orderNo={}", order.getId(), order.getOrderNo());
    }

    /**
     * 转换为响应DTO
     */
    private OrderResponseDTO convertToDTO(Orders order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        BeanUtils.copyProperties(order, dto);

        // 设置状态文本
        switch (order.getStatus()) {
            case 0:
                dto.setStatusText("待完成");
                break;
            case 1:
                dto.setStatusText("已完成");
                break;
            case 2:
                dto.setStatusText("已取消");
                break;
            default:
                dto.setStatusText("未知");
        }

        // 查询订单明细
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);

        List<OrderResponseDTO.OrderItemDTO> itemDTOs = orderItems.stream().map(item -> {
            OrderResponseDTO.OrderItemDTO itemDTO = new OrderResponseDTO.OrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setName(item.getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(itemDTOs);

        return dto;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return OrderNumberGenerator.generate();
    }
}
