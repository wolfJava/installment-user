package com.wolfman.installment.user.dal.mapper;

import com.wolfman.installment.user.dal.entity.OauthRefreshToken;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OauthRefreshTokenMapper {
    int deleteByPrimaryKey(String refreshToken);

    int insert(OauthRefreshToken record);

    int insertSelective(OauthRefreshToken record);

    OauthRefreshToken selectByPrimaryKey(String refreshToken);

    int updateByPrimaryKeySelective(OauthRefreshToken record);

    int updateByPrimaryKey(OauthRefreshToken record);

    List<OauthRefreshToken> selectByUserIdAndRevoked(@Param("userId") Long userId, @Param("revoked") boolean revoked);

    int updateBatch(List<OauthRefreshToken> oauthRefreshTokens);

}