package com.zzz.familykitchen.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 订单号生成工具类
 */
public class OrderNumberGenerator {
    
    private static final String PREFIX = "ORD";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();
    
    /**
     * 生成订单号
     * 格式：ORD + yyyyMMddHHmmss + 4位随机码
     * 例如：ORD20241125143025A1B2
     */
    public static String generate() {
        // 获取当前时间
        String timestamp = LocalDateTime.now().format(FORMATTER);
        
//        // 生成4位随机码
//        StringBuilder randomCode = new StringBuilder(4);
//        for (int i = 0; i < 4; i++) {
//            randomCode.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
//        }
        
        return PREFIX + timestamp ;
    }
}
