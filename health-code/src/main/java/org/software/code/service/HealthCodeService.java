package org.software.code.service;

import org.software.code.common.consts.FSMConst;
import org.springframework.stereotype.Service;

@Service
public interface HealthCodeService {
    String getHealthCodeInfo(String code);
    int updateHealthCode(FSMConst.HealthCodeEvent event);
}
