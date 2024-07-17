package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.HealthCodeManagerDto;
import org.software.code.dto.NucleicAcidTestPersonnelDto;
import org.software.code.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController

public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/user/login")
    public Result<?> login(@RequestParam(name = "code") String code) {
        String token = userService.userLogin(code);
        return Result.success(token);
    }

    @PutMapping("/user/userModify")
    public Result<?> userModify(@RequestHeader("Authorization") String token,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "phone_number") String phone_number,
                                @RequestParam(name = "district_id") int district_id,
                                @RequestParam(name = "street_id") int street_id,
                                @RequestParam(name = "community_id") int community_id,
                                @RequestParam(name = "address") String address) {
        long uid = userService.extractUidValidateToken(token);
        userService.userModify(uid, name, phone_number, district_id, street_id, community_id, address);
        return Result.success("成功");
    }

    @PostMapping("/user/nucleicAcidsLogin")
    public Result<?> nucleicAcidsLogin(@RequestParam(name = "identity_card") String identity_card,
                                       @RequestParam(name = "password") String password) {
        String token = userService.nucleicAcidTestUserLogin(identity_card, password);
        return Result.success(token);
    }

    @PostMapping("/user/managerLogin")
    public Result<?> managerLogin(@RequestParam(name = "identity_card") String identity_card,
                                  @RequestParam(name = "password") String password) {
        String token = userService.managerLogin(identity_card, password);
        return Result.success(token);
    }

    @PostMapping("/user/nucleic_acid")
    public Result<?> createNucleicAcid(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "identity_card") String identity_card,
                                       @RequestParam(name = "name") String name,
                                       @RequestParam(name = "password") String password) {
        userService.extractMidValidateToken(token);
        userService.newNucleicAcidTestUser(identity_card, name, password);
        return Result.success("成功");
    }

    @GetMapping("/user/nucleic_acid")
    public Result<?> getNucleicAcidList(@RequestHeader("Authorization") String token) {
        userService.extractMidValidateToken(token);
        List<NucleicAcidTestPersonnelDto> nucleicAcidUserInfoList = userService.getNucleicAcidTestUser();
        return Result.success(nucleicAcidUserInfoList);
    }

    @PatchMapping("/user/nucleic_acid_opposite")
    public Result<?> nucleicAcidOpposite(@RequestHeader("Authorization") String token,
                                         @RequestParam(name = "tid") long tid) {
         userService.extractMidValidateToken(token);
        userService.nucleicAcidOpposite(tid);
        return Result.success("成功");
    }

    @PostMapping("/user/manage")
    public Result<?> createManage(@RequestHeader("Authorization") String token,
                                  @RequestParam(name = "identity_card") String identity_card,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "password") String password) {
        userService.extractMidValidateToken(token);
        userService.newMangerUser(identity_card, name, password);
        return Result.success(token);
    }

    @GetMapping("/user/manager")
    public Result<?> getManageList(@RequestHeader("Authorization") String token) {
        userService.extractMidValidateToken(token);
        List<HealthCodeManagerDto> manageUserInfoList = userService.getManagerUser();
        return Result.success(manageUserInfoList);
    }

    @PatchMapping("/user/manage_opposite")
    public Result<?> manageOpposite(@RequestHeader("Authorization") String token,
                                    @RequestParam(name = "mid") long mid) {
        userService.extractMidValidateToken(token);
        userService.manageOpposite(mid);
        return Result.success("成功");
    }
}
