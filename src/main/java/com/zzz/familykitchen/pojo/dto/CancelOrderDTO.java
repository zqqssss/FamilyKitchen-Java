package com.zzz.familykitchen.pojo.dto;

import lombok.Data;

@Data
public class CancelOrderDTO {
    private Long orderId;
    private String cancelReason;  // 退回原因
}
