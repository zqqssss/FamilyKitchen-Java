package com.zzz.familykitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzz.familykitchen.mapper.UserMapper;
import com.zzz.familykitchen.util.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.zzz.familykitchen.pojo.entity.User;
import com.zzz.familykitchen.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户服务实现类
 *
 * @author 张琦松
 * @since 2025.11.23
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WxUtil wxUtil;

    @Autowired
    private UserMapper userMapper;

    // 头像存储路径(从配置文件读取)
    @Value("${file.upload.path:/data/kitchen/avatars/}")
    private String uploadPath;

    // 头像访问URL前缀
    @Value("${file.upload.url-prefix:/avatars/}")
    private String urlPrefix;

    /**
     * 微信小程序登录 - 实现逻辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User login(String code, String nickname, String avatarUrl) throws IOException {
        log.info("用户登录请求 - nickname: {}", nickname);

        // 1. 调用微信API获取openid
        Map<String, String> sessionData = wxUtil.code2Session(code);
        String openid = sessionData.get("openid");
        log.info("获取到openid: {}", openid);

        // 2. 查询数据库中是否已存在该用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);

        // 3. 如果是新用户,创建用户记录
        if (user == null) {
            log.info("新用户注册 - openid: {}", openid);
            user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl); // 暂时存储微信临时路径
            user.setDeleted("0"); // 未删除
            // createTime 和 updateTime 由 MyBatis Plus 自动填充

            userMapper.insert(user);
            log.info("新用户创建成功 - userId: {}", user.getId());
        } else {
            // 4. 老用户更新信息
            log.info("老用户登录 - userId: {}", user.getId());
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl);
            // updateTime 由 MyBatis Plus 自动填充

            userMapper.updateById(user);
            log.info("用户信息更新成功");
        }

        return user;
    }

    /**
     * 上传头像 - 实现逻辑
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(Long userId, MultipartFile avatarFile) throws IOException {
        log.info("上传头像 - userId: {}, 文件大小: {} bytes", userId, avatarFile.getSize());

        // 1. 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
            log.info("创建上传目录: {}", uploadPath);
        }

        // 2. 生成唯一文件名
        String originalFilename = avatarFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = userId + "_" + System.currentTimeMillis() + extension;

        // 3. 保存文件
        Path filepath = Paths.get(uploadPath, filename);
        Files.write(filepath, avatarFile.getBytes());
        log.info("头像文件保存成功: {}", filepath);

        // 4. 生成访问URL
        String avatarUrl = urlPrefix + filename;

        // 5. 更新数据库中的头像URL
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setAvatarUrl(avatarUrl);
            userMapper.updateById(user);
            log.info("数据库头像URL更新成功: {}", avatarUrl);
        }

        return avatarUrl;
    }

    /**
     * 根据用户ID查询用户信息 - 实现逻辑
     */
    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * ✅ 更新用户信息（新增）
     */
    @Override
    @Transactional
    public User updateUserInfo(Long userId, String nickname, String phone,
            String address, String remark) {

        // 1. 查询用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.warn("用户不存在 - userId: {}", userId);
            return null;
        }

        // 2. 更新字段（只更新非空字段）
        if (nickname != null && !nickname.isEmpty()) {
            user.setNickname(nickname);
        }
        if (phone != null && !phone.isEmpty()) {
            user.setPhone(phone);
        }
        if (address != null && !address.isEmpty()) {
            user.setAddress(address);
        }
        if (remark != null && !remark.isEmpty()) {
            user.setRemarks(remark);
        }

        user.setUpdateTime(LocalDateTime.now());

        // 3. 执行更新
        int rows = userMapper.updateById(user);

        if (rows > 0) {
            log.info("用户信息更新成功 - userId: {}, 更新行数: {}", userId, rows);
            return user; // 返回更新后的对象
        } else {
            log.error("用户信息更新失败 - userId: {}", userId);
            return null;
        }
    }

    @Override
    public java.util.List<User> findAll() {
        return userMapper.selectList(null);
    }
}
