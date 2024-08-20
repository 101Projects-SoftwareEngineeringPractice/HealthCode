package org.software.code.controller;

import org.software.code.common.consts.FSMConst;
import org.software.code.common.except.BusinessException;
import org.software.code.common.except.ExceptionEnum;
import org.software.code.common.result.Result;
import org.software.code.model.dto.HealthQRCodeDto;
import org.software.code.model.input.TranscodingEventsRequest;
import org.software.code.model.input.UidInput;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/health-code")
public class HealthCodeInternalController {
    @Autowired
    private HealthCodeService healthCodeService;


    @PostMapping("/applyHealthCode")
    public Result<?> applyHealthCode(@Valid @RequestBody UidInput request) {
        long uid = request.getUid();
        healthCodeService.applyHealthCode(uid);
        return Result.success();
    }

    @GetMapping("/getHealthCode")
    public Result<?> getHealthCode(@RequestParam(name = "uid") @NotNull(message = "uid不能为空") Long uid) {
        HealthQRCodeDto healthQRCodeDto = healthCodeService.getHealthCode(uid);
        return Result.success(healthQRCodeDto);
    }

    @PatchMapping("/transcodingHealthCodeEvents")
    public Result<?> transcodingHealthCodeEvents(@Valid @RequestBody TranscodingEventsRequest request) {
        long uid = request.getUid();
        int event = request.getEvent();
        FSMConst.HealthCodeEvent healthCodeEvent;
        switch (event) {
            case 0:
                healthCodeEvent = FSMConst.HealthCodeEvent.FORCE_GREEN;
                break;
            case 1:
                healthCodeEvent = FSMConst.HealthCodeEvent.FORCE_YELLOW;
                break;
            case 2:
                healthCodeEvent = FSMConst.HealthCodeEvent.FORCE_RED;
                break;
            default:
                throw new BusinessException(ExceptionEnum.HEALTH_CODE_EVENT_INVALID);
        }
        healthCodeService.transcodingHealthCodeEvents(uid, healthCodeEvent);
        return Result.success();
    }
}
