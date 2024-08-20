package org.software.code.client;

import org.software.code.common.result.Result;
import org.software.code.model.input.GetPlacesByUserListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "place-code")
public interface PlaceCodeClient {

    @PostMapping("/place-code/getPlacesByUserList")
    Result<?> getPlacesByUserList(@RequestBody GetPlacesByUserListRequest request);

    @GetMapping("/place-code/getRecordByPid")
    Result<?> getRecordByPid(@RequestParam("pid") Long pid,
                                    @RequestParam("start_time") String startTime,
                                    @RequestParam("end_time") String endTime);

    @GetMapping("/place-code/getAllPids")
    Result<?> getAllPids();

    @PutMapping("/place-code/setPlaceRisk")
    Result<?> setPlaceRisk(@RequestParam("pid") Long pid, @RequestParam("risk")  String risk);
}
