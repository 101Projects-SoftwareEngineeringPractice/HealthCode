package org.software.code.controller;

import org.software.code.common.consts.FSMConst;
import org.software.code.common.JWTUtil;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.dto.ApplyCodeRequest;
import org.software.code.dto.GetCodeDto;
import org.software.code.dto.HealthCodeInfoDto;
import org.software.code.dto.TranscodingEventsRequest;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/health-code")
public class HealthCodeController {
    @Autowired
    private HealthCodeService healthCodeService;

    /**
     * applyCode
     * 用户在申请健康码页面（个人信息填写页面）申请健康码。
     */
    @PostMapping("/applyCode")
    public Result<?> applyCode(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                               @Valid @RequestBody ApplyCodeRequest input) {
        long uid = JWTUtil.extractID(token);
        String name = input.getName();
        String phone_number = input.getPhoneNumber();
        String identity_card = input.getIdentityCard();
        int district_id = input.getDistrictId();
        int street_id = input.getStreetId();
        long community_id = input.getCommunityId();
        String address = input.getAddress();
        healthCodeService.applyCode(uid, name, phone_number, identity_card, district_id, street_id, community_id, address);
        return Result.success();

    }


    /**
     * getCode
     * 用户登录后，在首页展示健康码状态，包括绿码、黄码和红码。每次返回首页都需要重新获取，每分钟自动刷新一次。
     */
    @GetMapping("/getCode")
    public Result<?> getCode(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        long uid = JWTUtil.extractID(token);
        GetCodeDto getCodeDto = healthCodeService.getCode(uid);
        return Result.success(getCodeDto);


    }

    /**
     * health_code
     * 用户登录管理系统后，在转码管理页面选择通过身份证来对用户进行转码。
     */
    @GetMapping("/health_code")
    public Result<?> getHealthCodeInfo(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                       @RequestParam(name = "identity_card") @NotNull(message = "identity_card不能为空") String identity_card) {
        JWTUtil.extractID(token);
        HealthCodeInfoDto healthCodeInfoDto = healthCodeService.getHealthCodeInfo(identity_card);
        return Result.success(healthCodeInfoDto);

    }

    /**
     * transcodingEvents
     * 用户登录管理系统后，在转码管理页面选择通过身份证来对用户进行转码。
     */
    @PostMapping("/transcodingEvents")
    public Result<?> transcodingEvents(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                       @Valid @RequestBody TranscodingEventsRequest request) {
        long uid = request.getUid();
        int event = request.getEvent();
        FSMConst.HealthCodeEvent healthCodeEvent;
        switch (event) {
            case 0:
                healthCodeEvent = FSMConst.HealthCodeEvent.FORCE_GREEN;
                break;
            case 1:
                healthCodeEvent = FSMConst.HealthCodeEvent.FORCE_GREEN;
                break;
            case 2:
                healthCodeEvent = FSMConst.HealthCodeEvent.FORCE_GREEN;
                break;
            default:
                throw new BusinessException(ExceptionEnum.HEALTH_CODE_EVENT_INVALID);
        }
        JWTUtil.extractID(token);
        healthCodeService.transcodingHealthCodeEvents(uid, healthCodeEvent);
        return Result.success();
    }


}
