package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.HealthCodeDao;

@Mapper
public interface HealthCodeMapper {
    @Insert("INSERT INTO health_code_health_code.health_code (uid, color) VALUES (#{uid}, #{color});")
    void addHealthCode(HealthCodeDao healthCodeDao);

    @Update("UPDATE health_code_health_code.health_code SET color = #{color} WHERE uid = #{uid}")
    void updateColorByUID(@Param("color") int color, @Param("uid") long uid);

    @Select("SELECT * FROM health_code_health_code.health_code WHERE uid = #{uid} limit 1")
    HealthCodeDao getHealthCodeByUID(long uid);


}
