package org.software.code.dao;

import org.apache.ibatis.annotations.Mapper;
import org.software.code.model.entity.UidMapping;
@Mapper
public interface UidMappingDao {
    void addUserMapping(UidMapping uidMapping);

    UidMapping getUidMappingByOpenID(String openID);
}
