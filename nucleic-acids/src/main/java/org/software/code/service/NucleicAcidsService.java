package org.software.code.service;

import org.software.code.dto.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface NucleicAcidsService {

    void addNucleicAcidTestRecord(NucleicAcidTestRecordDto testRecord);

    void enterNucleicAcidTestRecordList(List<NucleicAcidTestRecordInput> testRecords);

    public NucleicAcidTestResultDto getLastNucleicAcidTestRecordByUID(long uid);

    public List<NucleicAcidTestResultDto> getNucleicAcidTestRecordByUID(long uid);

    public NucleicAcidTestInfoDto getNucleicAcidTestInfoByTime(Date startTime, Date endTime);

    public List<PositiveInfoDto> getPositiveInfoByTime(Date startTime, Date endTime);

    public List<NucleicAcidTestRecordDto> getNoticeReTestRecords();

    public int autoModify();

    long extractUidValidateToken(String token);

    void addNucleicAcidTestRecordByToken(long tid, String qrToken, int kind, Long tubeid, String testAddress);

    void addNucleicAcidTestRecordByID(long tid, String identityCard, int kind, Long tubeid, String testAddress);
}
