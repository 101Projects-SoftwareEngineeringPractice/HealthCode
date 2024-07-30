package org.software.code.controller;

import org.software.code.common.result.Result;

import org.software.code.dto.NucleicAcidTestRecordDto;
import org.software.code.dto.NucleicAcidTestRecordInput;
import org.software.code.service.NucleicAcidsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Validated
@RestController
@RequestMapping(value = "/nucleic-acids")
public class NucleicAcidsInternalController {
    @Autowired
    private NucleicAcidsService nucleicAcidsService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/addNucleicAcidTestRecord")
    public Result<?> addNucleicAcidTestRecord(@RequestBody @Valid NucleicAcidTestRecordDto testRecord) {

        nucleicAcidsService.addNucleicAcidTestRecord(testRecord);
        return Result.success(testRecord);

    }

    @PutMapping("/enterNucleicAcidTestRecordList")
    public Result<?> enterNucleicAcidTestRecordList(@RequestBody @Valid List<NucleicAcidTestRecordInput> testRecords) {

        nucleicAcidsService.enterNucleicAcidTestRecordList(testRecords);
        return Result.success(testRecords);

    }

    @GetMapping("/getLastNucleicAcidTestRecordByUID")
    public Result<?> getLastNucleicAcidTestRecordByUID(@RequestParam @NotNull(message = "uid不能为空") Long uid) {

        return Result.success(nucleicAcidsService.getLastNucleicAcidTestRecordByUID(uid));

    }

    @GetMapping("/getNucleicAcidTestRecordByUID")
    public Result<?> getNucleicAcidTestRecordByUID(@RequestParam @NotNull(message = "uid不能为空") Long uid) {
        return Result.success(nucleicAcidsService.getNucleicAcidTestRecordByUID(uid));
    }


    @GetMapping("/getNucleicAcidTestInfoByTime")
    public Result<?> getNucleicAcidTestInfoByTime(@RequestParam("start_time") @NotNull(message = "开始时间不能为空") String startTime,
                                                  @RequestParam("end_time") @NotNull(message = "结束时间不能为空") String endTime) {
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            return Result.success(nucleicAcidsService.getNucleicAcidTestInfoByTime(startDate, endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.failed("服务执行失败，请稍后重试");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getPositiveInfoByTime")
    public Result<?> getPositiveInfoByTime(@RequestParam("start_time") @NotNull(message = "开始时间不能为空") String startTime,
                                           @RequestParam("end_time") @NotNull(message = "结束时间不能为空") String endTime) {
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            return Result.success(nucleicAcidsService.getPositiveInfoByTime(startDate, endDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.failed("服务执行失败，请稍后重试");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(e.getMessage());
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
