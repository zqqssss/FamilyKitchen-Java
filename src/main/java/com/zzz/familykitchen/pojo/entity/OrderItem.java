package com.zzz.familykitchen.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_item")
public class OrderItem implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;         // 主键，对应 order_item.id

    private Long orderId;    // 订单id，对应 order_item.order_id

    private Long recipeId;     // 菜品id，对应 order_item.dish_id

    private String name;     // 菜品名称，对应 order_item.name

    private String image;    // 菜品图片，对应 order_item.image

    private BigDecimal price; // 菜品单价，对应 order_item.price

    private Integer quantity; // 数量，对应 order_item.quantity

    private LocalDateTime createTime; // 创建时间，对应 order_item.create_time
}
