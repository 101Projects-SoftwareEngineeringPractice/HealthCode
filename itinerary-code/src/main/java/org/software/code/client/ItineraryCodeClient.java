package org.software.code.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "itinerary-code")
public interface ItineraryCodeClient {
}
