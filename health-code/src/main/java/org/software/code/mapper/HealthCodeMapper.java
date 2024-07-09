package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.HealthCodeDao;

@Mapper
public interface HealthCodeMapper {
    @Select("SELECT * FROM health_code_health_code.health_code WHERE uid = #{uid} limit 1")
    HealthCodeDao getHealthCodeByUID(long uid);

    @Update("UPDATE health_code_health_code.health_code SET color = #{color} WHERE uid = #{uid}")
    void updateColorByUID(@Param("color") int color, @Param("uid") long uid);

    @Insert("INSERT INTO health_code_health_code.health_code (uid, color) VALUES (#{uid}, #{color});")
    void addHealthCode(HealthCodeDao healthCodeDao);

//    @Select("Select * from health_code_user.user_info where uid = #{uid} limit 1")
//    UserInfoDao find(@Param("uid") long uid);

//    @Update("UPDATE health_code_user.user_info SET district = #{district} WHERE uid = #{uid}")
//    void updateUser(@Param("district") int district, @Param("uid") long uid);

//    @Update("   UPDATE health_code_user.user_info\n" +
//            "        SET\n" +
//            "            name = #{name},\n" +
//            "            phone_number = #{phone_number},\n" +
//            "            identity_card = #{identity_card},\n" +
//            "            district = #{district},\n" +
//            "            street = #{street},\n" +
//            "            community = #{community},\n" +
//            "            address = #{address}\n" +
//            "        WHERE uid = #{uid}")
//    void updateUserInfo(UserInfoDao userInfoDao);
//
//    @Delete("DELETE FROM health_code_user.user_info WHERE uid = #{uid}")
//    void deleteUser(@Param("uid") Long uid);
//
//    @Select("SELECT * FROM health_code_user.user_info WHERE uid = #{uid} limit 1")
//    UserInfoDao getUserInfoByUID(long uid);
//
//    @Select("SELECT * FROM health_code_user.user_info WHERE identity_card = #{identity_card} limit 1")
//    UserInfoDao getUserInfoByID(String identity_card);
//


}
