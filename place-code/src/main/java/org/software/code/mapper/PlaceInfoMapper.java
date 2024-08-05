package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.PlaceInfoDao;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlaceInfoMapper {

    @Insert("INSERT INTO health_code_place_code.place_info (pid, uid, identity_card, name, district, street, community, address, status) " +
            "VALUES (#{pid}, #{uid}, #{identity_card}, #{name}, #{district}, #{street}, #{community}, #{address}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "pid")
    void insertPlace(PlaceInfoDao placeDao);

    @Update("UPDATE health_code_place_code.place_info SET status = #{status} WHERE pid = #{pid}")
    void updatePlaceStatusByPid(@Param("status") boolean status,@Param("pid") long pid);


    @Select("SELECT * FROM health_code_place_code.place_info")
    List<PlaceInfoDao> findAllPlaces();

    @Select("SELECT uid FROM health_code_place_code.place_info WHERE pid = #{pid} AND time BETWEEN #{startTime} AND #{endTime}")
    List<Long> findUidsByPidAndTimeRange(@Param("pid") long pid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    @Select("SELECT * FROM health_code_place_code.place_info WHERE pid = #{pid} limit 1")
    PlaceInfoDao getPlaceInfoByPID(@Param("pid") long pid);



}
