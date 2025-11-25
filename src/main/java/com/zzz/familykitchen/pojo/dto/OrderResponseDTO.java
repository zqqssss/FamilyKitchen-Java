package com.zzz.familykitchen.pojo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private String orderNo;
    private String consignee;
    private String phone;
    private String address;
    private BigDecimal amount;
    private Integer status;
    private String statusText;  // 状态文本描述
    private String cancelReason;
    private LocalDateTime createTime;
    private List<OrderItemDTO> items;  // 订单明细
    
    @Data
    public static class OrderItemDTO {
        private Long id;
        private String name;
        private Integer quantity;
        private BigDecimal price;
        private String image;
    }
}
