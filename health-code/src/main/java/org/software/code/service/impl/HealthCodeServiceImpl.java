package org.software.code.service.impl;

import org.software.code.service.HealthCodeService;
import org.springframework.stereotype.Service;

@Service
public class HealthCodeServiceImpl implements HealthCodeService {
    public String getHealthCodeInfo(String code) {
        return "Health code info for " + code;
    }
}
