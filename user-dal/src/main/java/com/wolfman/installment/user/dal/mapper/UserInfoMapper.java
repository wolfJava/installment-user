package com.wolfman.installment.user.dal.mapper;

import com.wolfman.installment.user.dal.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo selectByPhoneNumber(String phoneNumber);

    UserInfo selectByPhoneNumberAndPassword(@Param("phoneNumber") String phoneNumber, @Param("password")String passwordDigest);


}