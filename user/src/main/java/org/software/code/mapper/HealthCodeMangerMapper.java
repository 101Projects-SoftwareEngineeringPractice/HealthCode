package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.HealthCodeManagerDao;

import java.util.List;


@Mapper
public interface HealthCodeMangerMapper {

    @Insert("INSERT INTO health_code_user.health_code_manager (mid, identity_card, password_hash, name,status) VALUES (#{mid}, #{identity_card}, #{password_hash}, #{name}, #{status});")
    void addHealthCodeManager(HealthCodeManagerDao healthCodeManagerDao);

    @Update("UPDATE health_code_user.health_code_manager SET status = #{status} WHERE mid = #{mid}")
    void updateStatusByMID(@Param("status") boolean status, @Param("mid") long mid);


    @Select("SELECT * FROM health_code_user.health_code_manager")
    List<HealthCodeManagerDao> getHealthCodeManagerList();


    @Select("SELECT * FROM health_code_user.health_code_manager WHERE identity_card = #{identity_card} limit 1")
    HealthCodeManagerDao getHealthCodeManagerByID(String identityCard);

    @Select("SELECT * FROM health_code_user.health_code_manager WHERE mid = #{mid} limit 1")
    HealthCodeManagerDao getHealthCodeManagerByMID(long mid);


}
