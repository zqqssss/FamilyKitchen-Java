package com.zzz.familykitchen.controller;

import com.zzz.familykitchen.common.Result;
import com.zzz.familykitchen.common.ResultCode;
import com.zzz.familykitchen.pojo.entity.User;
import com.zzz.familykitchen.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 登录逻辑
     * @param params
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String,String> params){
        try {
            String code=params.get("code");
            String avatarUrl=params.get("avatarUrl");
            String nickname = params.get("nickname");

            log.info("收到登录请求 nickname：{}",nickname);

            if (code == null || code.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "登录code不能为空");
            }

            if (nickname == null || nickname.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "昵称不能为空");
            }

            // 调用登录服务
            User user = userService.login(code, nickname, avatarUrl);

            log.info("登录成功 - userId: {}, nickname: {}", user.getId(), user.getNickname());
            return Result.success(user); // 成功返回：code=200 + 用户数据
        }catch (Exception e){
            log.error("登录失败", e);
            return Result.error("登录失败: " + e.getMessage()); // 失败返回：code=500 + 异常信息
        }

    }


    /**
     * 头像文件上传
     * @param userId
     * @param avatar
     * @return
     */
    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(
            @RequestParam("userId") Long userId,
            @RequestParam("avatar") MultipartFile avatar) {

        try {
            log.info("收到头像上传请求 - userId: {}", userId);

            // 参数校验
            if (avatar.isEmpty()) {
                return Result.error(ResultCode.PARAM_ERROR, "头像文件不能为空");
            }

            // 文件大小限制(5MB)
            if (avatar.getSize() > 5 * 1024 * 1024) {
                return Result.error(ResultCode.PARAM_ERROR, "头像文件不能超过5MB");
            }

            // 文件类型检查
            String contentType = avatar.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error(ResultCode.PARAM_ERROR, "只能上传图片文件");
            }

            // 调用上传服务
            String avatarUrl = userService.uploadAvatar(userId, avatar);

            log.info("头像上传成功 - userId: {}, url: {}", userId, avatarUrl);
            return Result.success(avatarUrl); // 成功返回：code=200 + 头像URL

        } catch (Exception e) {
            log.error("头像上传失败", e);
            return Result.error("头像上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取已有用户信息
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result<User> getUserInfo(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);

            if (user == null) {
                return Result.error(ResultCode.NOT_FOUND, "用户不存在"); // 404状态码
            } else {
                return Result.success(user); // 200 + 用户信息
            }

        } catch (Exception e) {
            log.error("查询用户信息失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }



}
