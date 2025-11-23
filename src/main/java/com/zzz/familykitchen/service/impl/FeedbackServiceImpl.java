package com.zzz.familykitchen.service.impl;

import com.zzz.familykitchen.mapper.FeedbackMapper;
import com.zzz.familykitchen.pojo.entity.Feedback;
import com.zzz.familykitchen.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Value("${file.upload.path:/uploads/feedback/}")
    private String uploadPath;

    @Value("${file.access.url:http://localhost:8080/uploads/feedback/}")
    private String accessUrl;


    @Override
    public String uploadFeedbackImage(MultipartFile image) {
        try {
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            // 保存文件
            File targetFile = new File(uploadDir, fileName);
            image.transferTo(targetFile);

            String imageUrl = accessUrl + fileName;
            log.info("反馈图片上传成功 - url: {}", imageUrl);

            return imageUrl;

        } catch (Exception e) {
            log.error("反馈图片上传失败", e);
            throw new RuntimeException("图片上传失败: " + e.getMessage());
        }
    }

    @Override
    public Feedback submitFeedback(String type, String content, String contact,String images) {
        Feedback feedback=new Feedback();
        feedback.setContact(contact);
        feedback.setType(type);
        feedback.setContent(content);


        if (images== null ) {
            feedback.setImages(String.join(",", images));
        }

        feedback.setCreateTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());

        int rows = feedbackMapper.insert(feedback);

        if (rows > 0) {
            log.info("反馈提交成功 -  feedbackId: {}", feedback.getId());
            return feedback;
        } else {
            log.error("反馈提交失败");
            return null;
        }

    }
}
