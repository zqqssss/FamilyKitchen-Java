package com.zzz.familykitchen.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zzz.familykitchen.pojo.enums.RecipeCategory;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 菜品实体类
 *
 * @author 张琦松
 * @since 2025.11.23
 */
@Data
@TableName("recipe")
public class Recipe {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;            // 创建者ID
    private String title;           // 菜名
    private String coverImage;      // 封面图
    private String description;     // 简介

    /**
     * 菜品分类
     * 数据库存 int (1,2,3...)
     * 代码用 Enum (MEAT, VEGETABLE...)
     */
    private RecipeCategory category;

    @TableLogic  // 逻辑删除
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}