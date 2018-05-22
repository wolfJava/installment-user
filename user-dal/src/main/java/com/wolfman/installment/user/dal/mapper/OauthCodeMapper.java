package com.wolfman.installment.user.dal.mapper;

import com.wolfman.installment.user.dal.entity.OauthCode;

public interface OauthCodeMapper {
    int deleteByPrimaryKey(String code);

    int insert(OauthCode record);

    int insertSelective(OauthCode record);

    OauthCode selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(OauthCode record);

    int updateByPrimaryKey(OauthCode record);
}