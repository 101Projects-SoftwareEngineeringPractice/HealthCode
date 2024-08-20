package org.software.code.client;

import org.software.code.common.result.Result;
import org.software.code.model.input.TranscodingEventsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "health-code")
public interface HealthCodeClient {

    @GetMapping("/health-code/getHealthCode")
    Result<?> getHealthCode(@RequestParam(name = "uid") Long uid);

    @PatchMapping("/health-code/transcodingHealthCodeEvents")
    Result<?> transcodingHealthCodeEvents(@RequestBody TranscodingEventsRequest request);
}
