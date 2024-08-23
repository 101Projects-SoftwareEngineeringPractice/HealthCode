package org.software.code.controller;

import org.software.code.common.utils.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.model.dto.*;
import org.software.code.model.input.*;
import org.software.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequestMapping("user")
public class UserInternalController {
    @Autowired
    private UserService userService;


    @GetMapping("/getUserByUID")
    public Result<?> getUserByUID(@RequestParam(name = "uid") @NotNull(message = "uid不能为空") Long uid) {
        UserInfoDto userInfoDto = userService.getUserByUID(uid);
        return Result.success(userInfoDto);
    }

    @GetMapping("/getUserByID")
    public Result<?> getUserByID(@RequestParam(name = "identity_card") @NotNull(message = "identity_card不能为空") String identityCard) {
        UserInfoDto userInfoDto = userService.getUserByID(identityCard);
        return Result.success(userInfoDto);
    }

    @PostMapping("/userLogin")
    public Result<?> userLogin(@Valid @RequestBody CodeRequest input) {
        String token = userService.userLogin(input.getCode());
        return Result.success(token);
    }

    @PostMapping("/nucleicAcidTestUserLogin")
    public Result<?> nucleicAcidTestUserLogin(@RequestBody NucleicAcidsLoginRequest request) {
        String token = userService.nucleicAcidTestUserLogin(request);
        return Result.success(token);
    }

    @GetMapping("/getNucleicAcidTestUser")
    public Result<?> getNucleicAcidTestUser() {
        List<NucleicAcidTestPersonnelDto> nucleicAcidUserInfoList = userService.getNucleicAcidTestUser();
        return Result.success(nucleicAcidUserInfoList);
    }

    @GetMapping("/getManagerUser")
    public Result<?> getManagerUser() {
        List<HealthCodeManagerDto> manageUserInfoList = userService.getManagerUser();
        return Result.success(manageUserInfoList);
    }

    @PostMapping("/newNucleicAcidTestUser")
    public Result<?> newNucleicAcidTestUser(@Valid @RequestBody CreateNucleicAcidRequest request) {
        userService.newNucleicAcidTestUser(request);
        return Result.success();
    }

    @PostMapping("/newMangerUser")
    public Result<?> newMangerUser(@Valid @RequestBody CreateManageRequest request) {
        userService.newMangerUser(request);
        return Result.success();
    }

    @PostMapping("/managerUserLogin")
    public Result<?> managerLogin(@Valid @RequestBody ManagerLoginRequest request) {
        String token = userService.managerLogin(request);
        return Result.success(token);
    }

    @PutMapping("/modifyUserInfo")
    public Result<?> modifyUserInfo(@Valid @RequestBody UserInfoRequest request) {
        userService.modifyUserInfo(request);
        return Result.success();
    }

    @PatchMapping("/statusNucleicAcidTestUser")
    public Result<?> statusNucleicAcidTestUser(@Valid @RequestBody StatusNucleicAcidTestUserRequest request) {
        userService.statusNucleicAcidTestUser(request);
        return Result.success();
    }

    @PatchMapping("/statusManager")
    public Result<?> statusManager(@Valid @RequestBody StatusManagerRequest request) {
        userService.statusManager(request);
        return Result.success();
    }

    @PutMapping("/addUserInfo")
    public Result<?> addUserInfo(@Valid @RequestBody UserInfoRequest request) {
        userService.addUserInfo(request);
        return Result.success();
    }

    @GetMapping("/delete")
    public Result<?> deleteUserInfo(@RequestParam("uid") @NotNull(message = "uid不能为空") Long uid) {
        userService.deleteUserInfo(uid);
        return Result.success();
    }

    @PostMapping("/testuid")
    public Result<?> testuid(@RequestBody @Valid TidRequest tidInpt) {
        System.out.println(tidInpt.getTid());
        return Result.success();
    }

    @GetMapping("/getuid")
    public Result<?> createManage(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        return Result.success(JWTUtil.extractID(token));
    }
}
