package org.software.code.controller;

import org.software.code.common.JWTUtil;
import org.software.code.common.result.Result;
import org.software.code.dto.GetItineraryDto;
import org.software.code.service.ItineraryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/itinerary-code")
public class ItineraryCodeController {
    @Autowired
    private ItineraryCodeService itineraryCodeService;
    private JWTUtil jwtUtil =new JWTUtil();

    @GetMapping("/getItinerary")
    public Result<?> getItinerary (@RequestHeader("Authorization") String token) {
        long uid=jwtUtil.extractID(token);
        GetItineraryDto getItineraryDto = itineraryCodeService.getItinerary(uid);
        return Result.success(getItineraryDto);
    }

}
