package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.NucleicAcidTestRecordDao;

import java.util.Date;
import java.util.List;

@Mapper
public interface NucleicAcidTestMapper {

    @Insert("INSERT INTO health_code_nucleic_acids.nucleic_acid_test (uid, tid, kind, tubeid, identity_card, phone_number, name, district, street, community, address, test_address, result, testing_organization, re_test) " +
            "VALUES (#{uid}, #{tid}, #{kind}, #{tubeid}, #{identity_card}, #{phone_number}, #{name}, #{district}, #{street}, #{community}, #{address}, #{test_address}, #{result}, #{testing_organization}, #{re_test})")
    void insertTestRecord(NucleicAcidTestRecordDao testRecordDao);

    @Update("UPDATE health_code_nucleic_acids.nucleic_acid_test SET result = #{result}, testing_organization = #{testing_organization} WHERE tubeid = #{tubeid} AND kind = #{kind}")
    void updateTestRecord(@Param("tubeid") Long tubeid,
                          @Param("kind") Integer kind,
                          @Param("result") Integer result,
                          @Param("testing_organization") String testing_organization);

    @Update("UPDATE health_code_nucleic_acids.nucleic_acid_test SET re_test = #{re_test} WHERE tubeid = #{tubeid}")
    void updateRetestStatus(@Param("tubeid") Long tubeid,
                            @Param("re_test") Boolean re_test);

    @Update("UPDATE health_code_nucleic_acids.nucleic_acid_test SET re_test = true WHERE uid = #{uid} AND created_at >= #{time} AND re_test= false")
    void updateTestRecordReTestToTrueByUidAndTime(@Param("uid") Long uid,
                                                  @Param("time") String time);

    @Select("SELECT created_at, result, testing_organization FROM health_code_nucleic_acids.nucleic_acid_test WHERE uid = #{uid} ORDER BY created_at DESC LIMIT 1")
    NucleicAcidTestRecordDao findLastTestRecordByUid(Long uid);

    @Select("SELECT * FROM health_code_nucleic_acids.nucleic_acid_test WHERE uid = #{uid} AND created_at >= #{time}")
    List<NucleicAcidTestRecordDao> findTestRecordsByUidWithinDays(@Param("uid") Long uid,
                                                                  @Param("time") Date time);

    @Select("SELECT COUNT(*) FROM health_code_nucleic_acids.nucleic_acid_test WHERE created_at BETWEEN #{startTime} AND #{endTime}")
    long countRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT COUNT(*) FROM health_code_nucleic_acids.nucleic_acid_test WHERE (result = 2 OR result IS NULL) AND created_at BETWEEN #{startTime} AND #{endTime}")
    long countUncheckRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT COUNT(*) FROM health_code_nucleic_acids.nucleic_acid_test WHERE kind = 0 AND result = 1 AND created_at BETWEEN #{startTime} AND #{endTime}")
    long countOnePositiveRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT COUNT(*) FROM health_code_nucleic_acids.nucleic_acid_test WHERE kind != 0 AND result = 1 AND created_at BETWEEN #{startTime} AND #{endTime}")
    long countPositiveRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT * FROM health_code_nucleic_acids.nucleic_acid_test WHERE result = 1 AND created_at BETWEEN #{startTime} AND #{endTime}")
    List<NucleicAcidTestRecordDao> findPositiveRecordsWithinTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT * FROM health_code_nucleic_acids.nucleic_acid_test WHERE re_test = false AND created_at >= #{time}")
    List<NucleicAcidTestRecordDao> findUnreTestedRecordsWithinDays(@Param("time") Date time);


    @Select("SELECT uid FROM health_code_nucleic_acids.nucleic_acid_test WHERE kind = 0 AND result = 1 AND DATE(created_at) = DATE(#{time})")
    List<Long> findPositiveSingleTubeUids(@Param("time") Date time);

    @Select("SELECT uid FROM health_code_nucleic_acids.nucleic_acid_test WHERE tubeid = #{tubeid}")
    List<Long> findUidsByTubeid(@Param("tubeid") Long tubeid);
    @Select("SELECT * FROM health_code_nucleic_acids.nucleic_acid_test WHERE uid = #{uid} AND tubeid = #{tubeid} LIMIT 1")
    NucleicAcidTestRecordDao findTestRecordsByUidAndTubeid(@Param("uid") Long uid,
                                                           @Param("tubeid")Long tubeid);
}