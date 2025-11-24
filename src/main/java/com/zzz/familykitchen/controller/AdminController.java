package com.zzz.familykitchen.controller;

import com.zzz.familykitchen.common.Result;
import com.zzz.familykitchen.pojo.dto.AdminLoginDTO;
import com.zzz.familykitchen.pojo.vo.AdminLoginVO;
import com.zzz.familykitchen.service.AdminService;
import com.zzz.familykitchen.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import com.zzz.familykitchen.pojo.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    JwtUtil jwtUtil;



    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("员工登录：{}", adminLoginDTO);

        try {
            Admin admin = adminService.login(adminLoginDTO);

            // 登录成功后，生成JWT令牌 (这里用 employee.getId() 作为标识)
            String token = jwtUtil.generateToken(admin.getId());

            AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                    .id(admin.getId())
                    .userName(admin.getName())
                    .token(token)
                    .build();

            return Result.success(adminLoginVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
