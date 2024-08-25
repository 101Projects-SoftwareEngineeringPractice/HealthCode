package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.model.entity.PlaceInfo;

import java.util.List;

@Mapper
public interface PlaceInfoDao {

    void insertPlace(PlaceInfo placeDao);

    void updatePlaceStatusByPid(@Param("status") boolean status,@Param("pid") long pid);

    List<PlaceInfo> findAllPlaces();

    PlaceInfo getPlaceInfoByPID(Long pid);

    List<Long> getAllPids();
}
