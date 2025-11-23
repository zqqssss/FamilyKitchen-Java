package com.zzz.familykitchen.service;

import com.zzz.familykitchen.pojo.entity.Feedback;
import org.springframework.web.multipart.MultipartFile;

public interface FeedbackService {
    String uploadFeedbackImage(MultipartFile image);

    Feedback submitFeedback(String type, String content, String contact,String images);
}
