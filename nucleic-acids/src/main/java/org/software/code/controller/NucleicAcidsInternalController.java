package org.software.code.controller;

import org.software.code.common.result.IResult;
import org.software.code.common.result.Result;
import org.software.code.dto.NucleicAcidTestRecordDto;
import org.software.code.dto.NucleicAcidTestRecordInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.software.code.service.NucleicAcidsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value ="/nucleic-acids")
public class NucleicAcidsInternalController {
    @Autowired
    private NucleicAcidsService nucleicAcidsService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/addNucleicAcidTestRecord")
    public Result<?> addNucleicAcidTestRecord(@RequestBody NucleicAcidTestRecordDto testRecord) {
        nucleicAcidsService.addNucleicAcidTestRecord(testRecord);
        return Result.success(testRecord);
    }

    @PutMapping("/enterNucleicAcidTestRecordList")
    public Result<?> enterNucleicAcidTestRecordList(@RequestBody List<NucleicAcidTestRecordInput> testRecords) {
        nucleicAcidsService.enterNucleicAcidTestRecordList(testRecords);
        return Result.success(testRecords);
    }


    @GetMapping("/getLastNucleicAcidTestRecordByUID")
    public Result<?> getLastNucleicAcidTestRecordByUID(@RequestParam long uid) {
        return Result.success(nucleicAcidsService.getLastNucleicAcidTestRecordByUID(uid));
    }

    @GetMapping("/getNucleicAcidTestRecordByUID")
    public Result<?> getNucleicAcidTestRecordByUID(@RequestParam long uid) {
        return Result.success(nucleicAcidsService.getNucleicAcidTestRecordByUID(uid));
    }


    @GetMapping("/getNucleicAcidTestInfoByTime")
    public Result<?> getNucleicAcidTestInfoByTime(@RequestParam("start_time") String startTime, @RequestParam("end_time") String endTime) {
        try {
            return Result.success(nucleicAcidsService.getNucleicAcidTestInfoByTime(dateFormat.parse(startTime), dateFormat.parse(endTime)));
        } catch (ParseException e) {
            // 处理日期解析错误
            return Result.failed((IResult) e);
        }
    }

    @GetMapping("/getPositiveInfoByTime")
    public Result<?> getPositiveInfoByTime(@RequestParam("start_time") String startTime, @RequestParam("end_time") String endTime) {

        try {
            return Result.success(nucleicAcidsService.getPositiveInfoByTime(dateFormat.parse(startTime), dateFormat.parse(endTime)));
        } catch (ParseException e) {
            // 处理日期解析错误
            return Result.failed((IResult) e);
        }
    }

    @GetMapping("/noticeReTest")
    public Result<?> noticeReTest() {
        return Result.success(nucleicAcidsService.getNoticeReTestRecords());
    }

    @GetMapping("/autoModify")
    public Result<?> autoModify() {
        return Result.success(nucleicAcidsService.autoModify());
    }



}
