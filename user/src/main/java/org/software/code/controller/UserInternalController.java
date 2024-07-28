package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.*;
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
    public Result<?> getUserByID(@RequestParam(name = "identity_card") @NotNull(message = "identity_card不能为空") String identity_card) {
        UserInfoDto userInfoDto = userService.getUserByID(identity_card);
        return Result.success(userInfoDto);
    }

    @PostMapping("/userLogin")
    public Result<?> userLogin(@Valid @RequestBody CodeInput input) {
        String token = userService.userLogin(input.getCode());
        return Result.success(token);
    }

    @PostMapping("/nucleicAcidTestUserLogin")
    public Result<?> nucleicAcidTestUserLogin(@RequestBody NucleicAcidsLoginRequest request) {

        String identityCard = request.getIdentityCard();
        String password = request.getPassword();

        String token = userService.nucleicAcidTestUserLogin(identityCard, password);
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

        String identityCard = request.getIdentityCard();
        String name = request.getName();
        String password = request.getPassword();
        userService.newNucleicAcidTestUser(identityCard, password, name);
        return Result.success();
    }

    @PostMapping("/newMangerUser")
    public Result<?> newMangerUser(@Valid @RequestBody CreateManageRequest request) {

        String identityCard = request.getIdentityCard();
        String name = request.getName();
        String password = request.getPassword();
        userService.newMangerUser(identityCard, password, name);
        return Result.success();
    }

    @PostMapping("/managerUserLogin")
    public Result<?> managerLogin(@Valid @RequestBody ManagerLoginRequest request) {

        String identityCard = request.getIdentityCard();
        String password = request.getPassword();
        String token = userService.managerLogin(identityCard, password);
        return Result.success(token);
    }

    @PutMapping("/modifyUserInfo")
    public Result<?> modifyUserInfo(@Valid @RequestBody UserInfoRequest request) {

        Long uid = request.getUid();
        String name = request.getName();
        String phoneNumber = request.getPhoneNumber();
        String identityCard = request.getIdentityCard();
        Integer district = request.getDistrict();
        Integer street = request.getStreet();
        Long community = request.getCommunity();
        String address = request.getAddress();
        userService.modifyUserInfo(uid, name, phoneNumber, identityCard, district, street, community, address);
        return Result.success();
    }

    @PatchMapping("/statusNucleicAcidTestUser")
    public Result<?> statusNucleicAcidTestUser(@Valid @RequestBody StatusNucleicAcidTestUserRequest request) {

        Long tid = request.getTid();
        Boolean status = request.getStatus();
        userService.statusNucleicAcidTestUser(tid, status);
        return Result.success();
    }

    @PatchMapping("/statusManager")
    public Result<?> statusManager(@Valid @RequestBody StatusManagerRequest request) {

        Long mid = request.getMid();
        Boolean status = request.getStatus();
        userService.statusManager(mid, status);
        return Result.success();
    }

    @PutMapping("/addUserInfo")
    public Result<?> addUserInfo(@Valid @RequestBody UserInfoRequest request) {

        Long uid = request.getUid();
        String name = request.getName();
        String phoneNumber = request.getPhoneNumber();
        String identityCard = request.getIdentityCard();
        Integer district = request.getDistrict();
        Integer street = request.getStreet();
        Long community = request.getCommunity();
        String address = request.getAddress();
        userService.addUserInfo(uid, name, phoneNumber, identityCard, district, street, community, address);
        return Result.success();
    }

    @GetMapping("/delete")
    public Result<?> deleteUserInfo(@RequestParam("uid") @NotNull(message = "uid不能为空") Long uid) {
        userService.deleteUserInfo(uid);
        return Result.success();
    }

    @PostMapping("/testuid")
    public Result<?> testuid(@RequestBody @Valid TidInput tidInpt) {
        System.out.println(tidInpt.getTid());
//        userService.deleteUserInfo(uid);
        return Result.success();
    }


}
