package org.software.code.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.client.HealthCodeClient;
import org.software.code.client.PlaceCodeClient;
import org.software.code.client.UserClient;
import org.software.code.common.result.Result;
import org.software.code.dao.NucleicAcidTestRecordDao;
import org.software.code.dto.*;
import org.software.code.mapper.NucleicAcidTestMapper;
import org.software.code.service.NucleicAcidsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NucleicAcidsServiceImpl implements NucleicAcidsService {

    @Autowired
    private NucleicAcidTestMapper nucleicAcidTestMapper;

    @Autowired
    HealthCodeClient healthCodeClient;

    @Autowired
    PlaceCodeClient placeCodeClient;

    @Autowired
    UserClient userClient;

    public void addNucleicAcidTestRecord(NucleicAcidTestRecordDto testRecordDto) {
        NucleicAcidTestRecordDao testRecordDao = new NucleicAcidTestRecordDao();
        BeanUtils.copyProperties(testRecordDto, testRecordDao);
        nucleicAcidTestMapper.insertTestRecord(testRecordDao); // 调用 Mapper 层进行数据库操作
    }

    public void enterNucleicAcidTestRecordList(List<NucleicAcidTestRecordInput> testRecords) {
        for (NucleicAcidTestRecordInput input : testRecords) {
            // 更新检测结果
            nucleicAcidTestMapper.updateTestRecord(input.getTubeid(), input.getKind(), input.getResult(), input.getTesting_organization());
            if (input.getKind() != 0 && input.getResult() == 1) { // 混管且阳性
                nucleicAcidTestMapper.updateRetestStatus(input.getTubeid(), false);
            } else if (input.getKind() == 0 && input.getResult() == 1) { // 单管且阳性
                // 发送消息队列派人处理
                // sendMessageToQueue(input.getTubeid());
            } else if (input.getResult() == 0) { // 阴性
                //nucleicAcidTestMapper.updateHealthCodeStatus(input.getTubeid(), "green");
                System.out.println("阴性无需处理");
            }
        }
    }

    public NucleicAcidTestResultDto getLastNucleicAcidTestRecordByUID(long uid) {
        NucleicAcidTestRecordDao recordDao = nucleicAcidTestMapper.findLastTestRecordByUid(uid);
        if (recordDao == null) {
            return null;
        }
        NucleicAcidTestResultDto resultDto = new NucleicAcidTestResultDto();
        BeanUtils.copyProperties(recordDao, resultDto);
        return resultDto;
    }

    public List<NucleicAcidTestResultDto> getNucleicAcidTestRecordByUID(long uid) {
        Date fourteenDaysAgo = new Date(System.currentTimeMillis() - 14L * 24 * 60 * 60 * 1000);
        List<NucleicAcidTestRecordDao> recordsDao = nucleicAcidTestMapper.findTestRecordsByUidWithinDays(uid, fourteenDaysAgo);
        return recordsDao.stream()
                .map(recordDao -> {
                    NucleicAcidTestResultDto resultDto = new NucleicAcidTestResultDto();
                    BeanUtils.copyProperties(recordDao, resultDto);
                    return resultDto;
                })
                .collect(Collectors.toList());
    }

    public NucleicAcidTestInfoDto getNucleicAcidTestInfoByTime(Date startTime, Date endTime) {
        long record = nucleicAcidTestMapper.countRecordsWithinTimeRange(startTime, endTime);
        long uncheck = nucleicAcidTestMapper.countUncheckRecordsWithinTimeRange(startTime, endTime);
        long onePositive = nucleicAcidTestMapper.countOnePositiveRecordsWithinTimeRange(startTime, endTime);
        long positive = nucleicAcidTestMapper.countPositiveRecordsWithinTimeRange(startTime, endTime);
        NucleicAcidTestInfoDto infoDto = new NucleicAcidTestInfoDto();
        infoDto.setRecord(record);
        infoDto.setUncheck(uncheck);
        infoDto.setOnePositive(onePositive);
        infoDto.setPositive(positive);
        return infoDto;
    }

    public List<PositiveInfoDto> getPositiveInfoByTime(Date startTime, Date endTime) {
        List<NucleicAcidTestRecordDao> recordsDao = nucleicAcidTestMapper.findPositiveRecordsWithinTimeRange(startTime, endTime);
        return recordsDao.stream()
                .map(recordDao -> {
                    PositiveInfoDto infoDto = new PositiveInfoDto();
                    BeanUtils.copyProperties(recordDao, infoDto);
                    return infoDto;
                })
                .collect(Collectors.toList());
    }

    public List<NucleicAcidTestRecordDto> getNoticeReTestRecords() {
        Date threeDaysAgo = new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000);
        List<NucleicAcidTestRecordDao> recordsDao = nucleicAcidTestMapper.findUnreTestedRecordsWithinDays(threeDaysAgo);
        return recordsDao.stream()
                .map(recordDao -> {
                    NucleicAcidTestRecordDto recordDto = new NucleicAcidTestRecordDto();
                    BeanUtils.copyProperties(recordDao, recordDto);
                    return recordDto;
                })
                .collect(Collectors.toList());
    }

    public int autoModify() {
        // 获取前天的日期
        Date twoDaysAgo = new Date(System.currentTimeMillis() - 2L * 24 * 60 * 60 * 1000);
        // 获取前天一天内单管阳性用户uid集合
        List<Long> uids = nucleicAcidTestMapper.findPositiveSingleTubeUids(twoDaysAgo);
        // 格式化日期为"yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String twoDaysAgoFormatted = dateFormat.format(twoDaysAgo);
        // 获取当天的开始时间和结束时间
        Date now = new Date();
        String nowFormatted = dateFormat.format(now);
        // 调用placeCodeClient的getPlacesByUserList方法
        GetPlacesByUserListRequest getPlacesByUserListRequest = new GetPlacesByUserListRequest(uids, twoDaysAgoFormatted, nowFormatted);
        Result<?> result = placeCodeClient.getPlacesByUserList(getPlacesByUserListRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> pids = objectMapper.convertValue(result.getData(), List.class);
        // 调用场所微服务获取对应的场所pid
        // 调用场所微服务获取这些pid中前天进入的uid
        Set<Long> affectedUids = pids.stream()
                .flatMap(pid -> {
                    List<Long> users = objectMapper.convertValue(placeCodeClient.getRecordByPid(pid, twoDaysAgoFormatted, nowFormatted), List.class);
                    return users.stream();
                })
                .collect(Collectors.toSet());
        // 调用健康码微服务获取健康码颜色并设定为黄码
        int count = 0;
        for (Long uid : affectedUids) {
            Integer healthCode = objectMapper.convertValue(healthCodeClient.getHealthCode(uid), Integer.class);
            if (healthCode != null && healthCode != 2) { // 非红码用户
                TranscodingEventsRequest transcodingEventsRequest=new TranscodingEventsRequest(uid, 1);
                healthCodeClient.transcodingHealthCodeEvents(transcodingEventsRequest);
                count++;
            }
        }
        return count;
    }


    @Override
    public void addNucleicAcidTestRecordByToken(long tid, long uid, int kind, Long tubeid, String testAddress) {
        Result<?> result = userClient.getUserByUID(uid);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        NucleicAcidTestRecordDao testRecordDao = new NucleicAcidTestRecordDao();
        testRecordDao.setUid(uid);
        testRecordDao.setTid(tid);
        testRecordDao.setKind(kind);
        testRecordDao.setTubeid(tubeid);
        testRecordDao.setIdentity_card(userInfoDto.getIdentity_card());
        testRecordDao.setPhone_number(userInfoDto.getPhone_number());
        testRecordDao.setName(userInfoDto.getName());
        testRecordDao.setDistrict(userInfoDto.getDistrict());
        testRecordDao.setStreet(userInfoDto.getStreet());
        testRecordDao.setCommunity(userInfoDto.getCommunity());
        testRecordDao.setAddress(userInfoDto.getAddress());
        testRecordDao.setTest_address(testAddress);
        nucleicAcidTestMapper.insertTestRecord(testRecordDao);// 调用 Mapper 层进行数据库操作
    }

    @Override
    public void addNucleicAcidTestRecordByID(long tid, String identityCard, int kind, Long tubeid, String testAddress) {
        Result<?> result = userClient.getUserByID(identityCard);
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        NucleicAcidTestRecordDao testRecordDao = new NucleicAcidTestRecordDao();
        testRecordDao.setUid(userInfoDto.getUid());
        testRecordDao.setTid(tid);
        testRecordDao.setKind(kind);
        testRecordDao.setTubeid(tubeid);
        testRecordDao.setIdentity_card(identityCard);
        testRecordDao.setPhone_number(userInfoDto.getPhone_number());
        testRecordDao.setName(userInfoDto.getName());
        testRecordDao.setDistrict(userInfoDto.getDistrict());
        testRecordDao.setStreet(userInfoDto.getStreet());
        testRecordDao.setCommunity(userInfoDto.getCommunity());
        testRecordDao.setAddress(userInfoDto.getAddress());
        testRecordDao.setTest_address(testAddress);
        nucleicAcidTestMapper.insertTestRecord(testRecordDao);// 调用 Mapper 层进行数据库操作
    }
}
