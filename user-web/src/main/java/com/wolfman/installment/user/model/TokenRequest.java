package com.wolfman.installment.user.model;

import lombok.Data;

/**
 * Created by qianz on 2017/2/8.
 */
@Data
public class TokenRequest {

    String clientId;

    String grantType;

    String code;

    String username;

    String password;

    String scope;

    String refreshToken;

    String device;

    String userAgent;

    String ipAddress;

    String openId;

    String appId;


}
