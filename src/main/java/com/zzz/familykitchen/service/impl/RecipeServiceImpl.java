package com.zzz.familykitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzz.familykitchen.mapper.RecipeMapper;
import com.zzz.familykitchen.pojo.dto.RecipeQueryDTO;
import com.zzz.familykitchen.pojo.entity.Recipe;
import com.zzz.familykitchen.pojo.enums.RecipeCategory;
import com.zzz.familykitchen.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

    @Override
    public Page<Recipe> getRecipePage(RecipeQueryDTO queryDTO) {
        // 1. 构建分页对象
        Page<Recipe> pageInfo = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();

        // 按标题模糊查询
        wrapper.like(StringUtils.hasText(queryDTO.getTitle()), Recipe::getTitle, queryDTO.getTitle());

        // 按分类查询 (注意：MyBatis-Plus 会自动处理 Integer 到 Enum 的转换，只要配置正确)
        // 如果前端传的是 null，则不拼接此条件
        if (queryDTO.getCategory() != null) {
            // 这里假设你的 RecipeCategory 枚举能处理 int 值，或者底层直接存 int
            // 如果数据库是 tinyint，MyBatis-Plus + @EnumValue 会自动处理
            wrapper.apply("category = {0}", queryDTO.getCategory());
        }

        // 价格区间筛选（新增）
        if (queryDTO.getMinPrice() != null) {
            wrapper.ge(Recipe::getPrice, queryDTO.getMinPrice());
        }
        if (queryDTO.getMaxPrice() != null) {
            wrapper.le(Recipe::getPrice, queryDTO.getMaxPrice());
        }

        // 按创建时间倒序
        wrapper.orderByDesc(Recipe::getCreateTime);

        // 3. 执行查询
        return this.page(pageInfo, wrapper);
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        // 这里可以添加业务逻辑，比如：获取当前登录用户的 ID
        // 假设当前登录用户 ID 固定为 1 (实际应从 Token/Context 中获取)
        if (recipe.getUserId() == null) {
            recipe.setUserId(1L);
        }
        return this.save(recipe);
    }

    @Override
    public List<Map<String, Object>> getMenuByCategory() {

        // 1️⃣ 从数据库查询所有菜品（未删除的）
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Recipe::getDeleted, 0)
                .orderBy(true, true, Recipe::getCategory); // 按分类排序

        List<Recipe> allRecipes = this.list(wrapper);

        // 2️⃣ 按分类分组
        Map<RecipeCategory, List<Recipe>> groupedMap = allRecipes.stream()
                .collect(Collectors.groupingBy(Recipe::getCategory));

        // 3️⃣ 构建返回数据
        List<Map<String, Object>> result = new ArrayList<>();

        // 遍历所有分类枚举
        for (RecipeCategory category : RecipeCategory.values()) {

            // 获取该分类下的菜品列表（如果没有则为空列表）
            List<Recipe> dishes = groupedMap.getOrDefault(category, new ArrayList<>());

            // 构建分类对象
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", category.getCode()); // 分类ID (1,2,3...)
            categoryMap.put("name", category.getDesc()); // 分类名称 ("肉菜","素菜"...)
            categoryMap.put("count", 0); // 初始选中数量为0

            // 构建菜品列表
            List<Map<String, Object>> dishList = dishes.stream().map(recipe -> {
                Map<String, Object> dishMap = new HashMap<>();
                dishMap.put("id", recipe.getId());
                dishMap.put("name", recipe.getTitle());
                dishMap.put("price", recipe.getPrice());

                // 动态替换图片地址中的 localhost，确保手机可以访问
                String image = recipe.getCoverImage();
                if (image != null) {
                    image = image.replace("localhost", "192.168.199.171")
                            .replace("127.0.0.1", "192.168.199.171");
                }
                dishMap.put("image", image);

                dishMap.put("description", recipe.getDescription());
                return dishMap;
            }).collect(Collectors.toList());

            categoryMap.put("dishes", dishList);

            result.add(categoryMap);
        }

        return result;
    }
}
