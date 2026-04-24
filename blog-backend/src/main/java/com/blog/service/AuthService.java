package com.blog.service;

import com.blog.dto.LoginRequest;
import com.blog.dto.LoginResponse;
import com.blog.dto.RegisterRequest;

public interface AuthService {

    /** 用户注册 */
    LoginResponse register(RegisterRequest request);

    /** 用户登录 */
    LoginResponse login(LoginRequest request);
}
