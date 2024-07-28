package org.software.code.client;

import org.software.code.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "place-code")
public interface PlaceCodeClient {

    @PostMapping("/getPlacesByUserList")
    Result<?> getPlacesByUserList(@RequestBody List<Long> uidList,
                                         @RequestParam("start_time") String startTime,
                                         @RequestParam("end_time") String endTime);

    @GetMapping("/getRecordByPid")
    public Result<?> getRecordByPid(@RequestParam("pid") long pid,
                                    @RequestParam("start_time") String startTime,
                                    @RequestParam("end_time") String endTime);
}
