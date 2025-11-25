package com.zzz.familykitchen.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;            // 主键，对应 orders.id

    private String orderNo;     // 订单号，对应 orders.order_no

    private Long userId;        // 用户id，对应 orders.user_id

    private String consignee;   // 联系人，对应 orders.consignee

    private String phone;       // 联系电话，对应 orders.phone

    private String address;     // 收货地址，对应 orders.address

    private BigDecimal amount;  // 订单金额，对应 orders.amount

    private String remark;      // 备注，对应 orders.remark


    /**
     * 订单状态：
     *  - 0：待完成 / 待处理
     *  - 1：已完成
     *  - 2：已取消
     *  ...你可以再扩展
     */
    private Integer status;     // 对应 orders.status

    private String cancelReason;  // 取消原因

    private LocalDateTime createTime; // 下单时间，对应 orders.create_time

    private LocalDateTime updateTime; // 更新时间，对应 orders.update_time
}
