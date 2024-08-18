package org.software.code.service.impl;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.client.UserClient;
import org.software.code.common.JWTUtil;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.dao.PlaceInfoDao;
import org.software.code.dao.PlaceMappingDao;
import org.software.code.dto.*;
import org.software.code.mapper.PlaceInfoMapper;
import org.software.code.mapper.PlaceMappingMapper;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlaceCodeServiceImpl implements PlaceCodeService {
    @Autowired
    private PlaceInfoMapper placeInfoMapper;

    @Autowired
    private PlaceMappingMapper placeMappingMapper;

    @Autowired
    UserClient userClient;

    public Long addPlace(AddPlaceInput placeDto) {
        Result<?> result = userClient.getUserByUID(placeDto.getUid());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        PlaceInfoDao placeInfoDao = new PlaceInfoDao();
        BeanUtils.copyProperties(placeDto, placeInfoDao);
        placeInfoDao.setIdentity_card(userInfo.getIdentity_card());
        placeInfoDao.setDistrict(placeDto.getDistrict()); // 默认开启
        placeInfoDao.setStreet(placeDto.getStreet()); // 默认开启
        placeInfoDao.setCommunity(placeInfoDao.getCommunity()); // 默认开启
        placeInfoDao.setAddress(placeInfoDao.getAddress()); // 默认开启
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
        return placeInfoMapper.findUidsByPidAndTimeRange(pid, startTime, endTime);
    }


    public void oppositePlaceCode(long pid, boolean status) {
        placeInfoMapper.updatePlaceStatusByPid(status, pid);
    }


    public void scanPlaceCode(long uid, String token) {
        long pid = JWTUtil.extractID(token);
        PlaceMappingDao placeMappingDao = new PlaceMappingDao();
        placeMappingDao.setPid(pid);
        placeMappingDao.setUid(uid);
        placeMappingDao.setTime(new Date());
        placeMappingMapper.insertPlaceMapping(placeMappingDao);
    }

    public List<Long> getPlacesByUserList(List<Long> uidList, Date startTime, Date endTime) {
        return placeMappingMapper.findPidsByUidListAndTimeRange(uidList, startTime, endTime);
    }


    @Override
    public void createPlaceCode(String identityCard, String name, int districtId, int streetId, long communityId, String address) {
        Result<?> result = userClient.getUserByID(identityCard);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        PlaceInfoDao placeInfoDao = new PlaceInfoDao();
        long snowflakePid = IdUtil.getSnowflake().nextId();
        placeInfoDao.setPid(snowflakePid);
        placeInfoDao.setUid(userInfoDto.getUid());
        placeInfoDao.setIdentity_card(userInfoDto.getIdentity_card());
        placeInfoDao.setName(name);
        placeInfoDao.setDistrict(districtId);
        placeInfoDao.setStreet(streetId);
        placeInfoDao.setCommunity(communityId);
        placeInfoDao.setAddress(address);
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
            throw new BusinessException(ExceptionEnum.PLACE_CODE_NOT_FIND);
        }
        Boolean status = placeInfoDao.getStatus();
        placeInfoMapper.updatePlaceStatusByPid(!status, pid);
    }


}
