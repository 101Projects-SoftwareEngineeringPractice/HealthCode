package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.software.code.model.entity.ItineraryCode;
import java.util.Date;
import java.util.List;
@Mapper
public interface ItineraryCodeDao {
    void addItineraryCodeDao(ItineraryCode itineraryCode);

    void deleteItineraryCodeBeforeTime(Date time);

    List<ItineraryCode> getItineraryCodeListByUID(long uid);
}
