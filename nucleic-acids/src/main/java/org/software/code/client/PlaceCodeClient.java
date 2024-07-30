package org.software.code.client;

import org.software.code.common.result.Result;
import org.software.code.dto.GetPlacesByUserListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "place-code")
public interface PlaceCodeClient {

    @PostMapping("/place-code/getPlacesByUserList")
    Result<?> getPlacesByUserList(@RequestBody GetPlacesByUserListRequest request);

    @GetMapping("/place-code/getRecordByPid")
    Result<?> getRecordByPid(@RequestParam("pid") Long pid,
                                    @RequestParam("start_time") String startTime,
                                    @RequestParam("end_time") String endTime);
}
