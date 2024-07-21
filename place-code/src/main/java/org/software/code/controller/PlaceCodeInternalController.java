package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.AddPlaceInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.software.code.service.PlaceCodeService;

@RestController
@RequestMapping("/place-code")
public class PlaceCodeInternalController {

    @Autowired
    private PlaceCodeService placeCodeService;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @PostMapping("/addPlace")
    public Result<?> addPlace(@RequestBody AddPlaceInput placeDto) {
        try {
            return Result.success(placeCodeService.addPlace(placeDto));
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getPlaces")
    public Result<?> getPlaces() {
        return Result.success(placeCodeService.getPlaces());
    }

    @GetMapping("/getRecordByPid")
    public Result<?> getRecordByPid(@RequestParam("pid") long pid,
                                    @RequestParam("start_time") String startTime,
                                    @RequestParam("end_time") String endTime) {
        try {
            Date startDate = timeFormat.parse(startTime);
            Date endDate = timeFormat.parse(endTime);
            return Result.success(placeCodeService.getRecordByPid(pid, startDate, endDate));
        } catch (ParseException e) {
            return Result.failed("服务执行失败，请稍后重试");
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/scanPlaceCode")
    public Result<?> scanPlaceCode(@RequestParam("uid") long uid, @RequestParam("token") String token) {
        try {
            placeCodeService.scanPlaceCode(uid, token);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/oppositePlaceCode")
    public Result<?> oppositePlaceCode(@RequestParam("pid") long pid, @RequestParam("status") boolean status) {
        try {
            placeCodeService.oppositePlaceCode(pid, status);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/getPlacesByUserList")
    public Result<?> getPlacesByUserList(@RequestBody List<Long> uidList,
                                         @RequestParam("start_time") String startTime,
                                         @RequestParam("end_time") String endTime) {
        try {
            Date startDate = timeFormat.parse(startTime);
            Date endDate = timeFormat.parse(endTime);
            return Result.success(placeCodeService.getPlacesByUserList(uidList, startDate, endDate));
        } catch (ParseException e) {
            return Result.failed("服务执行失败，请稍后重试");
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }
}
