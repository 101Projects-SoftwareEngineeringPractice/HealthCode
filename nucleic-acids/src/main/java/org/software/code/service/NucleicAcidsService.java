package org.software.code.service;

import org.software.code.dto.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface NucleicAcidsService {

    void addNucleicAcidTestRecord(NucleicAcidTestRecordDto testRecord);

    void enterNucleicAcidTestRecordList(List<NucleicAcidTestRecordInput> testRecords);

    NucleicAcidTestResultDto getLastNucleicAcidTestRecordByUID(long uid);

    List<NucleicAcidTestResultDto> getNucleicAcidTestRecordByUID(long uid);

    NucleicAcidTestInfoDto getNucleicAcidTestInfoByTime(Date startTime, Date endTime);

    List<PositiveInfoDto> getPositiveInfoByTime(Date startTime, Date endTime);

    void getNoticeReTestRecords();

    void autoModify();

    void addNucleicAcidTestRecordByToken(long tid, long uid, int kind, Long tubeid, String testAddress);

    void addNucleicAcidTestRecordByID(long tid, String identityCard, int kind, Long tubeid, String testAddress);

}
