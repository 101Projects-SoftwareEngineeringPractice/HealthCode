package org.software.code.client;

import org.software.code.common.result.Result;
import org.software.code.model.input.UserInfoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user")
public interface UserClient {
    @PutMapping("/user/addUserInfo")
    Result<?> addUserInfo(@RequestBody UserInfoRequest request);

    @GetMapping("/user/delete")
    Result<?> deleteUserInfo(@RequestParam("uid") long uid);

    @GetMapping("/user/getUserByUID")
    Result<?> getUserByUID(@RequestParam(name = "uid") long uid);

    @GetMapping("/user/getUserByID")
    Result<?> getUserByID(@RequestParam(name = "identity_card") String identity_card);
}
