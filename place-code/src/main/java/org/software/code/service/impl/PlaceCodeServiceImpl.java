package org.software.code.service.impl;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.software.code.client.UserClient;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.model.entity.PlaceInfo;
import org.software.code.model.entity.PlaceMapping;
import org.software.code.dao.PlaceInfoDao;
import org.software.code.dao.PlaceMappingDao;
import org.software.code.model.dto.GetPlaceDto;
import org.software.code.model.dto.PlaceCodeInfoDto;
import org.software.code.model.dto.UserInfoDto;
import org.software.code.model.input.*;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceCodeServiceImpl implements PlaceCodeService {
    private static final Logger logger = LogManager.getLogger(PlaceCodeServiceImpl.class);


    @Resource
    private PlaceInfoDao placeInfoDao;

    @Resource
    private PlaceMappingDao placeMappingDao;

    @Autowired
    UserClient userClient;

    public Long addPlace(AddPlaceRequest request) {
        Result<?> result = userClient.getUserByUID(request.getUid());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        PlaceInfo placeInfo = new PlaceInfo();
        BeanUtils.copyProperties(request, placeInfo);
        placeInfo.setIdentityCard(userInfo.getIdentityCard());
        placeInfo.setDistrict(request.getDistrict());
        placeInfo.setStreet(request.getStreet());
        placeInfo.setCommunity(placeInfo.getCommunity());
        placeInfo.setAddress(placeInfo.getAddress());
        placeInfo.setStatus(true); // 默认开启
        long snowflakePid = IdUtil.getSnowflake().nextId();
        placeInfo.setPid(snowflakePid);
        placeInfoDao.insertPlace(placeInfo);
        return placeInfo.getPid();
    }

    public List<GetPlaceDto> getPlaces() {
        List<PlaceInfo> placeInfoList = placeInfoDao.findAllPlaces();
        return placeInfoList.stream()
                .map(placeInfo -> {
                    GetPlaceDto getPlaceDto = new GetPlaceDto();
                    BeanUtils.copyProperties(placeInfo, getPlaceDto);
                    Result<?> result = userClient.getUserByUID(placeInfo.getUid());
                    ObjectMapper objectMapper = new ObjectMapper();
                    UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
                    getPlaceDto.setPhoneNumber(userInfo.getPhoneNumber());
                    return getPlaceDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getRecordByPid(long pid, Date startTime, Date endTime) {
        return placeMappingDao.findUidsByPidAndTimeRange(pid, startTime, endTime);
    }


    public void oppositePlaceCode(OppositePlaceCodeRequest request) {
        placeInfoDao.updatePlaceStatusByPid(request.getStatus(), request.getPid());
    }


    public void scanPlaceCode(ScanPlaceCodeInput input) {
        PlaceMapping placeMapping = new PlaceMapping();
        BeanUtils.copyProperties(input, placeMapping);
        placeMapping.setTime(new Date());
        placeMappingDao.insertPlaceMapping(placeMapping);
    }

    public List<Long> getPlacesByUserList(GetPlacesByUserListRequest request) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime;
        Date endTime;
        try {
            startTime = timeFormat.parse(request.getStartTime());
            endTime = timeFormat.parse(request.getEndTime());
        } catch (ParseException e) {
            logger.error("Date parsing error: start_time={}, end_time={}, message={}", request.getStartTime(), request.getEndTime(), e.getMessage());
            throw new BusinessException(ExceptionEnum.DATETIME_FORMAT_ERROR);
        }
        return placeMappingDao.findPidsByUidListAndTimeRange(request.getUidList(), startTime, endTime);
    }


    @Override
    public void createPlaceCode(CreatePlaceCodeRequest request) {
        Result<?> result = userClient.getUserByID(request.getIdentityCard());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        PlaceInfo placeInfo = new PlaceInfo();
        long snowflakePid = IdUtil.getSnowflake().nextId();
        BeanUtils.copyProperties(request, placeInfo);
        placeInfo.setPid(snowflakePid);
        placeInfo.setUid(userInfoDto.getUid());
        placeInfo.setDistrict(request.getDistrictId());
        placeInfo.setStreet(request.getStreetId());
        placeInfo.setAddress(request.getAddress());
        placeInfo.setCommunity(request.getCommunityId());
        placeInfo.setStatus(true); // 默认开启
        placeInfoDao.insertPlace(placeInfo);
    }

    @Override
    public List<PlaceCodeInfoDto> getPlaceInfoList() {
        List<PlaceInfo> placeInfoList = placeInfoDao.findAllPlaces();
        return placeInfoList.stream()
                .map(placeInfo -> {
                    PlaceCodeInfoDto placeCodeInfoDto = new PlaceCodeInfoDto();
                    BeanUtils.copyProperties(placeInfo, placeCodeInfoDto);
                    placeCodeInfoDto.setCommunityId(placeInfo.getCommunity());
                    placeCodeInfoDto.setDistrictId(placeInfo.getDistrict());
                    placeCodeInfoDto.setStreetId(placeInfo.getStreet());
                    return placeCodeInfoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void placeCodeOpposite(Long pid) {
        PlaceInfo placeInfo = placeInfoDao.getPlaceInfoByPID(pid);
        if(placeInfo ==null){
            logger.error("Place code not found for PID: {}", pid);
            throw new BusinessException(ExceptionEnum.PLACE_CODE_NOT_FIND);
        }
        Boolean status = placeInfo.getStatus();
        placeInfoDao.updatePlaceStatusByPid(!status, pid);
    }

    @Override
    public List<Long> getAllPids() {
        return placeInfoDao.getAllPids();
    }


}
