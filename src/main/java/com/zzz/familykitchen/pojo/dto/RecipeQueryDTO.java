package com.zzz.familykitchen.pojo.dto;

import lombok.Data;

@Data
public class RecipeQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String title;
    private Integer category; // 接收前端传来的数字 (1, 2...)
}
