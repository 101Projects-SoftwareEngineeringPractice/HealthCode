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
        try {
            UserInfoDto userInfoDto = userService.getUserByUID(uid);
            return Result.success(userInfoDto);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getUserByID")
    public Result<?> getUserByID(@RequestParam(name = "identity_card") String identity_card) {
        try {
            UserInfoDto userInfoDto = userService.getUserByID(identity_card);
            return Result.success(userInfoDto);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/userLogin")
    public Result<?> userLogin(@RequestParam(name = "code") String code) {

        try {
            String token = userService.userLogin(code);
            return Result.success(token);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/nucleicAcidTestUserLogin")
    public Result<?> nucleicAcidTestUserLogin(@RequestParam(name = "identity_card") String identity_card,
                                              @RequestParam(name = "password") String password) {
        try {
            String token = userService.nucleicAcidTestUserLogin(identity_card, password);
            return Result.success(token);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getNucleicAcidTestUser")
    public Result<?> getNucleicAcidTestUser() {
        try {
            List<NucleicAcidTestPersonnelDto> nucleicAcidUserInfoList = userService.getNucleicAcidTestUser();
            return Result.success(nucleicAcidUserInfoList);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @GetMapping("/getManagerUser")
    public Result<?> getManagerUser() {
        try {
            List<HealthCodeManagerDto> manageUserInfoList = userService.getManagerUser();
            return Result.success(manageUserInfoList);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/newNucleicAcidTestUser")
    public Result<?> newNucleicAcidTestUser(@RequestParam(name = "identity_card") String identity_card,
                                            @RequestParam(name = "password") String password,
                                            @RequestParam(name = "name") String name) {
        try {
            userService.newNucleicAcidTestUser(identity_card, password, name);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/newMangerUser")
    public Result<?> newMangerUser(@RequestParam(name = "identity_card") String identity_card,
                                   @RequestParam(name = "password") String password,
                                   @RequestParam(name = "name") String name) {
        try {
            userService.newMangerUser(identity_card, password, name);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PostMapping("/managerUserLogin")
    public Result<?> managerLogin(@RequestParam(name = "identity_card") String identity_card,
                                  @RequestParam(name = "password") String password) {
        try {
            String token = userService.managerLogin(identity_card, password);
            return Result.success(token);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
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
        try {
            userService.modifyUserInfo(uid, name, phone_number, identity_card, district, street, community, address);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PatchMapping("/statusNucleicAcidTestUser")
    public Result<?> statusNucleicAcidTestUser(@RequestParam(name = "tid") long tid,
                                               @RequestParam(name = "status") boolean status) {
        try {
            userService.statusNucleicAcidTestUser(tid, status);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

    @PatchMapping("/statusManager")
    public Result<?> statusManager(@RequestParam(name = "mid") long mid,
                                   @RequestParam(name = "status") boolean status) {
        try {
            userService.statusManager(mid, status);
            return Result.success();
        } catch (
                Exception e) {
            return Result.failed(e.getMessage());
        }
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
        try {
            userService.addUserInfo(uid, name, phone_number, identity_card, district, street, community, address);
            return Result.success();
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
    }

}
