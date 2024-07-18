package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.ItineraryCodeDao;

import java.util.Date;
import java.util.List;

@Mapper
public interface ItineraryCodeMapper {
    @Select("SELECT * FROM health_code_itinerary_code.itinerary_code WHERE uid = #{uid}")
    List<ItineraryCodeDao> getItineraryCodeListByUID(long uid);

    @Delete("DELETE FROM health_code_itinerary_code.itinerary_code WHERE time <= #{time}")
    void deleteItineraryCodeBeforeTime(@Param("time") Date time);
}
