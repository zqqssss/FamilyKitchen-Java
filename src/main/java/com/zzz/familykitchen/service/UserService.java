package com.zzz.familykitchen.service;

import com.zzz.familykitchen.pojo.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户服务接口
 */

public interface UserService {


    User login(String code, String nickname, String avatarUrl) throws IOException;


    String uploadAvatar(Long userId, MultipartFile avatar) throws IOException;


    User getUserById(Long userId);
}
