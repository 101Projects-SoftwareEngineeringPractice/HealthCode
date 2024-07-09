package org.software.code.mapper;

import org.apache.ibatis.annotations.*;
import org.software.code.dao.UidMappingDao;
import org.software.code.dao.UserInfoDao;

@Mapper
public interface UidMappingMapper {

    @Select("SELECT * FROM health_code_user.uid_mapping WHERE openid = #{openid} limit 1")
    UidMappingDao getUidMappingByOpenID(@Param("openid") String openID);

    @Insert("INSERT INTO health_code_user.uid_mapping (uid, openid) VALUES (#{uid}, #{openid});")
    void addUserMapping(UidMappingDao uidMapping);
}
