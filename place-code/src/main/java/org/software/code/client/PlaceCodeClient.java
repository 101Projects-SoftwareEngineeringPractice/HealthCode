package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "place-code")
public interface PlaceCodeClient {
}
