package org.software.code.controller;


import org.software.code.common.utils.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.model.dto.*;
import org.software.code.model.input.*;
import org.software.code.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;


@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody CodeRequest input) {

        String token = userService.userLogin(input.getCode());
        return Result.success(token);
    }

    @PostMapping("/login-test")
    public Result<?> login_test(@Valid @RequestBody CodeRequest input) {
        String token = userService.userLoginTest(input.getCode());
        return Result.success(token);
    }


    @PutMapping("/userModify")
    public Result<?> userModify(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                @Valid @RequestBody UserModifyRequest request) {
        long uid = JWTUtil.extractID(token);
        UserModifyInput input=new UserModifyInput();
        BeanUtils.copyProperties(request,input);
        input.setUid(uid);
        userService.userModify(input);
        return Result.success();
    }


    @PostMapping("/nucleicAcidsLogin")
    public Result<?> nucleicAcidsLogin(@Valid @RequestBody NucleicAcidsLoginRequest request) {
        String token = userService.nucleicAcidTestUserLogin(request);
        return Result.success(token);
    }

    @PostMapping("/managerLogin")
    public Result<?> managerLogin(@Valid @RequestBody ManagerLoginRequest request) {
        String token = userService.managerLogin(request);
        return Result.success(token);
    }

    @PostMapping("/nucleic_acid")
    public Result<?> createNucleicAcid(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                       @Valid @RequestBody CreateNucleicAcidRequest request) {
        JWTUtil.extractID(token);
        userService.newNucleicAcidTestUser(request);
        return Result.success();

    }


    @GetMapping("/nucleic_acid")
    public Result<?> getNucleicAcidList(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        JWTUtil.extractID(token);
        List<NucleicAcidTestPersonnelDto> nucleicAcidUserInfoList = userService.getNucleicAcidTestUser();
        return Result.success(nucleicAcidUserInfoList);

    }

    @PatchMapping("/nucleic_acid_opposite")
    public Result<?> nucleicAcidOpposite(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                         @Valid @RequestBody TidRequest request) {

        JWTUtil.extractID(token);
        userService.nucleicAcidOpposite(request.getTid());
        return Result.success();

    }

    @PostMapping("/manage")
    public Result<?> createManage(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                  @Valid @RequestBody CreateManageRequest request) {
        JWTUtil.extractID(token);
        userService.newMangerUser(request);
        return Result.success(token);

    }


    @GetMapping("/manager")
    public Result<?> getManageList(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        JWTUtil.extractID(token);
        List<HealthCodeManagerDto> manageUserInfoList = userService.getManagerUser();
        return Result.success(manageUserInfoList);

    }

    @PatchMapping("/manage_opposite")
    public Result<?> manageOpposite(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                    @Valid @RequestBody MidRequest request) {
        JWTUtil.extractID(token);
        userService.manageOpposite(request.getMid());
        return Result.success();

    }

}
