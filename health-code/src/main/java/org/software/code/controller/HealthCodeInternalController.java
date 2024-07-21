package org.software.code.controller;

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
        try {
            healthCodeService.transcodingHealthCodeEvents(uid, event);
            return Result.success();
        } catch (
                Exception e) {
            return Result.failed(e.getMessage());
        }
    }
}
