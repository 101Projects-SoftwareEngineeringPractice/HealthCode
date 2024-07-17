package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.NucleicAcidTestPersonnelDao;
//import org.software.code.dto.NucleicAcidTestPersonnelDto;

import java.util.List;

@Mapper
public interface NucleicAcidTestPersonnelMapper {
    @Select("SELECT * FROM health_code_user.nucleic_acid_test_personnel")
    List<NucleicAcidTestPersonnelDao> getNucleicAcidUserInfoList();

    @Insert("INSERT INTO health_code_user.nucleic_acid_test_personnel (tid, identity_card, password_hash, name,status) VALUES (#{tid}, #{identity_card}, #{password_hash}, #{name}, #{status});")
    void addNucleicAcidTestPersonnel(NucleicAcidTestPersonnelDao nucleicAcidTestPersonnelDao);

    @Select("SELECT * FROM health_code_user.nucleic_acid_test_personnel WHERE identity_card = #{identity_card} limit 1")
    NucleicAcidTestPersonnelDao getNucleicAcidTestPersonnelByID(String identityCard);

    @Select("SELECT * FROM health_code_user.nucleic_acid_test_personnel WHERE tid = #{tid} limit 1")
    NucleicAcidTestPersonnelDao getNucleicAcidTestPersonnelByTID(long tid);

    @Update("UPDATE health_code_user.nucleic_acid_test_personnel SET status = #{status} WHERE tid = #{tid}")
    void updateStatusByTID(@Param("status") boolean status, @Param("tid") long tid);
}
