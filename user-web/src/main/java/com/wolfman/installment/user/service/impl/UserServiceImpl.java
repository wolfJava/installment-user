package com.wolfman.installment.user.service.impl;


import com.wanjiafenqi.installment.common.bean.ResponseCode;
import com.wolfman.installment.user.dal.entity.UserInfo;
import com.wolfman.installment.user.dal.mapper.UserInfoMapper;
import com.wolfman.installment.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Map<String, Object> queryByUserId(Long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        result.put("userName",userInfo.getUserName());
        ResponseCode.putSuccess(result);
        return result;
    }

    @Override
    public boolean isExistPhoneNumber(String phoneNumber) {
        UserInfo userInfo = userInfoMapper.selectByPhoneNumber(phoneNumber);
        if (userInfo!=null){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> regist(UserInfo userInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int a = userInfoMapper.insert(userInfo);
        if (a>0){
            ResponseCode.putSuccess(resultMap);
            return resultMap;
        }
        ResponseCode.put(resultMap,ResponseCode.FAIL);
        return resultMap;
    }

    @Override
    public Map<String, Object> forgetPasswordModifyByPhoneNumber(String phoneNumber, String password) {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfo userInfo = userInfoMapper.selectByPhoneNumber(phoneNumber);
        userInfo.setPassword(DigestUtils.sha256Hex(password));
        userInfo.setModifyTime(new Date());
        userInfoMapper.updateByPrimaryKey(userInfo);
        ResponseCode.putSuccess(result);
        return result;
    }


}
