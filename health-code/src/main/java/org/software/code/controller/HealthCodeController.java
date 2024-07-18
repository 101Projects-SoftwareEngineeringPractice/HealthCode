package org.software.code.controller;

import org.software.code.common.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.dto.GetCodeDto;
import org.software.code.dto.HealthCodeInfoDto;
import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-code")
public class HealthCodeController {
    @Autowired
    private HealthCodeService healthCodeService;
    private JWTUtil jwtUtil=new JWTUtil();
    /**
     * 用户在申请健康码页面（个人信息填写页面）申请健康码。
     *
     * @param token
     * @param name
     * @param phone_number
     * @param identity_card
     * @param district_id
     * @param street_id
     * @param community_id
     * @param address
     * @return
     */
    @PostMapping("/applyCode")
    public Result<?> applyCode(@RequestHeader("Authorization") String token,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "phone_number") String phone_number,
                               @RequestParam(name = "identity_card") String identity_card,
                               @RequestParam(name = "district_id") int district_id,
                               @RequestParam(name = "street_id") int street_id,
                               @RequestParam(name = "community_id") int community_id,
                               @RequestParam(name = "address") String address) {
        long uid = jwtUtil.extractID(token);
        healthCodeService.applyCode(uid, name, phone_number, identity_card, district_id, street_id, community_id, address);
        return Result.success();
    }

    /**
     * 用户登录后，在首页展示健康码状态，包括绿码、黄码和红码。每次返回首页都需要重新获取，每分钟自动刷新一次。
     *
     * @param token
     * @return
     */
    @GetMapping("/getCode")
    public Result<?> getCode(@RequestHeader("Authorization") String token) {
        long uid = jwtUtil.extractID(token);
        GetCodeDto getCodeDto = healthCodeService.getCode(uid);
        return Result.success(getCodeDto);
    }

    /**
     * 用户登录管理系统后，在转码管理页面选择通过身份证来对用户进行转码。
     *
     * @param token
     * @param identity_card
     * @return
     */
    @GetMapping("/health_code")
    public Result<?> getHealthCodeInfo(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "identity_card") String identity_card) {

        jwtUtil.extractID(token);
        HealthCodeInfoDto healthCodeInfoDto = healthCodeService.getHealthCodeInfo(identity_card);
        return Result.success(healthCodeInfoDto);
    }

    /**
     * 用户登录管理系统后，在转码管理页面选择通过身份证来对用户进行转码。
     *
     * @param token
     * @param uid
     * @param event
     * @return
     */
    @PostMapping("/transcodingEvents")
    public Result<?> transcodingEvents(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "uid") long uid,
                                       @RequestParam(name = "event") int event) {

        jwtUtil.extractID(token);
        healthCodeService.transcodingHealthCodeEvents(uid, event);
        return Result.success();
    }

}
