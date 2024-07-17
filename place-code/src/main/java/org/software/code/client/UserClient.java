package org.software.code.client;

import org.software.code.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user")
public interface UserClient {
    @PostMapping("/user/extractUidValidateToken")
    Result<?> extractUidValidateToken(@RequestHeader("Authorization") String token);

    @PostMapping("/user/extractMidValidateToken")
    Result<?> extractMidValidateToken(@RequestHeader("Authorization") String token);


    @GetMapping("/user/getUserByUID")
    Result<?> getUserByUID(@RequestParam(name = "uid") long uid);

    @GetMapping("/user/getUserByID")
    Result<?> getUserByID(@RequestParam(name = "identity_card") String identity_card);
}
