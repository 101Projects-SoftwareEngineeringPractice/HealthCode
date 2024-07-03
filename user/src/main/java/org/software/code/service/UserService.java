package org.software.code.service;

import org.software.code.dto.UserInfoDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    String testOthers();

    UserInfoDto getInfo(Long uid);

    void updateInfo(Long uid, int district);

    UserInfoDto getUserByUID(long uid);

    UserInfoDto getUserByID(String identity_card);
}
