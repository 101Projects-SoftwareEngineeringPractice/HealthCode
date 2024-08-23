package org.software.code.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.software.code.client.HealthCodeClient;
import org.software.code.client.PlaceCodeClient;
import org.software.code.client.UserClient;
import org.software.code.common.result.Result;
import org.software.code.model.entity.NucleicAcidTest;
import org.software.code.kafaka.NotificationProducer;
import org.software.code.dao.NucleicAcidTestDao;
import org.software.code.model.dto.*;
import org.software.code.model.input.*;
import org.software.code.model.message.NotificationMessage;
import org.software.code.service.NucleicAcidsService;
import org.software.code.service.notification.CommunityNotificationHandler;
import org.software.code.service.notification.EpidemicPreventionNotificationHandler;
import org.software.code.service.notification.NotificationChain;
import org.software.code.service.notification.SmsNotificationHandler;
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
    private NucleicAcidTestDao nucleicAcidTestDao;

    @Autowired
    HealthCodeClient healthCodeClient;

    @Autowired
    PlaceCodeClient placeCodeClient;

    @Autowired
    UserClient userClient;

    @Autowired
    private NotificationProducer notificationProducer;

    @Autowired
    private RiskCalculationContext riskCalculationContext;


    public void addNucleicAcidTestRecord(AddNucleicAcidTestRecordRequest testRecordDto) {
        NucleicAcidTest testRecordDao = new NucleicAcidTest();
        BeanUtils.copyProperties(testRecordDto, testRecordDao);
        //如果为单管类型，还需将该uid三天内的其他核酸检测记录标记为已复检状态，否则置为空
        if (testRecordDao.getKind() == 0) {
            Date oneDayAgo = new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String threeDayAgoFormatted = dateFormat.format(oneDayAgo);
            nucleicAcidTestDao.updateTestRecordReTestToTrueByUidAndTime(testRecordDao.getUid(), threeDayAgoFormatted);
        }
        nucleicAcidTestDao.insertTestRecord(testRecordDao); // 调用 Mapper 层进行数据库操作
    }

    public void enterNucleicAcidTestRecordList(List<enterNucleicAcidTestRecordRequestItem> testRecords) {
        for (enterNucleicAcidTestRecordRequestItem input : testRecords) {
            // 更新检测结果
            nucleicAcidTestDao.updateTestRecord(input.getTubeid(), input.getKind(), input.getResult(), input.getTestingOrganization());
            // 混管且阳性，转黄码
            if (input.getKind() != 0 && input.getResult() == 1) {
                List<Long> uids = nucleicAcidTestDao.findUidsByTubeid(input.getTubeid());
                for (Long uid : uids) {
                    TranscodingEventsRequest transcodingEventsRequest = new TranscodingEventsRequest(uid, 1);
                    healthCodeClient.transcodingHealthCodeEvents(transcodingEventsRequest);
                }
                nucleicAcidTestDao.updateRetestStatus(input.getTubeid(), false);
            }
            // 单管且阳性，发送消息队列派人处理，转红码
            else if (input.getKind() == 0 && input.getResult() == 1) {
                List<Long> uids = nucleicAcidTestDao.findUidsByTubeid(input.getTubeid());
                for (Long uid : uids) {
                    NucleicAcidTest nucleicAcidTest = nucleicAcidTestDao.findTestRecordsByUidAndTubeid(uid, input.getTubeid());
                    NotificationMessage message = new NotificationMessage();
                    message.setName(nucleicAcidTest.getName());
                    message.setIdentityCard(nucleicAcidTest.getIdentityCard());
                    message.setPhone(nucleicAcidTest.getPhoneNumber());
                    message.setType("POSITIVE");
                    notificationProducer.sendNotification("notification-topic", message);
                    TranscodingEventsRequest transcodingEventsRequest = new TranscodingEventsRequest(uid, 2);
                    healthCodeClient.transcodingHealthCodeEvents(transcodingEventsRequest);
                }
            }
            // 阴性 转绿码
            else if (input.getResult() == 0) {
                List<Long> uids = nucleicAcidTestDao.findUidsByTubeid(input.getTubeid());
                for (Long uid : uids) {
                    TranscodingEventsRequest transcodingEventsRequest = new TranscodingEventsRequest(uid, 0);
                    healthCodeClient.transcodingHealthCodeEvents(transcodingEventsRequest);
                }
            }
        }
    }

    public NucleicAcidTestResultDto getLastNucleicAcidTestRecordByUID(long uid) {
        NucleicAcidTest recordDao = nucleicAcidTestDao.findLastTestRecordByUid(uid);
        if (recordDao == null) {
            return null;
        }
        NucleicAcidTestResultDto resultDto = new NucleicAcidTestResultDto();
        BeanUtils.copyProperties(recordDao, resultDto);
        return resultDto;
    }

    public List<NucleicAcidTestResultDto> getNucleicAcidTestRecordByUID(long uid) {
        Date fourteenDaysAgo = new Date(System.currentTimeMillis() - 14L * 24 * 60 * 60 * 1000);
        List<NucleicAcidTest> recordsDao = nucleicAcidTestDao.findTestRecordsByUidWithinDays(uid, fourteenDaysAgo);
        return recordsDao.stream()
                .map(recordDao -> {
                    NucleicAcidTestResultDto resultDto = new NucleicAcidTestResultDto();
                    BeanUtils.copyProperties(recordDao, resultDto);
                    return resultDto;
                })
                .collect(Collectors.toList());
    }

    public NucleicAcidTestInfoDto getNucleicAcidTestInfoByTime(Date startTime, Date endTime) {
        long record = nucleicAcidTestDao.countRecordsWithinTimeRange(startTime, endTime);
        long uncheck = nucleicAcidTestDao.countUncheckRecordsWithinTimeRange(startTime, endTime);
        long onePositive = nucleicAcidTestDao.countOnePositiveRecordsWithinTimeRange(startTime, endTime);
        long positive = nucleicAcidTestDao.countPositiveRecordsWithinTimeRange(startTime, endTime);
        NucleicAcidTestInfoDto infoDto = new NucleicAcidTestInfoDto();
        infoDto.setRecord(record);
        infoDto.setUncheck(uncheck);
        infoDto.setOnePositive(onePositive);
        infoDto.setPositive(positive);
        return infoDto;
    }

    public List<PositiveInfoDto> getPositiveInfoByTime(Date startTime, Date endTime) {
        List<NucleicAcidTest> recordsDao = nucleicAcidTestDao.findPositiveRecordsWithinTimeRange(startTime, endTime);
        return recordsDao.stream()
                .map(recordDao -> {
                    PositiveInfoDto infoDto = new PositiveInfoDto();
                    BeanUtils.copyProperties(recordDao, infoDto);
                    return infoDto;
                })
                .collect(Collectors.toList());
    }

    public void getNoticeReTestRecords() {
        Date threeDaysAgo = new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000);
        List<NucleicAcidTest> recordsDao = nucleicAcidTestDao.findUnreTestedRecordsWithinDays(threeDaysAgo);
        NotificationChain notificationChain = new NotificationChain()
                .addHandler(new SmsNotificationHandler(notificationProducer))
                .addHandler(new CommunityNotificationHandler(notificationProducer))
                .addHandler(new EpidemicPreventionNotificationHandler(notificationProducer));
        for (NucleicAcidTest record : recordsDao) {
            NotificationMessage message = new NotificationMessage();
            message.setName(record.getName());
            message.setIdentityCard(record.getIdentityCard());
            message.setPhone(record.getPhoneNumber());
            notificationChain.execute(message);
        }
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
            List<Long> positiveUids = nucleicAcidTestDao.findPositiveSingleTubeUids(oneDayAgo);
            Set<Long> positivePersons = uids.stream()
                    .filter(positiveUids::contains)
                    .collect(Collectors.toSet());
            int positiveCount = positivePersons.size();
            String risk = riskCalculationContext.calculateRiskLevel(totalPersons, positiveCount);
            placeCodeClient.setPlaceRisk(pid, risk);
        }
    }


    @Override
    public void addNucleicAcidTestRecordByToken(AddNucleicAcidTestRecordByTokenInput input) {
        Result<?> result = userClient.getUserByUID(input.getUid());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        NucleicAcidTest testRecordDao = new NucleicAcidTest();
        testRecordDao.setUid(input.getUid());
        testRecordDao.setTid(input.getTid());
        testRecordDao.setKind(input.getKind());
        testRecordDao.setTubeid(input.getTubeId());
        testRecordDao.setIdentityCard(userInfoDto.getIdentityCard());
        testRecordDao.setPhoneNumber(userInfoDto.getPhoneNumber());
        testRecordDao.setName(userInfoDto.getName());
        testRecordDao.setDistrict(userInfoDto.getDistrict());
        testRecordDao.setStreet(userInfoDto.getStreet());
        testRecordDao.setCommunity(userInfoDto.getCommunity());
        testRecordDao.setAddress(userInfoDto.getAddress());
        testRecordDao.setTestAddress(input.getTestAddress());
        //如果为单管类型，还需将该uid三天内的其他核酸检测记录标记为已复检状态，否则置为空
        if (input.getKind() == 0) {
            Date oneDayAgo = new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String threeDayAgoFormatted = dateFormat.format(oneDayAgo);
            nucleicAcidTestDao.updateTestRecordReTestToTrueByUidAndTime(input.getUid(), threeDayAgoFormatted);
        }
        nucleicAcidTestDao.insertTestRecord(testRecordDao);// 调用 Mapper 层进行数据库操作
    }

    @Override
    public void addNucleicAcidTestRecordByID(AddNucleicAcidTestRecordByIDInput input) {
        Result<?> result = userClient.getUserByID(input.getIdentityCard());
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoDto userInfoDto = objectMapper.convertValue(result.getData(), UserInfoDto.class);
        NucleicAcidTest testRecordDao = new NucleicAcidTest();
        testRecordDao.setUid(userInfoDto.getUid());
        testRecordDao.setTid(input.getTid());
        testRecordDao.setKind(input.getKind());
        testRecordDao.setTubeid(input.getTubeId());
        testRecordDao.setIdentityCard(input.getIdentityCard());
        testRecordDao.setPhoneNumber(userInfoDto.getPhoneNumber());
        testRecordDao.setName(userInfoDto.getName());
        testRecordDao.setDistrict(userInfoDto.getDistrict());
        testRecordDao.setStreet(userInfoDto.getStreet());
        testRecordDao.setCommunity(userInfoDto.getCommunity());
        testRecordDao.setAddress(userInfoDto.getAddress());
        testRecordDao.setTestAddress(input.getTestAddress());
        //如果为单管类型，还需将该uid三天内的其他核酸检测记录标记为已复检状态，否则置为空
        if (input.getKind() == 0) {
            Date oneDayAgo = new Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String threeDayAgoFormatted = dateFormat.format(oneDayAgo);
            nucleicAcidTestDao.updateTestRecordReTestToTrueByUidAndTime(userInfoDto.getUid(), threeDayAgoFormatted);
        }
        nucleicAcidTestDao.insertTestRecord(testRecordDao);// 调用 Mapper 层进行数据库操作
    }
}
