package org.software.code.controller;

import org.software.code.common.result.IResult;
import org.software.code.common.result.Result;
import org.software.code.dao.NucleicAcidTestRecordDao;
import org.software.code.dto.NucleicAcidTestRecordDto;
import org.software.code.dto.NucleicAcidTestRecordInput;
import org.software.code.dto.NucleicAcidTestResultDto;
import org.software.code.service.NucleicAcidsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NucleicAcidsController {
    @Autowired
    private NucleicAcidsService nucleicAcidsService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 用户登录后，在首页展示最近一次核酸检测信息。
     * @param token
     * @return
     */
    @GetMapping("/nucleic-acids/getLastNucleicAcidTestRecord")
    public Result<?> getLastNucleicAcidTestRecord(@RequestHeader("Authorization") String token) {
        long uid=nucleicAcidsService.extractUidValidateToken(token);
        NucleicAcidTestResultDto nucleicAcidTestResultDto=nucleicAcidsService.getLastNucleicAcidTestRecordByUID(uid);
        return Result.success(nucleicAcidTestResultDto);
    }

    /**
     * 用户也可点击首页的查看核酸结果功能，进入核酸结果页面查看近十四天的所有核酸记录。如果用户近十四天没有核酸检测记录，弹出该用户近十四天未做核酸。
     * @param token
     * @return
     */
    @GetMapping("/nucleic-acids/getNucleicAcidTestRecord")
    public Result<?> getNucleicAcidTestRecord(@RequestHeader("Authorization") String token) {
        long uid=nucleicAcidsService.extractUidValidateToken(token);
        //TODO token验证
        List<NucleicAcidTestResultDto> nucleicAcidTestResultDtoList=nucleicAcidsService.getNucleicAcidTestRecordByUID(uid);
        return Result.success(nucleicAcidTestResultDtoList);
    }

    /**
     * 用户登录核酸小程序后，在核酸采样页面扫描条形码添加试管ID并开管（仅该试管所检测人员的第一位需要开管，后面人员延续该试管直到达到类型上限），试管ID首位代表试管类型（0单管， 1 十人混管， 2 二十人混管）。随后扫描受检者健康码提交用户信息。
     * @param tid_token
     * @param qr_token
     * @param kind
     * @param tubeid
     * @param test_address
     * @return
     */
    @PostMapping("/nucleic-acids/addNucleicAcidTestRecordByToken")
    public Result<?> addNucleicAcidTestRecordByToken(@RequestHeader("Authorization") String tid_token,
                                                     @RequestParam(name = "token") String qr_token,
                                                     @RequestParam(name = "kind") int kind,
                                                     @RequestParam(name = "tubeid") Long tubeid,
                                                     @RequestParam(name = "test_address") String test_address) {
        long tid=nucleicAcidsService.extractUidValidateToken(tid_token);
        nucleicAcidsService.addNucleicAcidTestRecordByToken(tid,qr_token,kind,tubeid,test_address);
        return Result.success("");
    }

    /**
     * 用户登录核酸小程序后，在核酸采样页面扫描条形码添加试管ID并开管（仅该试管所检测人员的第一位需要开管，后面人员延续该试管直到达到类型上限），试管ID首位代表试管类型（0单管， 1 十人混管， 2 二十人混管）。随后输入身份证号（若缺少信息则需要登记用户信息申请健康码后才能继续，该申请调用applyCode接口），提交用户信息。
     * @param token
     * @param identity_card
     * @param kind
     * @param tubeid
     * @param test_address
     * @return
     */
    @PostMapping("/nucleic-acids/addNucleicAcidTestRecordByID")
    public Result<?> addNucleicAcidTestRecordByID(@RequestHeader("Authorization") String token,
                                                  @RequestParam(name = "identity_card") String identity_card,
                                                  @RequestParam(name = "kind") int kind,
                                                  @RequestParam(name = "tubeid") Long tubeid,
                                                  @RequestParam(name = "test_address") String test_address) {
        long tid=nucleicAcidsService.extractUidValidateToken(token);
        nucleicAcidsService.addNucleicAcidTestRecordByID(tid,identity_card,kind,tubeid,test_address);
        return Result.success("");
    }

    /**
     * 用户登录核酸小程序后，在录入核酸检测结果页面填写核酸检测结果并提交，扫描试管条形码并选择检测结果（阴性or阳性）。如果检测为阳性，根据管类型为单管或混管，对用户赋红码或黄码。
     * @param token
     * @param inputs
     * @return
     */
    @PutMapping("/nucleic-acids/enterNucleicAcidTestRecord")
    public Result<?> enterNucleicAcidTestRecord(@RequestHeader("Authorization") String token,
                                                @RequestBody List<NucleicAcidTestRecordInput> inputs) {
        long tid=nucleicAcidsService.extractUidValidateToken(token);
        nucleicAcidsService.enterNucleicAcidTestRecordList(inputs);
        return Result.success("");
    }

    /**
     * 用户登录管理系统后，在管理首页筛选某时间段内核酸检测参与人员信息及状态统计。
     * @param token
     * @param start_time
     * @param end_time
     * @return
     */
    @GetMapping("/nucleic-acids/getNucleicAcidTestInfo")
    public Result<?> getNucleicAcidTestInfo(@RequestHeader("Authorization") String token,
                                            @RequestParam(name = "start_time") String start_time,
                                            @RequestParam(name = "end_time") String end_time) {
        long mid=nucleicAcidsService.extractUidValidateToken(token);
        try {
            return Result.success(nucleicAcidsService.getNucleicAcidTestInfoByTime(dateFormat.parse(start_time), dateFormat.parse(end_time)));
        } catch (ParseException e) {
            // 处理日期解析错误
            return Result.failed((IResult) e);
        }
    }

    /**
     * 用户查看某时间段内核酸检测阳性人员信息及状态。
     * @param token
     * @param start_time
     * @param end_time
     * @return
     */
    @GetMapping("/nucleic-acids/getPositiveInfo")
    public Result<?> getPositiveInfo(@RequestHeader("Authorization") String token,
                                     @RequestParam(name = "start_time") String start_time,
                                     @RequestParam(name = "end_time") String end_time) {
        long mid=nucleicAcidsService.extractUidValidateToken(token);
        try {
            return Result.success(nucleicAcidsService.getPositiveInfoByTime(dateFormat.parse(start_time), dateFormat.parse(end_time)));
        } catch (ParseException e) {
            // 处理日期解析错误
            return Result.failed((IResult) e);
        }
    }


}
