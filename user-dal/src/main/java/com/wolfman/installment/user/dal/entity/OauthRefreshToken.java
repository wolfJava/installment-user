package com.wolfman.installment.user.dal.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OauthRefreshToken {

    private String refreshToken;

    private Long userId;

    private String authorizationCode;

    private String clientId;

    private String device;

    private Date expiration;

    private String grantType;

    private String ipAddress;

    private String responseType;

    private Boolean revoked = false;

    private String scope;

    private String userAgent;

    private Date createTime = new Date();

    private Date modifyTime = createTime;


}