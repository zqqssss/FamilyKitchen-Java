package com.zzz.familykitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzz.familykitchen.pojo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
