package com.wolfman.installment.user.dal.mapper;


import com.wolfman.installment.user.dal.entity.OauthClient;

public interface OauthClientMapper {
    int deleteByPrimaryKey(String clientId);

    int insert(OauthClient record);

    int insertSelective(OauthClient record);

    OauthClient selectByPrimaryKey(String clientId);

    int updateByPrimaryKeySelective(OauthClient record);

    int updateByPrimaryKey(OauthClient record);



}