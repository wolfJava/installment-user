package com.wolfman.installment.user.dal.entity;

import lombok.Data;
import lombok.Setter;

import java.util.Date;

@Data
public class UserInfo {

    private Long id;

    private String channel;

    private String description;

    private String email;

    private Integer enabledState = 1;

    private String openId;

    private String password;

    private String phoneNumber;

    private Long refererId;

    private String userName;

    private String uuid;

    private Date createTime = new Date();

    private Date modifyTime = createTime;

}