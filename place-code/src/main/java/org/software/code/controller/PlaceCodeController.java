package org.software.code.controller;

import org.software.code.common.utils.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.model.input.PidTokenInput;
import org.software.code.model.input.CreatePlaceCodeRequest;
import org.software.code.model.input.PidInput;
import org.software.code.model.dto.PlaceCodeInfoDto;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
@Validated
@RestController
@RequestMapping("/place-code")
public class PlaceCodeController {
    @Autowired
    private PlaceCodeService placeCodeService;

    @PostMapping("/scanCodeByToken")
    public Result<?> scanCodeByToken(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                              @Valid @RequestBody PidTokenInput request) {
        long pid = JWTUtil.extractID(request.getToken());
        long uid = JWTUtil.extractID(token);
        placeCodeService.scanPlaceCode(uid, pid);
        return Result.success();
    }
    @PostMapping("/scanCode")
    public Result<?> scanCode(@RequestHeader("authorization") @NotNull(message = "token不能为空") String token,
                              @Valid @RequestBody PidInput request) {
        long pid = request.getPid();
        long uid = JWTUtil.extractID(token);
        placeCodeService.scanPlaceCode(uid, pid);
        return Result.success();
    }

    /**
     * placeCode
     * 用户登录管理系统后，新建场所码。
     */
    @PostMapping("/placeCode")
    public Result<?> createPlaceCode(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                     @Valid @RequestBody CreatePlaceCodeRequest request) {

        String identityCard = request.getIdentity_card();
        String name = request.getName();
        int districtId = request.getDistrict_id();
        int streetId = request.getStreet_id();
        long communityId = request.getCommunity_id();
        String address = request.getAddress();
        JWTUtil.extractID(token);
        placeCodeService.createPlaceCode(identityCard, name, districtId, streetId, communityId, address);
        return Result.success();

    }


    /**
     * placeCode
     * 用户登录管理系统后，获取所有场所码。
     */
    @GetMapping("/placeCode")
    public Result<?> getPlaceCodeList(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {

        JWTUtil.extractID(token);
        List<PlaceCodeInfoDto> placeInfoList = placeCodeService.getPlaceInfoList();
        return Result.success(placeInfoList);

    }

    @PatchMapping("/place_code_opposite")
    public Result<?> placeCodeOpposite(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                       @Valid @RequestBody PidInput request) {
        JWTUtil.extractID(token);
        placeCodeService.placeCodeOpposite(request.getPid());
        return Result.success();
    }
}
