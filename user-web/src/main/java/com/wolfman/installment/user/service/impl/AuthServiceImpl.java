package com.wolfman.installment.user.service.impl;

import com.wanjiafenqi.installment.common.bean.ResponseCode;
import com.wolfman.installment.user.dal.entity.OauthAccessToken;
import com.wolfman.installment.user.dal.entity.OauthClient;
import com.wolfman.installment.user.dal.entity.OauthRefreshToken;
import com.wolfman.installment.user.dal.entity.UserInfo;
import com.wolfman.installment.user.dal.mapper.OauthAccessTokenMapper;
import com.wolfman.installment.user.dal.mapper.OauthClientMapper;
import com.wolfman.installment.user.dal.mapper.OauthRefreshTokenMapper;
import com.wolfman.installment.user.dal.mapper.UserInfoMapper;
import com.wolfman.installment.user.model.ResourceOwner;
import com.wolfman.installment.user.model.TokenRequest;
import com.wolfman.installment.user.model.TokenResponse;
import com.wolfman.installment.user.service.AuthService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.UnaryOperator;

/**
 * @ClassName AuthServiceImpl
 * @Author huhao
 * @Date Create in 2018/5/18 15:25
 * @Version 1.0
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private OauthClientMapper oauthClientMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private OauthRefreshTokenMapper oauthRefreshTokenMapper;

    @Autowired
    private OauthAccessTokenMapper oauthAccessTokenMapper;


    @Override
    public boolean validateGrantType(TokenRequest tokenRequest) {
        if (StringUtils.isNotBlank(tokenRequest.getClientId())) {
            if (StringUtils.isNotBlank(tokenRequest.getGrantType())){
                OauthClient oauthClient = oauthClientMapper.selectByPrimaryKey(tokenRequest.getClientId());
                if (oauthClient != null && StringUtils.isNotBlank(oauthClient.getAuthroizedGrantTypes())){
                    return StringUtils.equalsAnyIgnoreCase(tokenRequest.getGrantType(), StringUtils.split(oauthClient.getAuthroizedGrantTypes(), ","));
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> generateAccessTokenByPassword(TokenRequest tokenRequest) {
        Map<String, Object> result = new HashMap<>();
        //step1.判断用户是否存在
        OauthClient oauthClient = oauthClientMapper.selectByPrimaryKey(tokenRequest.getClientId());
        ResourceOwner resourceOwner = validateUser(oauthClient, tokenRequest.getUsername(), tokenRequest.getPassword());
        if (null != resourceOwner){
            //step1.根据配置生成RefreshToken
            OauthRefreshToken oauthRefreshToken = generateRefreshToken(tokenRequest,oauthClient, resourceOwner.getId(),
                    oauthClient.getRefreshTokenExpiresIn());
            String refreshToken = oauthRefreshToken == null ? "":oauthRefreshToken.getRefreshToken();
            OauthAccessToken oauthAccessToken =  generateAccessToken(tokenRequest, refreshToken, oauthClient, resourceOwner);
            if (null == oauthAccessToken){
                ResponseCode.put(result, ResponseCode.UNKNOWN);
                return result;
            }
            TokenResponse tokenResponse = toTokenResponse(oauthAccessToken);
            result.put("data",tokenResponse);
            ResponseCode.putSuccess(result);
            return result;
        } else {
            ResponseCode.put(result, ResponseCode.PHONENUMBER_PASSWORD_NOT_MATCH);
            return result;
        }
    }

    @Override
    public Map<String, Object> generateAccessTokenByRefreshToken(TokenRequest tokenRequest) {
        Map<String, Object> result = new HashMap<>();
        //step1.判断refreshToken是否为空
        if (StringUtils.isNotBlank(tokenRequest.getRefreshToken())) {
            OauthRefreshToken refreshToken = oauthRefreshTokenMapper.selectByPrimaryKey(tokenRequest.getRefreshToken());
            if (refreshToken != null){
                if (validateRefreshToken(refreshToken)) {
                    List<OauthAccessToken> oldAccessTokens = oauthAccessTokenMapper.selectByUserIdAndRevoked(refreshToken.getUserId(),false);
                    if (oldAccessTokens.size() > 0) {
                        oldAccessTokens.replaceAll(new UnaryOperator<OauthAccessToken>() {
                            @Override
                            public OauthAccessToken apply(OauthAccessToken oauthAccessToken) {
                                oauthAccessToken.setRevoked(true);
                                return oauthAccessToken;
                            }
                        });
                        oauthAccessTokenMapper.updateBatch(oldAccessTokens);
                    }

                    //获取client客户端信息
                    OauthClient oauthClient = oauthClientMapper.selectByPrimaryKey(tokenRequest.getClientId());
                    ResourceOwner resourceOwner = new ResourceOwner();
                    resourceOwner.setId(refreshToken.getUserId());
                    OauthAccessToken oauthAccessToken =  generateAccessToken(tokenRequest, refreshToken.getRefreshToken(), oauthClient, resourceOwner);
                    if (null == oauthAccessToken){
                        ResponseCode.put(result, ResponseCode.UNKNOWN);
                        return result;
                    }
                    TokenResponse tokenResponse = toTokenResponse(oauthAccessToken);
                    result.put("data",tokenResponse);
                    ResponseCode.putSuccess(result);
                    return result;
                } else {
                    ResponseCode.put(result, ResponseCode.PARAM_INVALID, "无效的参数：refresh_token");
                    return result;
                }
            }else {
                ResponseCode.put(result, ResponseCode.PARAM_INVALID, "无效的参数：refresh_token");
                return result;
            }
        }else {
            ResponseCode.put(result, ResponseCode.PARAM_EMPTY, "参数不能为空：refresh_token");
            return result;
        }
    }


    private boolean validateRefreshToken(OauthRefreshToken refreshToken) {
        return refreshToken != null && !refreshToken.getRevoked()
                && (refreshToken.getExpiration().getTime() > System.currentTimeMillis());
    }


    /**
     * @Author huhao
     * @Description 校验用户
     * @DATE 16:13 2018/5/18
     * @Param [clientId, username, password]
     * @return com.wanjiafenqi.installment.user.model.ResourceOwner
     **/
    private ResourceOwner validateUser(OauthClient oauthClient, String username, String password){

        if (oauthClient.getAccountType().equals("0")){
            //后台用户

        }else if (oauthClient.getAccountType().equals("1")){
            //前端用户
            UserInfo userInfo = userInfoMapper.selectByPhoneNumberAndPassword(username, DigestUtils.sha256Hex(password));
            if (null != userInfo){
                ResourceOwner respuser = new ResourceOwner();
                respuser.setId(userInfo.getId());
                respuser.setAccountType(oauthClient.getAccountType());
                return respuser;
            }
        }
        return null;
    }


    /**
     * @Author huhao
     * @Description 生成refreshToken
     * @DATE 17:16 2018/5/18
     * @Param [tokenRequest, userId, refreshTokenExpiresIn]
     * @return com.wanjiafenqi.installment.dal.entity.OauthRefreshToken
     **/
    private OauthRefreshToken generateRefreshToken(TokenRequest tokenRequest, OauthClient oauthClient, Long userId, int refreshTokenExpiresIn) {
        OauthRefreshToken refreshToken = new OauthRefreshToken();
        if (oauthClient.getRefreshTokenExpiresIn() > 0) {
            //step1.查询所有该用户refreshToken,设置其失效状态
            List<OauthRefreshToken> refreshTokenOlds = oauthRefreshTokenMapper.selectByUserIdAndRevoked(userId, false);
            if (refreshTokenOlds.size() > 0) {
                refreshTokenOlds.replaceAll(new UnaryOperator<OauthRefreshToken>() {
                    @Override
                    public OauthRefreshToken apply(OauthRefreshToken oauthRefreshToken) {
                        oauthRefreshToken.setRevoked(true);
                        return oauthRefreshToken;
                    }
                });
                oauthRefreshTokenMapper.updateBatch(refreshTokenOlds);
            }
            //step2.创建生成新的refreshToken
            OauthRefreshToken oauthRefreshToken = new OauthRefreshToken();
            oauthRefreshToken.setRefreshToken(UUID.randomUUID().toString().replace("-", ""));
            oauthRefreshToken.setGrantType(tokenRequest.getGrantType());
            oauthRefreshToken.setScope(tokenRequest.getScope());
            oauthRefreshToken.setUserId(userId);
            oauthRefreshToken.setDevice(tokenRequest.getDevice());
            oauthRefreshToken.setUserAgent(tokenRequest.getUserAgent());
            oauthRefreshToken.setDevice(tokenRequest.getDevice());
            oauthRefreshToken.setIpAddress(tokenRequest.getIpAddress());
            oauthRefreshToken.setExpiration(DateUtils.addSeconds(new Date(), refreshTokenExpiresIn));
            oauthRefreshToken.setClientId(tokenRequest.getClientId());
            oauthRefreshTokenMapper.insert(oauthRefreshToken);
            return oauthRefreshToken;
        }
        return null;
    }

    private OauthAccessToken generateAccessToken(TokenRequest tokenRequest, String refreshToken,
                                                 OauthClient oauthClient, ResourceOwner resourceOwner){
        if (oauthClient.getAccessTokenExpiresIn() > 0) {
            OauthAccessToken oauthAccessToken = new OauthAccessToken();
            oauthAccessToken.setAccessToken(UUID.randomUUID().toString().replace("-", ""));
            oauthAccessToken.setRefreshToken(refreshToken);
            oauthAccessToken.setGrantType(tokenRequest.getGrantType());
            oauthAccessToken.setScope(tokenRequest.getScope());
            Date now = new Date();
            Date exp = DateUtils.addSeconds(now, oauthClient.getAccessTokenExpiresIn());
            oauthAccessToken.setExpiration(exp);
            oauthAccessToken.setClientId(tokenRequest.getClientId());
            // 自定义扩展的属性
            oauthAccessToken.setUserId(resourceOwner.getId());
            oauthAccessToken.setUserPermission(resourceOwner.getPermission());
            oauthAccessToken.setDevice(tokenRequest.getDevice());
            oauthAccessToken.setUserAgent(tokenRequest.getUserAgent());
            oauthAccessToken.setIpAddress(tokenRequest.getIpAddress());
            oauthAccessTokenMapper.insert(oauthAccessToken);
            return oauthAccessToken;
        }
        return null;
    }

    private TokenResponse toTokenResponse(OauthAccessToken accessToken) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken.getAccessToken());
        tokenResponse.setRefreshToken(accessToken.getRefreshToken());
        tokenResponse.setTokenType(accessToken.getGrantType());
        long expiresIn = 0;
        if (accessToken.getExpiration() != null) {
            expiresIn = (accessToken.getExpiration().getTime() - System.currentTimeMillis()) / 1000;
        }
        tokenResponse.setExpiresIn(expiresIn);
        tokenResponse.setExpires(accessToken.getExpiration().getTime());
        tokenResponse.setUserId(accessToken.getUserId());
        if (StringUtils.isNotBlank(accessToken.getUserPermission())) {
            tokenResponse.setPermissions(accessToken.getUserPermission());
        }
        //todo implement openid
        return tokenResponse;
    }

    @Override
    public Map<String, Object> revoke(String accessToken) {
        Map<String, Object> result = new HashMap<>();
        OauthAccessToken oauthAccessToken = oauthAccessTokenMapper.selectByPrimaryKey(accessToken);
        if (oauthAccessToken != null){
            OauthRefreshToken oauthRefreshToken = oauthRefreshTokenMapper.selectByPrimaryKey(oauthAccessToken.getRefreshToken());
            oauthRefreshToken.setRevoked(true);
            oauthRefreshTokenMapper.updateByPrimaryKey(oauthRefreshToken);
            oauthAccessToken.setRevoked(true);
            oauthAccessTokenMapper.updateByPrimaryKey(oauthAccessToken);
            ResponseCode.putSuccess(result);
            return result;
        }else {
            ResponseCode.put(result, ResponseCode.PARAM_INVALID, "无效的参数：refresh_token");
            return result;
        }
    }







}
