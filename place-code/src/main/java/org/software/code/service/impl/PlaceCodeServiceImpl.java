package org.software.code.service.impl;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.software.code.client.UserClient;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.dao.PlaceInfoDao;
import org.software.code.dao.PlaceMappingDao;
import org.software.code.mapper.PlaceInfoMapper;
import org.software.code.mapper.PlaceMappingMapper;
import org.software.code.model.dto.GetPlaceDto;
import org.software.code.model.dto.PlaceCodeInfoDto;
import org.software.code.model.dto.UserInfoDto;
import org.software.code.model.input.*;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceCodeServiceImpl implements PlaceCodeService {
    private static final Logger logger = LogManager.getLogger(PlaceCodeServiceImpl.class);


    @Autowired
    private PlaceInfoMapper placeInfoMapper;

    @Autowired
    private PlaceMappingMapper placeMappingMapper;

    @Autowired
    UserClient userClient;

    public Long addPlace(AddPlaceRequest request) {
        Result<?> result = userClient.getUserByUID(request.getUid());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        PlaceInfoDao placeInfoDao = new PlaceInfoDao();
        BeanUtils.copyProperties(request, placeInfoDao);
        placeInfoDao.setIdentity_card(userInfo.getIdentity_card());
        placeInfoDao.setDistrict(request.getDistrict());
        placeInfoDao.setStreet(request.getStreet());
        placeInfoDao.setCommunity(placeInfoDao.getCommunity());
        placeInfoDao.setAddress(placeInfoDao.getAddress());
        placeInfoDao.setStatus(true); // 默认开启
        long snowflakePid = IdUtil.getSnowflake().nextId();
        placeInfoDao.setPid(snowflakePid);
        placeInfoMapper.insertPlace(placeInfoDao);
        return placeInfoDao.getPid();
    }

    public List<GetPlaceDto> getPlaces() {
        List<PlaceInfoDao> placeInfoDaoList = placeInfoMapper.findAllPlaces();
        return placeInfoDaoList.stream()
                .map(placeInfoDao -> {
                    GetPlaceDto getPlaceDto = new GetPlaceDto();
                    BeanUtils.copyProperties(placeInfoDao, getPlaceDto);
                    Result<?> result = userClient.getUserByUID(placeInfoDao.getUid());
                    ObjectMapper objectMapper = new ObjectMapper();
                    UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
                    getPlaceDto.setPhone_number(userInfo.getPhone_number());
                    return getPlaceDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getRecordByPid(long pid, Date startTime, Date endTime) {
        return placeMappingMapper.findUidsByPidAndTimeRange(pid, startTime, endTime);
    }


    public void oppositePlaceCode(OppositePlaceCodeRequest request) {
        placeInfoMapper.updatePlaceStatusByPid(request.getStatus(), request.getPid());
    }


    public void scanPlaceCode(ScanPlaceCodeInput input) {
        PlaceMappingDao placeMappingDao = new PlaceMappingDao();
        BeanUtils.copyProperties(input,placeMappingDao);
        placeMappingDao.setTime(new Date());
        placeMappingMapper.insertPlaceMapping(placeMappingDao);
    }

    public List<Long> getPlacesByUserList(GetPlacesByUserListRequest request) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime;
        Date endTime;
        try {
            startTime = timeFormat.parse(request.getStart_time());
            endTime = timeFormat.parse(request.getEnd_time());
        } catch (ParseException e) {
            logger.error("Date parsing error: start_time={}, end_time={}, message={}", request.getStart_time(), request.getEnd_time(), e.getMessage());
            throw new BusinessException(ExceptionEnum.DATETIME_FORMAT_ERROR);
        }
        return placeMappingMapper.findPidsByUidListAndTimeRange(request.getUidList(), startTime, endTime);
    }


    @Override
    public void createPlaceCode(CreatePlaceCodeRequest request) {
        Result<?> result = userClient.getUserByID(request.getIdentity_card());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        PlaceInfoDao placeInfoDao = new PlaceInfoDao();
        long snowflakePid = IdUtil.getSnowflake().nextId();
        BeanUtils.copyProperties(request,placeInfoDao);
        placeInfoDao.setPid(snowflakePid);
        placeInfoDao.setUid(userInfoDto.getUid());
        placeInfoDao.setStatus(true); // 默认开启
        placeInfoMapper.insertPlace(placeInfoDao);
    }

    @Override
    public List<PlaceCodeInfoDto> getPlaceInfoList() {
        List<PlaceInfoDao> placeInfoDaoList = placeInfoMapper.findAllPlaces();
        return placeInfoDaoList.stream()
                .map(placeInfoDao -> {
                    PlaceCodeInfoDto placeCodeInfoDto = new PlaceCodeInfoDto();
                    BeanUtils.copyProperties(placeInfoDao, placeCodeInfoDto);
                    placeCodeInfoDto.setCommunity_id(placeInfoDao.getCommunity());
                    placeCodeInfoDto.setDistrict_id(placeInfoDao.getDistrict());
                    placeCodeInfoDto.setStreet_id(placeInfoDao.getStreet());
                    return placeCodeInfoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void placeCodeOpposite(Long pid) {
        PlaceInfoDao placeInfoDao = placeInfoMapper.getPlaceInfoByPID(pid);
        if(placeInfoDao==null){
            logger.error("Place code not found for PID: {}", pid);
            throw new BusinessException(ExceptionEnum.PLACE_CODE_NOT_FIND);
        }
        Boolean status = placeInfoDao.getStatus();
        placeInfoMapper.updatePlaceStatusByPid(!status, pid);
    }

    @Override
    public List<Long> getAllPids() {
        return placeInfoMapper.getAllPids();
    }


}
