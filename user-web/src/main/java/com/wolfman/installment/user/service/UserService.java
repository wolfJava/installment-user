package com.wolfman.installment.user.service;

import com.wolfman.installment.user.dal.entity.UserInfo;

import java.util.Map;

public interface UserService {

    public Map<String, Object> queryByUserId(Long userId);

    /**
     * @Author huhao
     * @Description 判断手机号是否注册
     * @DATE 9:52 2018/5/16
     * @Param [phoneNumber]
     * @return boolean
     **/
    public boolean isExistPhoneNumber(String phoneNumber);

    /**
     * @Author huhao
     * @Description 注册接口
     * @DATE 10:45 2018/5/16
     * @Param [userInfo]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> regist(UserInfo userInfo);


    /**
     * 根据手机号修改密码
     * @Author huhao
     * @DATE 14:53 2018/5/22
     * @Param [phoneNumber, password]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public Map<String, Object> forgetPasswordModifyByPhoneNumber(String phoneNumber,String password);




}
