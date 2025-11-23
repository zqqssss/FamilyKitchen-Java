package com.zzz.familykitchen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置 - 配置头像静态资源访问
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:/data/kitchen/avatars/}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置头像访问路径
        // 访问 http://localhost:8080/avatars/xxx.jpg 
        // 映射到 /data/kitchen/avatars/xxx.jpg
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
