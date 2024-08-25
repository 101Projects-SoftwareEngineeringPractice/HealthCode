package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.model.entity.HealthCodeManager;

import java.util.List;
@Mapper
public interface HealthCodeMangerDao {

    void addHealthCodeManager(HealthCodeManager healthCodeManager);

    void updateStatusByMID(@Param("status") boolean status,@Param("mid") long mid);

    List<HealthCodeManager> getHealthCodeManagerList();

    HealthCodeManager getHealthCodeManagerByID(String identityCard);

    HealthCodeManager getHealthCodeManagerByMID(long mid);
}
