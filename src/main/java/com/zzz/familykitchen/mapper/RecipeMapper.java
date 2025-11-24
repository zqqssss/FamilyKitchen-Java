package com.zzz.familykitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzz.familykitchen.pojo.entity.Recipe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecipeMapper extends BaseMapper<Recipe> {
    // MyBatis-Plus 已经内置了基础 CRUD，无需手写 SQL
}
