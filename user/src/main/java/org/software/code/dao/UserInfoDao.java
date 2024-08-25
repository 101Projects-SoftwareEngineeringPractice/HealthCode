package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.software.code.model.entity.UserInfo;
@Mapper
public interface UserInfoDao {

    void addUserInfo(UserInfo userInfo);

    void updateUserInfo(UserInfo userInfo);

    void deleteById(long uid);

    UserInfo getUserInfoByUID(long uid);

    UserInfo getUserInfoByID(String identityCard);

    boolean existsById(long uid);

    UserInfo getUserInfoByPhone(String phoneNumber);
}
