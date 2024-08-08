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
import org.software.code.service.strategy.RiskCalculationContext;
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

    @Autowired
    private RiskCalculationContext riskCalculationContext;


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

    public void autoModify() {
        ObjectMapper objectMapper = new ObjectMapper();
        Date oneDayAgo = new Date(System.currentTimeMillis() - 1L * 24 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String oneDayAgoFormatted = dateFormat.format(oneDayAgo);
        Date now = new Date();
        String nowFormatted = dateFormat.format(now);
        //获取所有场所码
        List<Long> pids = objectMapper.convertValue(placeCodeClient.getAllPids().getData(), List.class);
        //对于每个场所
        for (Long pid : pids) {
            // 获取前一天到过该场所的人员
            List<Long> uids = objectMapper.convertValue(placeCodeClient.getRecordByPid(pid, oneDayAgoFormatted, nowFormatted).getData(), List.class);
            uids = uids.stream().distinct().collect(Collectors.toList()); //去重
            int totalPersons = uids.size();  // 获取总人数
            // 获取阳性人数
            List<Long> positiveUids = nucleicAcidTestMapper.findPositiveSingleTubeUids(oneDayAgo);
            Set<Long> positivePersons = uids.stream()
                    .filter(positiveUids::contains)
                    .collect(Collectors.toSet());
            int positiveCount = positivePersons.size();
            String risk = riskCalculationContext.calculateRiskLevel(totalPersons, positiveCount);
            placeCodeClient.setPlaceRisk(pid, risk);
        }
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
