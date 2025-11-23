// FeedbackDTO.java
package com.zzz.familykitchen.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class FeedbackDTO {
    private Long userId;
    private String type;
    private String content;
    private String contact;
    private List<String> images;
}
