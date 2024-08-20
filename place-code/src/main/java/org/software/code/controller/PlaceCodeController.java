package org.software.code.controller;

import org.software.code.common.utils.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.model.input.PidTokenRequest;
import org.software.code.model.input.CreatePlaceCodeRequest;
import org.software.code.model.input.PidRequest;
import org.software.code.model.input.ScanPlaceCodeInput;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/place-code")
public class PlaceCodeController {
    @Autowired
    private PlaceCodeService placeCodeService;

    @PostMapping("/scanCodeByToken")
    public Result<?> scanCodeByToken(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                              @Valid @RequestBody PidTokenRequest request) {
        long pid = JWTUtil.extractID(request.getToken());
        long uid = JWTUtil.extractID(token);
        ScanPlaceCodeInput input=new ScanPlaceCodeInput(uid,pid);
        placeCodeService.scanPlaceCode(input);
        return Result.success();
    }
    @PostMapping("/scanCode")
    public Result<?> scanCode(@RequestHeader("authorization") @NotNull(message = "token不能为空") String token,
                              @Valid @RequestBody PidRequest request) {
        long pid = request.getPid();
        long uid = JWTUtil.extractID(token);
        ScanPlaceCodeInput input=new ScanPlaceCodeInput(uid,pid);
        placeCodeService.scanPlaceCode(input);
        return Result.success();
    }

    /**
     * placeCode
     * 用户登录管理系统后，新建场所码。
     */
    @PostMapping("/placeCode")
    public Result<?> createPlaceCode(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                     @Valid @RequestBody CreatePlaceCodeRequest request) {
        JWTUtil.extractID(token);
        placeCodeService.createPlaceCode(request);
        return Result.success();
    }


    /**
     * placeCode
     * 用户登录管理系统后，获取所有场所码。
     */
    @GetMapping("/placeCode")
    public Result<?> getPlaceCodeList(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        JWTUtil.extractID(token);
        return Result.success(placeCodeService.getPlaceInfoList());
    }

    @PatchMapping("/place_code_opposite")
    public Result<?> placeCodeOpposite(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                       @Valid @RequestBody PidRequest request) {
        JWTUtil.extractID(token);
        placeCodeService.placeCodeOpposite(request.getPid());
        return Result.success();
    }
}
