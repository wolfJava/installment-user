package com.wolfman.installment.user.model;

import lombok.Data;

@Data
public class ResourceOwner {

    Long id;

    String openId;

    /**
     * 账户类型：0:后台	1:前端
     **/
    String accountType;

    String permission;


}
