package com.wolfman.installment.user.dal.entity;

import lombok.Data;
import java.util.Date;

@Data
public class OauthClient {

    private String clientId;

    private String clientSecret;

    private String scope;

    private String authroizedGrantTypes;

    private String redirectionUri;

    private Integer accessTokenExpiresIn;

    private Integer refreshTokenExpiresIn;

    private String accountType;

    private Boolean autoApprove;

    private String description;

    private Date createTime = new Date();

    private Date modifyTime = createTime;


}