package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.UserInfoDao;

@Mapper
public interface UserInfoMapper {

    @Insert("INSERT INTO health_code_user.user_info " +
            "(uid, name, phone_number, identity_card, district, street, community, address) " +
            "VALUES (#{uid}, #{name}, #{phone_number}, #{identity_card}, #{district}, #{street}, #{community}, #{address});")
    void addUserInfo(UserInfoDao userInfoDao);

    @Update("   UPDATE health_code_user.user_info\n" +
            "        SET\n" +
            "            name = #{name},\n" +
            "            phone_number = #{phone_number},\n" +
            "            identity_card = #{identity_card},\n" +
            "            district = #{district},\n" +
            "            street = #{street},\n" +
            "            community = #{community},\n" +
            "            address = #{address}\n" +
            "        WHERE uid = #{uid}")
    void updateUserInfo(UserInfoDao userInfoDao);


    @Delete("DELETE FROM health_code_user.user_info WHERE uid = #{uid}")
    void deleteById(long uid);

    @Select("SELECT * FROM health_code_user.user_info WHERE uid = #{uid} limit 1")
    UserInfoDao getUserInfoByUID(long uid);

    @Select("SELECT * FROM health_code_user.user_info WHERE identity_card = #{identity_card} limit 1")
    UserInfoDao getUserInfoByID(String identity_card);



    @Select("SELECT COUNT(*) FROM health_code_user.user_info WHERE uid = #{uid}")
    boolean existsById(long uid);


    @Select("SELECT * FROM health_code_user.user_info WHERE phone_number = #{phone_number} limit 1")
    UserInfoDao getUserInfoByPhone(String phone_number);
}
