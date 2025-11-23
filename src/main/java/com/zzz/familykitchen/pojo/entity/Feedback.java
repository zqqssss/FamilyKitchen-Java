package com.zzz.familykitchen.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 反馈实体类
 */
@Data
@TableName("feedback")
public class Feedback {
    
    @TableId(type = IdType.AUTO)
    private Long id;

    
    private String type; // 功能异常/功能建议/其他
    
    private String content;
    
    private String contact; // 联系方式（可选）
    
    private String images; // 图片URL，多张用逗号分隔

    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
