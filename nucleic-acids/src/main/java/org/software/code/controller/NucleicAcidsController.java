package org.software.code.controller;

import org.software.code.common.JWTUtil;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.dto.AddNucleicAcidTestRecordByIDRequest;
import org.software.code.dto.AddNucleicAcidTestRecordRequest;
import org.software.code.dto.NucleicAcidTestRecordInput;
import org.software.code.dto.NucleicAcidTestResultDto;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Validated
@RestController
@RequestMapping("/nucleic-acids")
public class NucleicAcidsController {
    private static final Logger logger = LogManager.getLogger(NucleicAcidsController.class);

    @Autowired
    private NucleicAcidsService nucleicAcidsService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/getLastNucleicAcidTestRecord")
    public Result<?> getLastNucleicAcidTestRecord(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        long uid = JWTUtil.extractID(token);
        NucleicAcidTestResultDto nucleicAcidTestResultDto = nucleicAcidsService.getLastNucleicAcidTestRecordByUID(uid);
        return Result.success(nucleicAcidTestResultDto);
    }

    @GetMapping("/getNucleicAcidTestRecord")
    public Result<?> getNucleicAcidTestRecord(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        long uid = JWTUtil.extractID(token);
        List<NucleicAcidTestResultDto> nucleicAcidTestResultDtoList = nucleicAcidsService.getNucleicAcidTestRecordByUID(uid);
        return Result.success(nucleicAcidTestResultDtoList);
    }

    @PostMapping("/addNucleicAcidTestRecordByToken")
    public Result<?> addNucleicAcidTestRecordByToken(@RequestHeader("Authorization") String tidToken,
                                                     @Valid @RequestBody AddNucleicAcidTestRecordRequest request) {
        String qrToken = request.getToken();
        int kind = request.getKind();
        Long tubeId = request.getTubeid();
        String testAddress = request.getTest_address();

        long tid = JWTUtil.extractID(tidToken);
        long uid = JWTUtil.extractID(qrToken);
        nucleicAcidsService.addNucleicAcidTestRecordByToken(tid, uid, kind, tubeId, testAddress);
        return Result.success();
    }

    @PostMapping("/addNucleicAcidTestRecordByID")
    public Result<?> addNucleicAcidTestRecordByID(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                                  @Valid @RequestBody AddNucleicAcidTestRecordByIDRequest request) {
        String identityCard = request.getIdentity_card();
        int kind = request.getKind();
        Long tubeId = request.getTubeid();
        String testAddress = request.getTest_address();

        long tid = JWTUtil.extractID(token);
        nucleicAcidsService.addNucleicAcidTestRecordByID(tid, identityCard, kind, tubeId, testAddress);
        return Result.success();
    }

    @PutMapping("/enterNucleicAcidTestRecord")
    public Result<?> enterNucleicAcidTestRecord(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                                @RequestBody List<NucleicAcidTestRecordInput> inputs) {
        JWTUtil.extractID(token);
        nucleicAcidsService.enterNucleicAcidTestRecordList(inputs);
        return Result.success();
    }

    @GetMapping("/getNucleicAcidTestInfo")
    public Result<?> getNucleicAcidTestInfo(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                            @RequestParam(name = "start_time") @NotNull(message = "开始日期不能为空") String start_time,
                                            @RequestParam(name = "end_time") @NotNull(message = "结束日期不能为空") String end_time) {
        try {
            JWTUtil.extractID(token);
            Date startDate = dateFormat.parse(start_time);
            Date endDate = dateFormat.parse(end_time);
            return Result.success(nucleicAcidsService.getNucleicAcidTestInfoByTime(startDate, endDate));
        } catch (ParseException e) {
            logger.error("Date parsing error: {}", e.getMessage());
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
    }

    @GetMapping("/getPositiveInfo")
    public Result<?> getPositiveInfo(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                     @RequestParam(name = "start_time") @NotNull(message = "开始日期不能为空") String start_time,
                                     @RequestParam(name = "end_time") @NotNull(message = "结束日期不能为空") String end_time) {
        try {
            JWTUtil.extractID(token);
            Date startDate = dateFormat.parse(start_time);
            Date endDate = dateFormat.parse(end_time);
            return Result.success(nucleicAcidsService.getPositiveInfoByTime(startDate, endDate));
        } catch (ParseException e) {
            logger.error("Date parsing error: {}", e.getMessage());
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
    }
}