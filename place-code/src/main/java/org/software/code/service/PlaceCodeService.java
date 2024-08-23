package org.software.code.service;

import org.apache.ibatis.annotations.Param;
import org.software.code.model.dto.GetPlaceDto;
import org.software.code.model.dto.PlaceCodeInfoDto;
import org.software.code.model.input.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PlaceCodeService {
    Long addPlace(AddPlaceRequest placeDto);

    List<GetPlaceDto> getPlaces();

    List<Long> getRecordByPid(@Param("pid")long pid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    void oppositePlaceCode(OppositePlaceCodeRequest request);

    void scanPlaceCode(ScanPlaceCodeInput input);

    List<Long> getPlacesByUserList(GetPlacesByUserListRequest request);

    void createPlaceCode(CreatePlaceCodeRequest request);

    List<PlaceCodeInfoDto> getPlaceInfoList();

    void placeCodeOpposite(Long pid);

    List<Long> getAllPids();
}
