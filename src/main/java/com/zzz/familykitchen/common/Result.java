package com.zzz.familykitchen.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 返回的结果类型
 *
 * @author 张琦松
 * @since 2025.11.23
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {
    private Integer code;      // 状态码
    private String message;    // 提示信息
    private T data;           // 返回数据

    // 成功(带数据)
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 成功(无数据)
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    // 自定义状态码
    public static <T> Result<T> error(ResultCode code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code.getCode());
        result.setMessage(message);
        return result;
    }
}