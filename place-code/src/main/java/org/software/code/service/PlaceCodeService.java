package org.software.code.service;

import org.software.code.dto.GetPlaceDto;
import org.software.code.dto.PlaceCodeInfoDto;
import org.software.code.dto.AddPlaceInput;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PlaceCodeService {
    Long addPlace(AddPlaceInput placeDto);

    List<GetPlaceDto> getPlaces();

    List<Long> getRecordByPid(long pid, Date startTime, Date endTime);

    void oppositePlaceCode(long pid, boolean status);

    void scanPlaceCode(long uid, String token);

    List<Long> getPlacesByUserList(List<Long> uidList, Date startTime, Date endTime);


    void createPlaceCode(String identityCard, String name, int districtId, int streetId, int communityId, String address);

    List<PlaceCodeInfoDto> getPlaceInfoList();

    void placeCodeOpposite(Long pid);

}
