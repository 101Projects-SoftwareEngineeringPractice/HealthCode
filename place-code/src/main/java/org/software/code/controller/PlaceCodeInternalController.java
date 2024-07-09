package org.software.code.controller;

import org.software.code.common.result.IResult;
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

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @PostMapping("/addPlace")
    public Result<?> addPlace(@RequestBody AddPlaceInput placeDto) {
        return Result.success(placeCodeService.addPlace(placeDto));
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
            return Result.success(placeCodeService.getRecordByPid(pid, timeFormat.parse(startTime), timeFormat.parse(endTime)));
        } catch (ParseException e) {
            // 处理日期解析错误
            return Result.failed((IResult) e);
        }
    }

    @PostMapping("/scanPlaceCode")
    public  Result<?> scanPlaceCode(@RequestParam("uid") long uid, @RequestParam("token") String token) {
        placeCodeService.scanPlaceCode(uid, token);
        return Result.success("");
    }

    @PostMapping("/oppositePlaceCode")
    public Result<?> oppositePlaceCode(@RequestParam("pid") long pid, @RequestParam("status") boolean status) {
        placeCodeService.oppositePlaceCode(pid, status);
        return Result.success("反转成功");
    }

    @PostMapping("/getPlacesByUserList")
    public Result<?> getPlacesByUserList(@RequestBody List<Long> uidList,
                                          @RequestParam("start_time") String startTime,
                                          @RequestParam("end_time") String endTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(startTime);
            endDate = formatter.parse(endTime);
            return Result.success(placeCodeService.getPlacesByUserList(uidList, startDate, endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            // handle error
            return Result.failed((IResult) e);
        }
    }
}
