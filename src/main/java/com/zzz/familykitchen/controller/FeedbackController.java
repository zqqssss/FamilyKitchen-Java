package com.zzz.familykitchen.controller;

import com.zzz.familykitchen.common.Result;
import com.zzz.familykitchen.common.ResultCode;
import com.zzz.familykitchen.pojo.dto.FeedbackDTO;
import com.zzz.familykitchen.pojo.entity.Feedback;
import com.zzz.familykitchen.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    /**
     * 上传反馈图片
     */
    @PostMapping("/uploadImage")
    public Result<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            log.info("收到反馈图片上传请求 - 文件名: {}", image.getOriginalFilename());

            if (image.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "图片不能为空");
            }

            if (image.getSize() > 10 * 1024 * 1024) {
                return Result.error(ResultCode.PARAM_ERROR, "图片大小不能超过10MB");
            }

            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error(ResultCode.PARAM_ERROR, "只能上传图片文件");
            }

            String imageUrl = feedbackService.uploadFeedbackImage(image);

            log.info("反馈图片上传成功 - url: {}", imageUrl);
            return Result.success(imageUrl);

        } catch (Exception e) {
            log.error("反馈图片上传失败", e);
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 提交反馈
     */
    @PostMapping("/submit")
    public Result<Feedback> submit(@RequestBody FeedbackDTO dto){
        try{
            log.info("收到反馈提交 - userId: {}, type: {}, content: {}",
                    dto.getUserId(), dto.getType(), dto.getContent());

            // 将图片列表转换为字符串
            String images = null;
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                images = String.join(",", dto.getImages());
            }

            Feedback feedback = feedbackService.submitFeedback(
                    dto.getType(),
                    dto.getContent(),
                    dto.getContact(),
                    images
            );

            if (feedback != null) {
                log.info("反馈提交成功 - feedbackId: {}", feedback.getId());
                return Result.success(feedback);
            } else {
                return Result.error("反馈提交失败");
            }

        } catch (Exception e) {
            log.error("反馈提交失败", e);
            return Result.error("反馈提交失败: " + e.getMessage());
        }
    }
}
