package org.software.code.client;

import org.software.code.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user")
public interface UserClient {
    @PostMapping("/user/extractUidValidateToken")
    Result<?> extractUidValidateToken(@RequestHeader("Authorization") String token);
    @PostMapping("/user/extractMidValidateToken")
    Result<?> extractMidValidateToken(@RequestHeader("Authorization") String token);

    @PutMapping("/user/addUserInfo")
    Result<?> addUserInfo(@RequestParam(name = "uid") long uid,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "phone_number") String phone_number,
                          @RequestParam(name = "identity_card") String identity_card,
                          @RequestParam(name = "district") int district,
                          @RequestParam(name = "street") int street,
                          @RequestParam(name = "community") int community,
                          @RequestParam(name = "address") String address);

    @GetMapping("/user/getUserByUID")
    Result<?> getUserByUID(@RequestParam(name = "uid") long uid);

    @GetMapping("/user/getUserByID")
    Result<?> getUserByID(@RequestParam(name = "identity_card") String identity_card);
}
