package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.model.entity.PlaceMapping;

import java.util.Date;
import java.util.List;
@Mapper
public interface PlaceMappingDao {

    void insertPlaceMapping(PlaceMapping placeMapping);

    List<Long> findPidsByUidListAndTimeRange(@Param("uidList") List<Long> uidList,@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    List<Long> findUidsByPidAndTimeRange(@Param("pid")long pid,@Param("startTime") Date startTime,@Param("endTime") Date endTime);
}
