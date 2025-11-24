package com.zzz.familykitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzz.familykitchen.mapper.AdminMapper;
import com.zzz.familykitchen.pojo.dto.AdminLoginDTO;
import com.zzz.familykitchen.pojo.entity.Admin;
import com.zzz.familykitchen.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(AdminLoginDTO adminLoginDTO) {
        String username = adminLoginDTO.getUsername();
        String password = adminLoginDTO.getPassword();

        // 1. 使用 LambdaQueryWrapper 查询数据库
        // 解释：查询 Admin 表，条件是 name 字段等于传入的 username
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getName, username);

        Admin admin = adminMapper.selectOne(queryWrapper);

        // 2. 处理各种异常情况
        if (admin == null) {
            throw new RuntimeException("账号不存在");
        }

        // 3. 密码比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 4. 返回实体对象
        return admin;
    }
}
