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
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate = new RestTemplate();

    private JWTUtil pidJwtUtil = new JWTUtil("pid_token_secret_key", 3600000);

    public Long addPlace(AddPlaceInput placeDto) {
        // 调用 user 微服务获取 identity_card
        Result<?> result = userClient.getUserByUID(placeDto.getUid());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfo = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        if (userInfo == null || userInfo.getUid() == 0) {
            throw new RuntimeException("用户信息未找到");
        }

        PlaceInfoDao placeInfoDao = new PlaceInfoDao();
        BeanUtils.copyProperties(placeDto, placeInfoDao);
        placeInfoDao.setIdentity_card(userInfo.getIdentity_card());
        placeInfoDao.setStatus(true); // 默认开启

        // 生成雪花算法 pid
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
                    if (userInfo == null || userInfo.getUid() == 0) {
                        throw new RuntimeException("用户信息未找到");
                    }
                    getPlaceDto.setPhone_number(userInfo.getPhone_number());
                    return getPlaceDto;
                })
                .collect(Collectors.toList());
    }

    public List<Long> getRecordByPid(long pid, Date startTime, Date endTime) {
        return placeInfoMapper.findUidsByPidAndTimeRange(pid, startTime, endTime);
    }


    public void oppositePlaceCode(long pid, boolean status) {
        placeInfoMapper.updatePlaceStatusByPid(status, pid);
    }


    public void scanPlaceCode(long uid, String token) {
        long pid = pidJwtUtil.extractID(token);

        // 创建并插入 PlaceMappingDao
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
    public void createPlaceCode(String identityCard, String name, int districtId, int streetId, int communityId, String address) {
        Result<?> result = userClient.getUserByID(identityCard);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        if (userInfoDto == null) {
            throw new RuntimeException("用户信息未找到");
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
        placeInfoMapper.insertPlace(placeInfoDao);
        System.out.println("identityCard = " + identityCard + ", name = " + name + ", districtId = " + districtId + ", streetId = " + streetId + ", communityId = " + communityId + ", address = " + address);
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

    @Override
    public long extractUidValidateToken(String token) {
        Result<?> result = userClient.extractUidValidateToken(token);
        return (Long) result.getData();
    }

    @Override
    public long extractMidValidateToken(String token) {
        Result<?> result = userClient.extractUidValidateToken(token);
        return (Long) result.getData();
    }

}
