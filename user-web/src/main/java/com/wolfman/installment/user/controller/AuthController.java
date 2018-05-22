package com.wolfman.installment.user.controller;

import com.wanjiafenqi.installment.common.bean.ResponseCode;
import com.wanjiafenqi.installment.common.utils.RequestUtils;
import com.wolfman.installment.user.model.TokenRequest;
import com.wolfman.installment.user.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthController
 * @Description token相关认证
 * @Author huhao
 * @Date Create in 2018/5/18 15:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/token")
    public Map<String, Object> token(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam(name = "clientId", required = true) String clientId,
                                     @RequestParam(value = "grantType", required = true) String grantType,
                                     @RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "password", required = false) String password,
                                     @RequestParam(value = "refreshToken", required = false) String refreshToken,
                                     @RequestParam(value = "device", required = false) String device){
        Map<String, Object> result = new HashMap<String, Object>();
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientId(clientId);
        tokenRequest.setGrantType(grantType);
        tokenRequest.setUsername(username);
        tokenRequest.setPassword(password);
        tokenRequest.setRefreshToken(refreshToken);
        tokenRequest.setDevice(device);
        String ua = request.getHeader("User-Agent");
        if (StringUtils.isNotBlank(ua)) {
            tokenRequest.setUserAgent(StringUtils.substring(ua, 0, 500));
        }
        tokenRequest.setDevice(device);
        tokenRequest.setIpAddress(RequestUtils.getIpAddress(request));

        //step1.判断参数GrantTyp与clientId是否合法
        if (!authService.validateGrantType(tokenRequest)) {
            ResponseCode.put(result, ResponseCode.PARAM_INVALID,"无效的参数：client_id or grant_type");
            return result;
        }
        if (StringUtils.equals(grantType, "password")){
            if (StringUtils.isBlank(username)) {
                ResponseCode.put(result, ResponseCode.PARAM_EMPTY, "参数不能为空：username");
                return result;
            } else if (StringUtils.isBlank(password)) {
                ResponseCode.put(result, ResponseCode.PARAM_EMPTY, "参数不能为空：password");
                return result;
            }
            return authService.generateAccessTokenByPassword(tokenRequest);
        }if (StringUtils.equals(grantType, "refresh_token")){
            //step1.校验refresh_token是否为空
            return authService.generateAccessTokenByRefreshToken(tokenRequest);
        } else {
            ResponseCode.put(result, ResponseCode.PARAM_INVALID, "无效的参数 grant_type");
            return result;
        }
    }

    @PostMapping("/revoke")
    public Map<String, Object> revoke(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("accessToken") String accessToken){
        Map<String, Object> result = new HashMap<String, Object>();
        //step1.判断accessToken是否为空
        if (StringUtils.isBlank(accessToken)) {
            ResponseCode.put(result, ResponseCode.PARAM_EMPTY,"参数不能为空：accessToken");
            return result;
        }else{
            return authService.revoke(accessToken);
        }
    }



}
