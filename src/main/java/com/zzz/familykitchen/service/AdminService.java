package com.zzz.familykitchen.service;

import com.zzz.familykitchen.pojo.dto.AdminLoginDTO;
import com.zzz.familykitchen.pojo.entity.Admin;

public interface AdminService {
    Admin login(AdminLoginDTO adminLoginDTO);
}
