package com.wolfman.installment.user.model;

import lombok.Data;

/**
 * @ClassName TokenResponse
 * @Description token响应结果
 * @Author huhao
 * @Date Create in 2018/5/18 15:19
 * @Version 1.0
 */
@Data
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

    private String tokenType;

    private Long expires;

    private Long userId;

    private String openId;

    private String permissions;


}
