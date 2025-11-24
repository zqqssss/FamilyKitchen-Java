package com.zzz.familykitchen.pojo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecipeCategory {

    MEAT(1, "肉菜"),
    VEGETABLE(2, "素菜"),
    GREENS(3, "青菜"),
    STAPLE(4, "主食"),
    DESSERT(5, "甜品"),
    SOUP(6, "汤羹");

    /**
     * @EnumValue: 告诉 MyBatis-Plus 存入数据库时用这个值 (存 1)
     */
    @EnumValue
    // ⬇️ 修改点：把 @JsonValue 移到 code 上
    // 告诉 Spring Boot 传给前端时也用这个值 (传 1)
    @JsonValue 
    private final Integer code;

    /**
     * 描述
     */
    // ❌ 这里的 @JsonValue 去掉
    private final String desc;
    
    // ... getByCode 方法保持不变
}
