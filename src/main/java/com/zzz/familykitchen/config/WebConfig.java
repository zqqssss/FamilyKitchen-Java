package com.zzz.familykitchen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 确保 uploadPath 以 / 或 \ 结尾，防止路径拼接错误
        String basePath = uploadPath;
        if (!basePath.endsWith("/") && !basePath.endsWith(File.separator)) {
            basePath += File.separator;
        }

        // 2. 打印日志，确认最终映射路径是否正确 (重要调试信息!)
        String recipePath = "file:" + basePath + "recipes" + File.separator;
        System.out.println("🔴 [WebConfig] 菜品图片映射路径: " + recipePath);

        // 3. 配置映射
        registry.addResourceHandler("/images/recipes/**")
                .addResourceLocations(recipePath);
    }
}
