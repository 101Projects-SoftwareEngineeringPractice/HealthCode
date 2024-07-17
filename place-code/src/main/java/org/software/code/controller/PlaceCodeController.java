package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.PlaceCodeInfoDto;
import org.software.code.service.PlaceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaceCodeController {
    @Autowired
    private PlaceCodeService placeCodeService;

    @PostMapping("/place-code/scanCode")
    public Result<?> scanCode(@RequestHeader("Authorization") String id_token,
                              @RequestParam(name = "token") String token) {
        long uid=placeCodeService.extractUidValidateToken(id_token);
        placeCodeService.scanPlaceCode(uid,token);
        return Result.success("成功");
    }

    /**
     * 用户登录管理系统后，新建场所码。
     * @param token
     * @param identity_card
     * @param name
     * @param district_id
     * @param street_id
     * @param community_id
     * @param address
     * @return
     */
    @PostMapping("/place-code/placeCode")
    public Result<?> createPlaceCode(@RequestHeader("Authorization") String token,
                                     @RequestParam(name = "identity_card") String identity_card,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "district_id") int district_id,
                               @RequestParam(name = "street_id") int street_id,
                               @RequestParam(name = "community_id") int community_id,
                               @RequestParam(name = "address") String address) {
        System.out.println("token = " + token + ", identity_card = " + identity_card + ", name = " + name + ", district_id = " + district_id + ", street_id = " + street_id + ", community_id = " + community_id + ", address = " + address);
        placeCodeService.extractMidValidateToken(token);
        placeCodeService.createPlaceCode(identity_card,name,district_id,street_id,community_id,address);
        return Result.success("成功");
    }

    /**
     * 用户登录管理系统后，获取所有场所码。
     * @param token
     * @return
     */
    @GetMapping("/place-code/placeCode")
    public Result<?> getPlaceCodeList(@RequestHeader("Authorization") String token) {
        placeCodeService.extractMidValidateToken(token);
        List<PlaceCodeInfoDto> placeInfoList =placeCodeService.getPlaceInfoList();
        return Result.success(placeInfoList);
    }
    @PatchMapping("/place-code/place_code_opposite")
    public Result<?> placeCodeOpposite(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "pid") Long pid) {
        long mid=placeCodeService.extractMidValidateToken(token);
        placeCodeService.placeCodeOpposite(pid);
        return Result.success("成功");
    }

}
