package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.HealthCodeManagerDto;
import org.software.code.dto.NucleicAcidTestPersonnelDto;
import org.software.code.dto.UserInfoDto;
import org.software.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInternalController {
    @Autowired
    private UserService userService;


    @GetMapping("/getUserByUID")
    public Result<?> getUserByUID(@RequestParam(name = "uid") long uid) {
        UserInfoDto userInfoDto = userService.getUserByUID(uid);
        return Result.success(userInfoDto);
    }

    @GetMapping("/getUserByID")
    public Result<?> getUserByID(@RequestParam(name = "identity_card") String identity_card) {
        UserInfoDto userInfoDto = userService.getUserByID(identity_card);
        return Result.success(userInfoDto);
    }

    @PostMapping("/userLogin")
    public Result<?> userLogin(@RequestParam(name = "code") String code) {
        String token = userService.userLogin(code);
        return Result.success(token);
    }

    @PostMapping("/nucleicAcidTestUserLogin")
    public Result<?> nucleicAcidTestUserLogin(@RequestParam(name = "identity_card") String identity_card,
                                              @RequestParam(name = "password") String password) {
        String token = userService.nucleicAcidTestUserLogin(identity_card, password);
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
    public Result<?> newNucleicAcidTestUser(@RequestParam(name = "identity_card") String identity_card,
                                            @RequestParam(name = "password") String password,
                                            @RequestParam(name = "name") String name) {
        userService.newNucleicAcidTestUser(identity_card, password, name);
        return Result.success("成功");
    }

    @PostMapping("/newMangerUser")
    public Result<?> newMangerUser(@RequestParam(name = "identity_card") String identity_card,
                                   @RequestParam(name = "password") String password,
                                   @RequestParam(name = "name") String name) {
        userService.newMangerUser(identity_card, password, name);
        return Result.success("成功");
    }

    @PostMapping("/managerUserLogin")
    public Result<?> managerLogin(@RequestParam(name = "identity_card") String identity_card,
                                  @RequestParam(name = "password") String password) {
        String token = userService.managerLogin(identity_card, password);
        return Result.success(token);
    }

    @PutMapping("/modifyUserInfo")
    public Result<?> modifyUserInfo(@RequestParam(name = "uid") long uid,
                                    @RequestParam(name = "name") String name,
                                    @RequestParam(name = "phone_number") String phone_number,
                                    @RequestParam(name = "identity_card") String identity_card,
                                    @RequestParam(name = "district") int district,
                                    @RequestParam(name = "street") int street,
                                    @RequestParam(name = "community") int community,
                                    @RequestParam(name = "address") String address) {
        userService.modifyUserInfo(uid, name, phone_number, identity_card, district, street, community, address);
        return Result.success("成功");
    }

    @PatchMapping("/statusNucleicAcidTestUser")
    public Result<?> statusNucleicAcidTestUser(@RequestParam(name = "tid") long tid,
                                               @RequestParam(name = "status") boolean status) {
        userService.statusNucleicAcidTestUser(tid, status);
        return Result.success("成功");
    }

    @PatchMapping("/statusManager")
    public Result<?> statusManager(@RequestParam(name = "mid") long mid,
                                   @RequestParam(name = "status") boolean status) {
        userService.statusManager(mid, status);
        return Result.success("成功");
    }

    @PostMapping("/extractUidValidateToken")
    public Result<?> extractUidValidateToken(@RequestHeader("Authorization") String token) {
        long id = userService.extractUidValidateToken(token);
        return Result.success(id);
    }

    @PostMapping("/extractTidValidateToken")
    public Result<?> extractTidValidateToken(@RequestHeader("Authorization") String token) {
        long id = userService.extractTidValidateToken(token);
        return Result.success(id);
    }

    @PostMapping("/extractMidValidateToken")
    public Result<?> extractMidValidateToken(@RequestHeader("Authorization") String token) {
        long id = userService.extractMidValidateToken(token);
        return Result.success(id);
    }

    @PutMapping("/addUserInfo")
    public Result<?> addUserInfo(@RequestParam(name = "uid") long uid,
                                 @RequestParam(name = "name") String name,
                                 @RequestParam(name = "phone_number") String phone_number,
                                 @RequestParam(name = "identity_card") String identity_card,
                                 @RequestParam(name = "district") int district,
                                 @RequestParam(name = "street") int street,
                                 @RequestParam(name = "community") int community,
                                 @RequestParam(name = "address") String address) {
        userService.addUserInfo(uid, name, phone_number, identity_card, district, street, community, address);
        return Result.success("成功");
    }

}
