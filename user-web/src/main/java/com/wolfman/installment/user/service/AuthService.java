package com.wolfman.installment.user.service;

import com.wolfman.installment.user.model.TokenRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @ClassName AuthService
 * @Description 认证服务
 * @Author huhao
 * @Date Create in 2018/5/18 15:24
 * @Version 1.0
 */
public interface AuthService {


    /**
     * @Author huhao
     * @Description 认证clientId与grantType是否合法
     * @DATE 15:25 2018/5/18
     * @Param [clientId, grantType]
     * @return boolean
     **/
    public boolean validateGrantType(TokenRequest tokenRequest );

    /**
     * 根据账号密码refresh_token，access_token
     * @Author huhao
     * @DATE 15:44 2018/5/22
     * @Param [tokenRequest]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> generateAccessTokenByPassword(TokenRequest tokenRequest);

    /**
     * 根据refresh_token换access_token
     * @Author huhao
     * @DATE 15:44 2018/5/22
     * @Param [tokenRequest]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> generateAccessTokenByRefreshToken(TokenRequest tokenRequest);

    /**
     * 注销accessToken
     * @Author huhao
     * @DATE 16:44 2018/5/22
     * @Param [accessToken]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @Transactional
    public Map<String, Object> revoke(String accessToken);

}