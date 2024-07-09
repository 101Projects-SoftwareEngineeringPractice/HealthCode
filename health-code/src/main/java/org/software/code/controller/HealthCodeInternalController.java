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
        healthCodeService.applyHealthCode(uid);
        return Result.success("");// 技术文档未说明出参
    }

    @GetMapping("/getHealthCode")
    public Result<?> getHealthCode(@RequestParam(name = "uid") long uid) {
        HealthQRCodeDto healthQRCodeDto = healthCodeService.getHealthCode(uid);
        return Result.success(healthQRCodeDto);
    }

    @PatchMapping("/transcodingHealthCodeEvents")
    public Result<?> transcodingHealthCodeEvents(@RequestParam(name = "uid") long uid,
                                                 @RequestParam(name = "event") int event) {
        healthCodeService.transcodingHealthCodeEvents(uid, event);
        return Result.success("");// 技术文档未说明出参
    }

    /****补充**/

    @PostMapping("/extractUIDValidateQRCodeToken")
    public Result<?> extractUIDValidateQRCodeToken(@RequestParam(name = "qrcode_token") String qrcode_token) {
        long uid = healthCodeService.extractUIDValidateQRCodeToken(qrcode_token);
        return Result.success(uid);
    }


}
