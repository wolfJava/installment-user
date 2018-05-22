package com.wolfman.installment.user.controller;

import com.wanjiafenqi.installment.common.bean.ResponseCode;
import com.wanjiafenqi.installment.common.utils.AccountUtil;
import com.wolfman.installment.user.dal.entity.UserInfo;
import com.wolfman.installment.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @Author huhao
     * @Description
     * @DATE 9:28 2018/5/16
     * @Param [request, response, mobile, password, smsCode]
     * @return java.util.Map<java.lang.String,?>
     **/
    @PostMapping("/regist")
    public Map<String, Object> wechatRegist(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam(value = "phoneNumber", required = true) String phoneNumber,
                                            @RequestParam(value = "password", required = true) String password,
                                            @RequestParam(value = "smsCode", required = false) String smsCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        //校验参数
        if (!AccountUtil.isPhoneNumber(phoneNumber)){
            //step1.校验手机号是否正确
            ResponseCode.put(result,ResponseCode.MOBILE_INVALID);
            return result;
        }else if (userService.isExistPhoneNumber(phoneNumber)){
            //step2.校验手机号是否已注册
            ResponseCode.put(result,ResponseCode.MOBILE_EXIST);
            return result;
        }else if (!AccountUtil.checkPasswordRule(password)){
            //step2.校验密码是否符合规则
            ResponseCode.put(result,ResponseCode.PASSWORD_NOT_MATCH);
            return result;
        }
        UserInfo userInfo = new UserInfo();
        String uuid = StringUtils.remove(UUID.randomUUID().toString(), '-');
        userInfo.setUuid(uuid);
        userInfo.setPhoneNumber(phoneNumber);
        String digestPassword = DigestUtils.sha256Hex(password);
        userInfo.setPassword(digestPassword);
        return userService.regist(userInfo);
    }

    @PostMapping("/forgetPasswordModify")
    public Map<String, Object> forgetPasswordModify(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestParam(value = "phoneNumber", required = true) String phoneNumber,
                                                    @RequestParam(value = "password", required = true) String password,
                                                    @RequestParam(value = "smsCode", required = false) String smsCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        //校验参数
        if (!AccountUtil.isPhoneNumber(phoneNumber) || !userService.isExistPhoneNumber(phoneNumber)){
            //step1.校验手机号是否正确
            ResponseCode.put(result,ResponseCode.MOBILE_INVALID);
            return result;
        }else if (!AccountUtil.checkPasswordRule(password)){
            //step2.校验密码是否符合规则
            ResponseCode.put(result,ResponseCode.PASSWORD_NOT_MATCH);
            return result;
        }
        //TODU
        //校验短信号码
        return userService.forgetPasswordModifyByPhoneNumber(phoneNumber,password);
    }

    @RequestMapping("/queryByUserId")
    public Map<String, Object> queryByUserId(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam(name = "id",required = true)Long userId){
        return userService.queryByUserId(userId);
    }

}
