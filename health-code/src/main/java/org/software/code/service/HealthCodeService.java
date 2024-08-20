package org.software.code.service;

import org.software.code.common.consts.FSMConst;
import org.software.code.model.dto.GetCodeDto;
import org.software.code.model.dto.HealthCodeInfoDto;
import org.software.code.model.dto.HealthQRCodeDto;
import org.springframework.stereotype.Service;

@Service
public interface HealthCodeService {

    void applyHealthCode(long uid);

    HealthQRCodeDto getHealthCode(long uid);

    void transcodingHealthCodeEvents(long uid, FSMConst.HealthCodeEvent event);

    void applyCode(long uid, String name, String phoneNumber, String identityCard, int districtId, int streetId, long communityId, String address);

    GetCodeDto getCode(long uid);

    HealthCodeInfoDto getHealthCodeInfo(String identityCard);
}
