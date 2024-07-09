package org.software.code.service;

import org.software.code.dto.GetItineraryDto;
import org.software.code.dto.PlaceStatusDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItineraryCodeService {

    List<PlaceStatusDto> getItineraryCodeList(long uid);

    void cleanItinerary();

    long extractUidValidateToken(String token);

    GetItineraryDto getItinerary(long uid);
}
