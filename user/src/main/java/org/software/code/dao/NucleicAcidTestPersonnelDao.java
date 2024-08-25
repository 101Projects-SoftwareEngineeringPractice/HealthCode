package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.software.code.model.entity.NucleicAcidTestPersonnel;

import java.util.List;
@Mapper
public interface NucleicAcidTestPersonnelDao {
    void addNucleicAcidTestPersonnel(NucleicAcidTestPersonnel nucleicAcidTestPersonnel);

    void updateStatusByTID(@Param("status") boolean status,@Param("tid") long tid);

    List<NucleicAcidTestPersonnel> getNucleicAcidUserInfoList();

    NucleicAcidTestPersonnel getNucleicAcidTestPersonnelByID(String identityCard);

    NucleicAcidTestPersonnel getNucleicAcidTestPersonnelByTID(long tid);
}
