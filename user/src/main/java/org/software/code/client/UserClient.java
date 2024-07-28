package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user")
public interface UserClient {
}
