package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "health-code")
public interface HealthCodeClient {
}
