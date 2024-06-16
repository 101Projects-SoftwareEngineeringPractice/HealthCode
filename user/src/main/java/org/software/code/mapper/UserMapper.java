package org.software.code.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.software.code.dao.UserInfoDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Select("Select * from health_code_user.user_info where uid = #{uid} limit 1")
    UserInfoDao find(@Param("uid") long uid);

    @Update("UPDATE health_code_user.user_info SET district = #{district} WHERE uid = #{uid}")
    void updateUser(@Param("district") int district, @Param("uid") long uid);

    @Delete("DELETE FROM health_code_user.user_info WHERE uid = #{uid}")
    void deleteUser(@Param("uid") Long uid);
}
