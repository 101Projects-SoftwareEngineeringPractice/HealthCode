package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.UidMappingDao;

@Mapper
public interface UidMappingMapper {
    @Insert("INSERT INTO health_code_user.uid_mapping (uid, openid) VALUES (#{uid}, #{openid});")
    void addUserMapping(UidMappingDao uidMapping);

    @Select("SELECT * FROM health_code_user.uid_mapping WHERE openid = #{openid} limit 1")
    UidMappingDao getUidMappingByOpenID(@Param("openid") String openID);

}
