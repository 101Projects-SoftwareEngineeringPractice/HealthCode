package org.software.code.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.software.code.dao.PlaceMappingDao;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlaceMappingMapper {

    @Insert("INSERT INTO place_mapping (pid, uid, time) VALUES (#{pid}, #{uid}, #{time})")
    void insertPlaceMapping(PlaceMappingDao placeMappingDao);

    @Select({
            "<script>",
            "SELECT DISTINCT pid FROM place_mapping",
            "WHERE uid IN",
            "<foreach item='item' index='index' collection='uidList' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "AND time BETWEEN #{startTime} AND #{endTime}",
            "</script>"
    })
    List<Long> findPidsByUidListAndTimeRange(@Param("uidList") List<Long> uidList, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
