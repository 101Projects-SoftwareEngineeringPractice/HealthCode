package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.model.entity.NucleicAcidTest;

import java.util.Date;
import java.util.List;

@Mapper
public interface NucleicAcidTestDao {

    void insertTestRecord(NucleicAcidTest testRecordDao);

    void updateTestRecord(@Param("tubeid") Long tubeid,
                          @Param("kind") Integer kind,
                          @Param("result") Integer result,
                          @Param("testingOrganization") String testingOrganization);

    void updateRetestStatus(@Param("tubeid") Long tubeid,
                            @Param("reTest") Boolean reTest);

    void updateTestRecordReTestToTrueByUidAndTime(@Param("uid") Long uid,
                                                  @Param("time") String time);

    NucleicAcidTest findLastTestRecordByUid(Long uid);

    List<NucleicAcidTest> findTestRecordsByUidWithinDays(@Param("uid") Long uid,
                                                         @Param("time") Date time);

    long countRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    long countUncheckRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    long countOnePositiveRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    long countPositiveRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<NucleicAcidTest> findPositiveRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<NucleicAcidTest> findUnreTestedRecordsWithinDays(Date time);

    List<Long> findPositiveSingleTubeUids(Date time);

    List<Long> findUidsByTubeid(Long tubeid);

    NucleicAcidTest findTestRecordsByUidAndTubeid(@Param("uid") Long uid,
                                                  @Param("tubeid") Long tubeid);
}
