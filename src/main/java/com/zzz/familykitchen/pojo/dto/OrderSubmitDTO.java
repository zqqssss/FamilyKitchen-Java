package com.zzz.familykitchen.pojo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSubmitDTO {
    private Long userId;              // 用户ID
    private String consignee;         // 联系人
    private String phone;             // 手机号
    private String address;           // 收货地址
    private BigDecimal amount;        // 订单总金额
    private String remark;            // 备注
    private String diningType;        // 用餐方式（DINE_IN/TAKE_OUT）
    private List<OrderItemDTO> items; // 订单明细

    @Data
    public static class OrderItemDTO {
        private Long dishId;          // 菜品ID（对应RecipeId）
        private String name;          // 菜品名称
        private Integer quantity;     // 数量
        private BigDecimal price;     // 单价
    }
}
