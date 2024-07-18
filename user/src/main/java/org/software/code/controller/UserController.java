package org.software.code.controller;

import org.software.code.common.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.dto.HealthCodeManagerDto;
import org.software.code.dto.NucleicAcidTestPersonnelDto;
import org.software.code.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    private JWTUtil jwtUtil = new JWTUtil();


    @PostMapping("/login")
    public Result<?> login(@RequestParam(name = "code") String code) {
        String token = userService.userLogin(code);
        return Result.success(token);
    }

    @PutMapping("/userModify")
    public Result<?> userModify(@RequestHeader("Authorization") String token,
                                @RequestParam(name = "name") String name,
                                @RequestParam(name = "phone_number") String phone_number,
                                @RequestParam(name = "district_id") int district_id,
                                @RequestParam(name = "street_id") int street_id,
                                @RequestParam(name = "community_id") int community_id,
                                @RequestParam(name = "address") String address) {
        long uid = jwtUtil.extractID(token);
        userService.userModify(uid, name, phone_number, district_id, street_id, community_id, address);
        return Result.success();
    }

    @PostMapping("/nucleicAcidsLogin")
    public Result<?> nucleicAcidsLogin(@RequestParam(name = "identity_card") String identity_card,
                                       @RequestParam(name = "password") String password) {
        String token = userService.nucleicAcidTestUserLogin(identity_card, password);
        return Result.success(token);
    }

    @PostMapping("/managerLogin")
    public Result<?> managerLogin(@RequestParam(name = "identity_card") String identity_card,
                                  @RequestParam(name = "password") String password) {
        String token = userService.managerLogin(identity_card, password);
        return Result.success(token);
    }

    @PostMapping("/nucleic_acid")
    public Result<?> createNucleicAcid(@RequestHeader("Authorization") String token,
                                       @RequestParam(name = "identity_card") String identity_card,
                                       @RequestParam(name = "name") String name,
                                       @RequestParam(name = "password") String password) {
        jwtUtil.extractID(token);
        userService.newNucleicAcidTestUser(identity_card, name, password);
        return Result.success();
    }

    @GetMapping("/nucleic_acid")
    public Result<?> getNucleicAcidList(@RequestHeader("Authorization") String token) {
        jwtUtil.extractID(token);
        List<NucleicAcidTestPersonnelDto> nucleicAcidUserInfoList = userService.getNucleicAcidTestUser();
        return Result.success(nucleicAcidUserInfoList);
    }

    @PatchMapping("/nucleic_acid_opposite")
    public Result<?> nucleicAcidOpposite(@RequestHeader("Authorization") String token,
                                         @RequestParam(name = "tid") long tid) {
        jwtUtil.extractID(token);
        userService.nucleicAcidOpposite(tid);
        return Result.success();
    }

    @PostMapping("/manage")
    public Result<?> createManage(@RequestHeader("Authorization") String token,
                                  @RequestParam(name = "identity_card") String identity_card,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "password") String password) {
        jwtUtil.extractID(token);
        userService.newMangerUser(identity_card, name, password);
        return Result.success(token);
    }

    @GetMapping("/manager")
    public Result<?> getManageList(@RequestHeader("Authorization") String token) {
        jwtUtil.extractID(token);
        List<HealthCodeManagerDto> manageUserInfoList = userService.getManagerUser();
        return Result.success(manageUserInfoList);
    }

    @PatchMapping("/manage_opposite")
    public Result<?> manageOpposite(@RequestHeader("Authorization") String token,
                                    @RequestParam(name = "mid") long mid) {
        jwtUtil.extractID(token);
        userService.manageOpposite(mid);
        return Result.success();
    }
}
