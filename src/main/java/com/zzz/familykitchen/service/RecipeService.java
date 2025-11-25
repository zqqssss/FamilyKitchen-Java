package com.zzz.familykitchen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzz.familykitchen.pojo.dto.RecipeQueryDTO;
import com.zzz.familykitchen.pojo.entity.Recipe;

import java.util.List;
import java.util.Map;

public interface RecipeService extends IService<Recipe> {
    /**
     * 分页查询菜品
     */
    Page<Recipe> getRecipePage(RecipeQueryDTO queryDTO);

    /**
     * 新增菜品
     */
    boolean addRecipe(Recipe recipe);

    List<Map<String, Object>> getMenuByCategory();
}
