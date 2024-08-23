package org.software.code.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.client.UserClient;
import org.software.code.common.result.Result;
import org.software.code.model.entity.ItineraryCode;
import org.software.code.model.dto.GetItineraryDto;
import org.software.code.model.dto.PlaceStarDto;
import org.software.code.model.dto.PlaceStatusDto;
import org.software.code.model.dto.UserInfoDto;
import org.software.code.dao.ItineraryCodeDao;
import org.software.code.service.ItineraryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ItineraryCodeServiceImpl implements ItineraryCodeService {
    @Autowired
    UserClient userClient;
    @Resource
    ItineraryCodeDao itineraryCodeDao;

    @Override
    public List<PlaceStatusDto> getItineraryCodeList(long uid) {
        List<ItineraryCode> itineraryCodeList = itineraryCodeDao.getItineraryCodeListByUID(uid);
        return itineraryCodeList.stream()
                .map(itineraryCode -> {
                    PlaceStatusDto placeStatusDto = new PlaceStatusDto();
                    List<String> stringList = Arrays.asList("北京市", "上海市", "广州市", "深圳市", "杭州市"); //随机枚举城市
                    int randomIndex = ThreadLocalRandom.current().nextInt(stringList.size());
                    String place = stringList.get(randomIndex);
                    boolean status = ThreadLocalRandom.current().nextBoolean(); //随机状态
                    placeStatusDto.setPlace(place);
                    placeStatusDto.setStatus(status);
                    return placeStatusDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void cleanItinerary() {
        Date currentDate = new Date(); // 获取当前日期
        Calendar calendar = Calendar.getInstance(); // 创建 Calendar 实例并设置为当前日期
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -15); // 减去15天
        Date dateBefore15Days = calendar.getTime(); // 获取15天前的日期
        itineraryCodeDao.deleteItineraryCodeBeforeTime(dateBefore15Days);
    }


    @Override
    public GetItineraryDto getItinerary(long uid) {
        List<ItineraryCode> itineraryCodeList = itineraryCodeDao.getItineraryCodeListByUID(uid);
        Result<?> result = userClient.getUserByUID(uid);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        List<PlaceStarDto> places = itineraryCodeList.stream()
                .map(itineraryCode -> {
                    PlaceStarDto placeStarDto = new PlaceStarDto();
                    List<String> stringList = Arrays.asList("北京市", "上海市", "广州市", "深圳市", "杭州市"); //随机枚举城市
                    int randomIndex = ThreadLocalRandom.current().nextInt(stringList.size());
                    String place = stringList.get(randomIndex);
                    boolean status = ThreadLocalRandom.current().nextBoolean(); //随机状态
                    placeStarDto.setPlace(place);
                    placeStarDto.setStar(status);
                    return placeStarDto;
                })
                .collect(Collectors.toList());
        GetItineraryDto getItineraryDto = new GetItineraryDto();
        getItineraryDto.setPlaces(places);
        getItineraryDto.setCreatedAt(new Date());
        getItineraryDto.setIdentityCard(userInfoDto.getIdentityCard());
        return getItineraryDto;
    }
}
