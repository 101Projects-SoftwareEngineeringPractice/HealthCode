package org.software.code.service;

import org.software.code.model.dto.*;
import org.software.code.model.input.AddNucleicAcidTestRecordByIDInput;
import org.software.code.model.input.AddNucleicAcidTestRecordByTokenInput;
import org.software.code.model.input.AddNucleicAcidTestRecordRequest;
import org.software.code.model.input.enterNucleicAcidTestRecordRequestItem;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface NucleicAcidsService {

    void addNucleicAcidTestRecord(AddNucleicAcidTestRecordRequest testRecord);

    void enterNucleicAcidTestRecordList(List<enterNucleicAcidTestRecordRequestItem> testRecords);

    NucleicAcidTestResultDto getLastNucleicAcidTestRecordByUID(long uid);

    List<NucleicAcidTestResultDto> getNucleicAcidTestRecordByUID(long uid);

    NucleicAcidTestInfoDto getNucleicAcidTestInfoByTime(Date startTime, Date endTime);

    List<PositiveInfoDto> getPositiveInfoByTime(Date startTime, Date endTime);

    void getNoticeReTestRecords();

    void autoModify();

    void addNucleicAcidTestRecordByToken(AddNucleicAcidTestRecordByTokenInput input);

    void addNucleicAcidTestRecordByID(AddNucleicAcidTestRecordByIDInput input);

}
