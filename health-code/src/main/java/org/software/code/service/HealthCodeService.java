package org.software.code.service;

import org.software.code.common.consts.FSMConst;
import org.software.code.model.dto.GetCodeDto;
import org.software.code.model.dto.HealthCodeInfoDto;
import org.software.code.model.dto.HealthQRCodeDto;
import org.software.code.model.input.ApplyCodeRequest;
import org.software.code.model.input.TranscodingHealthCodeEventsInput;
import org.software.code.model.input.UserInfoRequest;
import org.springframework.stereotype.Service;

@Service
public interface HealthCodeService {

    void applyHealthCode(long uid);

    HealthQRCodeDto getHealthCode(long uid);

    void transcodingHealthCodeEvents(TranscodingHealthCodeEventsInput input);

    void applyCode(UserInfoRequest userInfoRequest);

    GetCodeDto getCode(long uid);

    HealthCodeInfoDto getHealthCodeInfo(String identityCard);
}
