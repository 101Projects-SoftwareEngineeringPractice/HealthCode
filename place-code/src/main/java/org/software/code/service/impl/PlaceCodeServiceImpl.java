package org.software.code.service.impl;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.client.UserClient;
import org.software.code.common.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.dao.PlaceInfoDao;
import org.software.code.dao.PlaceMappingDao;
import org.software.code.dto.GetPlaceDto;
import org.software.code.dto.PlaceCodeInfoDto;
import org.software.code.dto.AddPlaceInput;
import org.software.code.dto.UserInfoDto;
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
        UserInfoDto userInfo;
        try {
            Result<?> result = userClient.getUserByUID(placeDto.getUid());
            ObjectMapper objectMapper = new ObjectMapper();
            userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
        if (userInfo == null || userInfo.getUid() == 0) {
            throw new NullPointerException("用户不存在，请重试");
        }

        PlaceInfoDao placeInfoDao = new PlaceInfoDao();
        BeanUtils.copyProperties(placeDto, placeInfoDao);
        placeInfoDao.setIdentity_card(userInfo.getIdentity_card());
        placeInfoDao.setStatus(true); // 默认开启
        long snowflakePid = IdUtil.getSnowflake().nextId();
        placeInfoDao.setPid(snowflakePid);
        try {
            placeInfoMapper.insertPlace(placeInfoDao);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
        return placeInfoDao.getPid();
    }

    public List<GetPlaceDto> getPlaces() {
        List<PlaceInfoDao> placeInfoDaoList = placeInfoMapper.findAllPlaces();
        try {
            return placeInfoDaoList.stream()
                    .map(placeInfoDao -> {
                        GetPlaceDto getPlaceDto = new GetPlaceDto();
                        BeanUtils.copyProperties(placeInfoDao, getPlaceDto);
                        Result<?> result = userClient.getUserByUID(placeInfoDao.getUid());
                        ObjectMapper objectMapper = new ObjectMapper();
                        UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
                        if (userInfo == null || userInfo.getUid() == 0) {
                            throw new NullPointerException("用户不存在，请重试");
                        }
                        getPlaceDto.setPhone_number(userInfo.getPhone_number());
                        return getPlaceDto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");

        }
    }

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
        try {
            placeMappingMapper.insertPlaceMapping(placeMappingDao);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");

        }
    }

    public List<Long> getPlacesByUserList(List<Long> uidList, Date startTime, Date endTime) {
        return placeMappingMapper.findPidsByUidListAndTimeRange(uidList, startTime, endTime);
    }


    @Override
    public void createPlaceCode(String identityCard, String name, int districtId, int streetId, int communityId, String address) {

        UserInfoDto userInfoDto;
        try {
            Result<?> result = userClient.getUserByID(identityCard);
            ObjectMapper objectMapper = new ObjectMapper();
            userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
        if (userInfoDto == null) {
            throw new NullPointerException("用户不存在，请重试");
        }
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
        try {
            placeInfoMapper.insertPlace(placeInfoDao);
        } catch (Exception e) {
            throw new RuntimeException("服务执行错误，请稍后重试");
        }
    }

    @Override
    public List<PlaceCodeInfoDto> getPlaceInfoList() {
        List<PlaceInfoDao> placeInfoDaoList = placeInfoMapper.findAllPlaces();
        return placeInfoDaoList.stream()
                .map(placeInfoDao -> {
                    PlaceCodeInfoDto placeCodeInfoDto = new PlaceCodeInfoDto();
                    BeanUtils.copyProperties(placeInfoDao, placeCodeInfoDto);
                    return placeCodeInfoDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void placeCodeOpposite(Long pid) {
        PlaceInfoDao placeInfoDao = placeInfoMapper.getPlaceInfoByPID(pid);
        Boolean status = placeInfoDao.getStatus();
        placeInfoMapper.updatePlaceStatusByPid(!status, pid);
    }
}
