package org.software.code.service;

import org.springframework.stereotype.Service;

@Service
public interface HealthCodeService {
    String getHealthCodeInfo(String code);
}
