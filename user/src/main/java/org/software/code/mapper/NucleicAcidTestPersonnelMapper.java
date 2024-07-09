package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.NucleicAcidTestPersonnelDao;
//import org.software.code.dto.NucleicAcidTestPersonnelDto;

import java.util.List;

@Mapper
public interface NucleicAcidTestPersonnelMapper {
    @Select("SELECT * FROM health_code_user.nucleic_acid_test_personnel")
    List<NucleicAcidTestPersonnelDao> getNucleicAcidUserInfoList();

    //    @Select("Select * from health_code_user.user_info where uid = #{uid} limit 1")
//    UserInfoDao find(@Param("uid") long uid);
//
//    @Update("UPDATE health_code_user.user_info SET district = #{district} WHERE uid = #{uid}")
//    void updateUser(@Param("district") int district, @Param("uid") long uid);
//
//    @Delete("DELETE FROM health_code_user.user_info WHERE uid = #{uid}")
//    void deleteUser(@Param("uid") Long uid);
//
//    @Select("SELECT * FROM health_code_user.user_info WHERE uid = #{uid} limit 1")
//    UserInfoDao getUserByUID(long uid);
//
//    @Select("SELECT * FROM health_code_user.user_info WHERE identity_card = #{identity_card} limit 1")
//    UserInfoDao getUserByID(String identity_card);
    @Insert("INSERT INTO health_code_user.nucleic_acid_test_personnel (tid, identity_card, password_hash, name,status) VALUES (#{tid}, #{identity_card}, #{password_hash}, #{name}, #{status});")
    void addNucleicAcidTestPersonnel(NucleicAcidTestPersonnelDao nucleicAcidTestPersonnelDao);

    @Select("SELECT * FROM health_code_user.nucleic_acid_test_personnel WHERE identity_card = #{identity_card} limit 1")
    NucleicAcidTestPersonnelDao getNucleicAcidTestPersonnelByID(String identityCard);
    @Select("SELECT * FROM health_code_user.nucleic_acid_test_personnel WHERE tid = #{tid} limit 1")
    NucleicAcidTestPersonnelDao getNucleicAcidTestPersonnelByTID(long tid);
    @Update("UPDATE health_code_user.nucleic_acid_test_personnel SET status = #{status} WHERE tid = #{tid}")
    void updateStatusByTID(@Param("status") boolean status, @Param("tid") long tid);
}
