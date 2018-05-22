package com.wolfman.installment.user.dal.entity;

import lombok.Data;
import java.util.Date;

@Data
public class OauthAccessToken {

    private String accessToken;

    private String authorizeCode;

    private String clientId;

    private String device;

    private Date expiration;

    private String grantType;

    private String ipAddress;

    private String refreshToken;

    private Boolean revoked = false;

    private String scope;

    private String userAgent;

    private Long userId;

    private String userPermission;

    private Date createTime = new Date();

    private Date modifyTime = createTime;

}