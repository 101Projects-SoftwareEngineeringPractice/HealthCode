package org.software.code.controller;

import org.software.code.common.utils.JWTUtil;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.model.input.*;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Validated
@RestController
@RequestMapping("/place-code")
public class PlaceCodeInternalController {

    private static final Logger logger = LogManager.getLogger(PlaceCodeInternalController.class);

    @Autowired
    private PlaceCodeService placeCodeService;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/addPlace")
    public Result<?> addPlace(@RequestBody @Valid AddPlaceRequest request) {
        return Result.success(placeCodeService.addPlace(request));
    }

    @GetMapping("/getPlaces")
    public Result<?> getPlaces() {
        return Result.success(placeCodeService.getPlaces());
    }

    @GetMapping("/getRecordByPid")
    public Result<?> getRecordByPid(@RequestParam("pid") @NotNull(message = "pid不能为空") Long pid,
                                    @RequestParam("start_time") @NotNull(message = "开始时间不能为空") String startTime,
                                    @RequestParam("end_time") @NotNull(message = "结束时间不能为空") String endTime) {
        Date startDate;
        Date endDate;
        try {
            startDate = timeFormat.parse(startTime);
            endDate = timeFormat.parse(endTime);
        } catch (ParseException e) {
            logger.error("Date parsing error: start_time={}, end_time={}, message={}", startTime, endTime, e.getMessage());
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
        return Result.success(placeCodeService.getRecordByPid(pid, startDate, endDate));
    }

    @PostMapping("/scanPlaceCode")
    public Result<?> scanPlaceCode(@Valid @RequestBody ScanPlaceCodeRequest request) {
        long pid = JWTUtil.extractID(request.getToken());
        ScanPlaceCodeInput input=new ScanPlaceCodeInput(request.getUid(),pid);
        placeCodeService.scanPlaceCode(input);
        return Result.success();
    }

    @PostMapping("/oppositePlaceCode")
    public Result<?> oppositePlaceCode(@Valid @RequestBody OppositePlaceCodeRequest request) {
        placeCodeService.oppositePlaceCode(request);
        return Result.success();
    }

    @PostMapping("/getPlacesByUserList")
    public Result<?> getPlacesByUserList(@Valid @RequestBody GetPlacesByUserListRequest request) {
        return Result.success(placeCodeService.getPlacesByUserList(request));
    }

    @GetMapping("/getAllPids")
    public Result<?> getAllPids() {
        List<Long> pids = placeCodeService.getAllPids();
        return Result.success(pids);
    }

    @PutMapping("/setPlaceRisk")
    public Result<?> setPlaceRisk(@RequestParam("pid") Long pid, @RequestParam("risk") String risk) {
        //TODO 设置场所的风险等级
        return Result.success();
    }

}
