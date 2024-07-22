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
@RequestMapping(value = "/nucleic-acids")
public class NucleicAcidsInternalController {
    @Autowired
    private NucleicAcidsService nucleicAcidsService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/addNucleicAcidTestRecord")
    public Result<?> addNucleicAcidTestRecord(@RequestBody NucleicAcidTestRecordDto testRecord) {
        try {
            nucleicAcidsService.addNucleicAcidTestRecord(testRecord);
            return Result.success(testRecord);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PutMapping("/enterNucleicAcidTestRecordList")
    public Result<?> enterNucleicAcidTestRecordList(@RequestBody List<NucleicAcidTestRecordInput> testRecords) {
        try {
            nucleicAcidsService.enterNucleicAcidTestRecordList(testRecords);
            return Result.success(testRecords);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }


    @GetMapping("/getLastNucleicAcidTestRecordByUID")
    public Result<?> getLastNucleicAcidTestRecordByUID(@RequestParam long uid) {
        try {
            return Result.success(nucleicAcidsService.getLastNucleicAcidTestRecordByUID(uid));
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getNucleicAcidTestRecordByUID")
    public Result<?> getNucleicAcidTestRecordByUID(@RequestParam long uid) {
        try {
            return Result.success(nucleicAcidsService.getNucleicAcidTestRecordByUID(uid));
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }

    }


    @GetMapping("/getNucleicAcidTestInfoByTime")
    public Result<?> getNucleicAcidTestInfoByTime(@RequestParam("start_time") String startTime, @RequestParam("end_time") String endTime) {
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            return Result.success(nucleicAcidsService.getNucleicAcidTestInfoByTime(startDate, endDate));
        } catch (ParseException e) {
            return Result.failed("服务执行失败，请稍后重试");

        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getPositiveInfoByTime")
    public Result<?> getPositiveInfoByTime(@RequestParam("start_time") String startTime, @RequestParam("end_time") String endTime) {
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            return Result.success(nucleicAcidsService.getPositiveInfoByTime(startDate,endDate));
        } catch (ParseException e) {
            return Result.failed("服务执行失败，请稍后重试");
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/noticeReTest")
    public Result<?> noticeReTest() {
        try {
            return Result.success(nucleicAcidsService.getNoticeReTestRecords());
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/autoModify")
    public Result<?> autoModify() {
        try {
            return Result.success(nucleicAcidsService.autoModify());
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }


}
