package org.software.code.service;

import org.software.code.dto.GetCodeDto;
import org.software.code.dto.HealthCodeInfoDto;
import org.software.code.dto.HealthQRCodeDto;
import org.springframework.stereotype.Service;

@Service
public interface HealthCodeService {
    long extractUidValidateToken(String token);

    void applyHealthCode(long uid);


    HealthQRCodeDto getHealthCode(long uid);

    void transcodingHealthCodeEvents(long uid, int event);

    void applyCode(long uid, String name, String phoneNumber, String identityCard, int districtId, int streetId, int communityId, String address);

    GetCodeDto getCode(long uid);

    HealthCodeInfoDto getHealthCodeInfo(String identityCard);

    Boolean validateToken(String token);

    long extractUID(String token);

    long extractUIDValidateQRCodeToken(String qrcode_token);
}
