package org.software.code.controller;


import org.software.code.common.utils.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.model.dto.*;
import org.software.code.model.input.CreateManageRequest;
import org.software.code.model.input.CreateNucleicAcidRequest;
import org.software.code.model.input.NucleicAcidsLoginRequest;
import org.software.code.model.input.UserModifyRequest;
import org.software.code.service.UserService;
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
    public Result<?> login(@Valid @RequestBody CodeInput input) {

        String token = userService.userLogin(input.getCode());
        return Result.success(token);
    }

    @PostMapping("/login-test")
    public Result<?> login_test(@Valid @RequestBody CodeInput input) {
        String token = userService.userLogin_test(input.getCode());
        return Result.success(token);
    }


    @PutMapping("/userModify")
    public Result<?> userModify(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                @Valid @RequestBody UserModifyRequest request) {


        String name = request.getName();
        String phoneNumber = request.getPhoneNumber();
        int districtId = request.getDistrictId();
        int streetId = request.getStreetId();
        long communityId = request.getCommunityId();
        String address = request.getAddress();

        long uid = JWTUtil.extractID(token);
        userService.userModify(uid, name, phoneNumber, districtId, streetId, communityId, address);
        return Result.success();
    }


    @PostMapping("/nucleicAcidsLogin")
    public Result<?> nucleicAcidsLogin(@Valid @RequestBody NucleicAcidsLoginRequest request) {

        String identityCard = request.getIdentityCard();
        String password = request.getPassword();
        String token = userService.nucleicAcidTestUserLogin(identityCard, password);
        return Result.success(token);
    }

    @PostMapping("/managerLogin")
    public Result<?> managerLogin(@Valid @RequestBody ManagerLoginRequest request) {

        String identityCard = request.getIdentityCard();
        String password = request.getPassword();
        String token = userService.managerLogin(identityCard, password);
        return Result.success(token);

    }

    @PostMapping("/nucleic_acid")
    public Result<?> createNucleicAcid(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                       @Valid @RequestBody CreateNucleicAcidRequest request) {

        String identityCard = request.getIdentityCard();
        String name = request.getName();
        String password = request.getPassword();
        JWTUtil.extractID(token);
        userService.newNucleicAcidTestUser(identityCard, password, name);
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
                                         @Valid @RequestBody TidInput request) {

        JWTUtil.extractID(token);
        userService.nucleicAcidOpposite(request.getTid());
        return Result.success();

    }

    @PostMapping("/manage")
    public Result<?> createManage(@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token,
                                  @Valid @RequestBody CreateManageRequest request) {

        String identityCard = request.getIdentityCard();
        String name = request.getName();
        String password = request.getPassword();
        JWTUtil.extractID(token);
        userService.newMangerUser(identityCard, password,name);
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
                                    @Valid @RequestBody MidInput request) {

        JWTUtil.extractID(token);
        userService.manageOpposite(request.getMid());
        return Result.success();

    }

}
