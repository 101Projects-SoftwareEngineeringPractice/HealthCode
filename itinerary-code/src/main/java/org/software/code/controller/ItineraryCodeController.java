package org.software.code.controller;

import org.software.code.common.utils.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.model.dto.GetItineraryDto;
import org.software.code.service.ItineraryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/itinerary-code")
public class ItineraryCodeController {
    @Autowired
    private ItineraryCodeService itineraryCodeService;
    @GetMapping("/getItinerary")
    public Result<?> getItinerary (@RequestHeader("Authorization") @NotNull(message = "token不能为空") String token) {
        long uid=JWTUtil.extractID(token);
        GetItineraryDto getItineraryDto = itineraryCodeService.getItinerary(uid);
        return Result.success(getItineraryDto);
    }
}
