package org.software.code.controller;

import org.software.code.common.JWTUtil;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.dto.AddPlaceInput;
import org.software.code.dto.GetPlacesByUserListRequest;
import org.software.code.dto.OppositePlaceCodeRequest;
import org.software.code.dto.ScanPlaceCodeRequest;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Validated
@RestController
@RequestMapping("/place-code")
public class PlaceCodeInternalController {

    @Autowired
    private PlaceCodeService placeCodeService;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @PostMapping("/addPlace")
    public Result<?> addPlace(@RequestBody @Valid AddPlaceInput placeDto) {

        return Result.success(placeCodeService.addPlace(placeDto));

    }

    @GetMapping("/getPlaces")
    public Result<?> getPlaces() {
        return Result.success(placeCodeService.getPlaces());
    }

    @GetMapping("/getRecordByPid")
    public Result<?> getRecordByPid(@RequestParam("pid") @NotNull(message = "pid不能为空") Long pid,
                                    @RequestParam("start_time") @NotNull(message = "开始时间不能为空") String start_time,
                                    @RequestParam("end_time") @NotNull(message = "结束时间不能为空") String end_time) {
        Date startDate;
        Date endDate;
        try {
            startDate = timeFormat.parse(start_time);
            endDate = timeFormat.parse(end_time);
        } catch (ParseException e) {
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
        return Result.success(placeCodeService.getRecordByPid(pid, startDate, endDate));
    }

    @PostMapping("/scanPlaceCode")
    public Result<?> scanPlaceCode(@Valid @RequestBody ScanPlaceCodeRequest request) {
        long pid = JWTUtil.extractID(request.getToken());
        placeCodeService.scanPlaceCode(request.getUid(), pid);
        return Result.success();

    }

    @PostMapping("/oppositePlaceCode")
    public Result<?> oppositePlaceCode(@Valid @RequestBody OppositePlaceCodeRequest request) {
        placeCodeService.oppositePlaceCode(request.getPid(), request.getStatus());
        return Result.success();
    }

    @PostMapping("/getPlacesByUserList")
    public Result<?> getPlacesByUserList(@Valid @RequestBody GetPlacesByUserListRequest request) {
        Date startDate;
        Date endDate;
        try {
            startDate = timeFormat.parse(request.getStart_time());
            endDate = timeFormat.parse(request.getEnd_time());
        } catch (ParseException e) {
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
        return Result.success(placeCodeService.getPlacesByUserList(request.getUidList(), startDate, endDate));
    }


}
