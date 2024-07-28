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
import java.util.Map;

@Validated
@RestController
@RequestMapping("/nucleic-acids")
public class NucleicAcidsController {
    @Autowired
    private NucleicAcidsService nucleicAcidsService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 用户登录后，在首页展示最近一次核酸检测信息。
     */
    @GetMapping("/getLastNucleicAcidTestRecord")
    public Result<?> getLastNucleicAcidTestRecord(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {

        long uid = JWTUtil.extractID(token);
        NucleicAcidTestResultDto nucleicAcidTestResultDto = nucleicAcidsService.getLastNucleicAcidTestRecordByUID(uid);
        return Result.success(nucleicAcidTestResultDto);

    }

    /**
     * 用户也可点击首页的查看核酸结果功能，进入核酸结果页面查看近十四天的所有核酸记录。如果用户近十四天没有核酸检测记录，弹出该用户近十四天未做核酸。
     */
    @GetMapping("/getNucleicAcidTestRecord")
    public Result<?> getNucleicAcidTestRecord(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {

        long uid = JWTUtil.extractID(token);
        List<NucleicAcidTestResultDto> nucleicAcidTestResultDtoList = nucleicAcidsService.getNucleicAcidTestRecordByUID(uid);
        return Result.success(nucleicAcidTestResultDtoList);

    }

    /**
     * 用户登录核酸小程序后，在核酸采样页面扫描条形码添加试管ID并开管（仅该试管所检测人员的第一位需要开管，后面人员延续该试管直到达到类型上限），试管ID首位代表试管类型（0单管， 1 十人混管， 2 二十人混管）。随后扫描受检者健康码提交用户信息。
     */
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

    /**
     * 用户登录核酸小程序后，在核酸采样页面扫描条形码添加试管ID并开管（仅该试管所检测人员的第一位需要开管，后面人员延续该试管直到达到类型上限），试管ID首位代表试管类型（0单管， 1 十人混管， 2 二十人混管）。随后输入身份证号（若缺少信息则需要登记用户信息申请健康码后才能继续，该申请调用applyCode接口），提交用户信息。
     */
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

    /**
     * 用户登录核酸小程序后，在录入核酸检测结果页面填写核酸检测结果并提交，扫描试管条形码并选择检测结果（阴性or阳性）。如果检测为阳性，根据管类型为单管或混管，对用户赋红码或黄码。
     */
    @PutMapping("/enterNucleicAcidTestRecord")
    public Result<?> enterNucleicAcidTestRecord(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                                @RequestBody List<NucleicAcidTestRecordInput> inputs) {

        JWTUtil.extractID(token);
        nucleicAcidsService.enterNucleicAcidTestRecordList(inputs);
        return Result.success();

    }


    /**
     * 用户登录管理系统后，在管理首页筛选某时间段内核酸检测参与人员信息及状态统计。
     */
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
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
    }

    /**
     * 用户查看某时间段内核酸检测阳性人员信息及状态。
     */
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
            return Result.failed(ExceptionEnum.DATETIME_FORMAT_ERROR.getMsg());
        }
    }


}
