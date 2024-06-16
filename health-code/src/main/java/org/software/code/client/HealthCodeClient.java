package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "health-code")
public interface HealthCodeClient {
    @GetMapping("/health-code/{code}")
    String getHealthCodeInfo(@PathVariable("code") String code);
}
