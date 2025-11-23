package com.zzz.familykitchen.pojo.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.FieldFill.*;

/**
 * 用户实体类
 *
 * @author 张琦松
 * @since 2025.11.23
 */
@Data
@TableName("user")//mybatis注解
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String openid;          // 微信openid(唯一标识)
    private String nickname;        // 昵称
    private String avatarUrl;       // 头像URL
    private String address;         //地址
    private String remarks;         //备注

    //自动添加时间
    @TableField(fill = INSERT)
    private LocalDateTime createTime;

    @TableField(fill = INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String deleted;          //逻辑删除
    private String phone;           // 手机号(可选)
}
