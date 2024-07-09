package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.GetItineraryDto;
import org.software.code.dto.PlaceStarDto;
import org.software.code.service.ItineraryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ItineraryCodeController {
    @Autowired
    private ItineraryCodeService itineraryCodeService;

    @GetMapping("/itinerary-code/getItinerary")
    public Result<?> getItinerary (@RequestHeader("Authorization") String token) {
        long uid=itineraryCodeService.extractUidValidateToken(token);
        GetItineraryDto getItineraryDto = itineraryCodeService.getItinerary(uid);
        return Result.success(getItineraryDto);
    }

}
