package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.model.entity.HealthCode;

@Mapper
public interface HealthCodeDao {
    void addHealthCode(HealthCode healthCode);

    void updateColorByUID(@Param("color") int color, @Param("uid") long uid);

    HealthCode getHealthCodeByUID(long uid);
}
