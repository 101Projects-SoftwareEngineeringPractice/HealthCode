package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test/others")
    public String testOthers() {
        return userService.testOthers();
    }

    @GetMapping("/test/hello")
    @ResponseBody
    public Result<?> hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return Result.success("Hello " + name);
    }

    @GetMapping("/test/get")
    @ResponseBody
    public Result<?> testGet() {
        return Result.success(userService.getInfo(1L).getName());
    }

    @PutMapping("/test/put")
    @ResponseBody
    public Result<?> testPut(@RequestParam(name = "uid") long uid, @RequestParam(name = "district") int district) {
        userService.updateInfo(uid, district);
        return Result.success("成功修改");
    }
}
