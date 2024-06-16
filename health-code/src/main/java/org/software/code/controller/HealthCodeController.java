package org.software.code.controller;

import org.software.code.service.HealthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-code")
public class HealthCodeController {
    @Autowired
    private HealthCodeService healthCodeService;

    @GetMapping("/{code}")
    public String getHealthCodeInfo(@PathVariable String code) {
        return healthCodeService.getHealthCodeInfo(code);
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name;
    }
}
