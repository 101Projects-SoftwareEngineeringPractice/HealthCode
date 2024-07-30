package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.ItineraryCodeDao;

import java.util.Date;
import java.util.List;

@Mapper
public interface ItineraryCodeMapper {
    @Insert("INSERT INTO health_code_user.itinerary_code " +
            "(id, uid, place, identity_card, time) " +
            "VALUES (#{id}, #{uid}, #{identity_card}, #{time});")
    void addItineraryCodeDao(ItineraryCodeDao itineraryCodeDao);

    @Delete("DELETE FROM health_code_itinerary_code.itinerary_code WHERE time <= #{time}")
    void deleteItineraryCodeBeforeTime(@Param("time") Date time);

    @Select("SELECT * FROM health_code_itinerary_code.itinerary_code WHERE uid = #{uid}")
    List<ItineraryCodeDao> getItineraryCodeListByUID(long uid);


}
