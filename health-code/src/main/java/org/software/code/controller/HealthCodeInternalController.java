package org.software.code.controller;

import org.software.code.common.consts.FSMConst;
import org.software.code.common.result.Result;
import org.software.code.dto.HealthQRCodeDto;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-code")
public class HealthCodeInternalController {
    @Autowired
    private HealthCodeService healthCodeService;


    @PostMapping("/applyHealthCode")
    public Result<?> applyHealthCode(@RequestParam(name = "uid") long uid) {
        try {
            healthCodeService.applyHealthCode(uid);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getHealthCode")
    public Result<?> getHealthCode(@RequestParam(name = "uid") long uid) {
        try {
            HealthQRCodeDto healthQRCodeDto = healthCodeService.getHealthCode(uid);
            return Result.success(healthQRCodeDto);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PatchMapping("/transcodingHealthCodeEvents")
    public Result<?> transcodingHealthCodeEvents(@RequestParam(name = "uid") long uid,
                                                 @RequestParam(name = "event") int event) {
        FSMConst.HealthCodeEvent healthCodeEvent;
        if(event==0){
            healthCodeEvent=FSMConst.HealthCodeEvent.FORCE_GREEN;
        }
        else if(event==1){
            healthCodeEvent=FSMConst.HealthCodeEvent.FORCE_GREEN;

        }
        else if (event==2){
            healthCodeEvent=FSMConst.HealthCodeEvent.FORCE_GREEN;
        }
        else {
            return Result.failed("服务执行错误，请稍后重试");
        }
        try {
            healthCodeService.transcodingHealthCodeEvents(uid, healthCodeEvent);
            return Result.success();
        } catch (
                Exception e) {
            return Result.failed(e.getMessage());
        }
    }
}
