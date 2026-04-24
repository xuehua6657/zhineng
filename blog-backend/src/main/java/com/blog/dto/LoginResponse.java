package com.blog.dto;

import lombok.Data;

/**
 * 登录响应
 */
@Data
public class LoginResponse {

    private String token;
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
}
