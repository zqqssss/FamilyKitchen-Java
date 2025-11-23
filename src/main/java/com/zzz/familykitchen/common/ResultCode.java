package com.zzz.familykitchen.common;

import lombok.Getter;

/**
 * 状态码枚举类型
 *
 * @author 张琦松
 * @since 2025.11.23
 *
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    ERROR(500, "服务器错误"),
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}