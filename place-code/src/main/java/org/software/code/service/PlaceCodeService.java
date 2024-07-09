package org.software.code.service;

import org.software.code.dto.GetPlaceDto;
import org.software.code.dto.PlaceCodeInfoDto;
import org.software.code.dto.AddPlaceInput;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PlaceCodeService {
    public Long addPlace(AddPlaceInput placeDto);

    public List<GetPlaceDto> getPlaces();

    public List<Long> getRecordByPid(long pid, Date startTime, Date endTime);

    public void oppositePlaceCode(long pid, boolean status);

    public void scanPlaceCode(long uid, String token);

    public List<Long> getPlacesByUserList(List<Long> uidList, Date startTime, Date endTime);

    long extractUidValidateToken(String token);

    Boolean validateToken(String token);

    long extractPid(String token);

    void createPlaceCode(String identityCard, String name, int districtId, int streetId, int communityId, String address);

    List<PlaceCodeInfoDto> getPlaceInfoList();

    void placeCodeOpposite(Long pid);
}
