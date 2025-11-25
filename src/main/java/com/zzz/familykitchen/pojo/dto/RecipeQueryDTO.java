package com.zzz.familykitchen.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecipeQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String title;
    private Integer category; // 接收前端传来的数字 (1, 2...)
    private BigDecimal minPrice;     // 最低价格（新增）
    private BigDecimal maxPrice;     // 最高价格（新增）

}
