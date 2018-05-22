package com.wolfman.installment.user.dal.mapper;

import java.util.List;

import com.wolfman.installment.user.dal.entity.OauthAccessToken;
import com.wolfman.installment.user.dal.entity.OauthAccessTokenExample;
import org.apache.ibatis.annotations.Param;

public interface OauthAccessTokenMapper {
    int countByExample(OauthAccessTokenExample example);

    int deleteByExample(OauthAccessTokenExample example);

    int deleteByPrimaryKey(String accessToken);

    int insert(OauthAccessToken record);

    int insertSelective(OauthAccessToken record);

    List<OauthAccessToken> selectByExample(OauthAccessTokenExample example);

    OauthAccessToken selectByPrimaryKey(String accessToken);

    int updateByExampleSelective(@Param("record") OauthAccessToken record, @Param("example") OauthAccessTokenExample example);

    int updateByExample(@Param("record") OauthAccessToken record, @Param("example") OauthAccessTokenExample example);

    int updateByPrimaryKeySelective(OauthAccessToken record);

    int updateByPrimaryKey(OauthAccessToken record);

    List<OauthAccessToken> selectByUserIdAndRevoked(@Param("userId") Long userId, @Param("revoked") boolean revoked);

    int updateBatch(List<OauthAccessToken> oauthAccessTokens);

}