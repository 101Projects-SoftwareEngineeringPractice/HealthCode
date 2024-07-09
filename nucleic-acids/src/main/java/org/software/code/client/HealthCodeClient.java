package org.software.code.client;

import org.software.code.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "health-code")
public interface HealthCodeClient {

    @GetMapping("/getHealthCode")
    Result<?> getHealthCode(@RequestParam(name = "uid") long uid);

    @PatchMapping("/transcodingHealthCodeEvents")
    Result<?> transcodingHealthCodeEvents(@RequestParam(name = "uid") long uid,
                                                 @RequestParam(name = "event") int event);
    @PostMapping("/extractUIDValidateQRCodeToken")
    Result<?> extractUIDValidateQRCodeToken(@RequestParam(name = "qrcode_token") String qrcode_token);
}
