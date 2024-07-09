package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.HealthCodeManagerDao;

import java.util.List;


@Mapper
public interface HealthCodeMangerMapper {
    //    @Update("UPDATE health_code_user.user_info SET district = #{district} WHERE uid = #{uid}")
//    void updateUser(@Param("district") int district, @Param("uid") long uid);
//
    @Select("SELECT * FROM health_code_user.health_code_manager")
    List<HealthCodeManagerDao> getHealthCodeManagerList();

    @Insert("INSERT INTO health_code_user.health_code_manager (mid, identity_card, password_hash, name,status) VALUES (#{mid}, #{identity_card}, #{password_hash}, #{name}, #{status});")
    void addHealthCodeManager(HealthCodeManagerDao healthCodeManagerDao);

    @Select("SELECT * FROM health_code_user.health_code_manager WHERE identity_card = #{identity_card} limit 1")
    HealthCodeManagerDao getHealthCodeManagerByID(String identityCard);

    @Select("SELECT * FROM health_code_user.health_code_manager WHERE mid = #{mid} limit 1")
    HealthCodeManagerDao getHealthCodeManagerByMID(long mid);

    @Update("UPDATE health_code_user.health_code_manager SET status = #{status} WHERE mid = #{mid}")
    void updateStatusByMID(@Param("status") boolean status, @Param("mid") long mid);

}
