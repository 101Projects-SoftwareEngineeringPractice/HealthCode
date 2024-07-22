package org.software.code.controller;

import org.software.code.common.result.Result;
import org.software.code.dto.PlaceStatusDto;
import org.software.code.service.ItineraryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itinerary-code")
public class ItineraryCodeInternalController {
    @Autowired
    ItineraryCodeService itineraryCodeService;

    /**
     * 获取用户近14天行程, 得到用户14天内途径的城市，并读取本地风险json文件获取当日风险地区的城市id,有风险的城市则状态设置为1。用户行程如果是国外则通过province小于0判断，此情况默认status返回1
     *
     * @param uid
     * @return
     */
    @GetMapping("/getItineraryCodeList")
    public Result<?> getItineraryCodeList(@RequestParam(name = "uid") long uid) {
        try{
        List<PlaceStatusDto> placeStatusDtoList = itineraryCodeService.getItineraryCodeList(uid);
        return Result.success(placeStatusDtoList);
    } catch (Exception e) {
        return Result.failed(e.getMessage());
    }
    }

    /**
     * 清理15天以前的用户行程
     * @return
     */
    @GetMapping("/cleanItinerary")
    public Result<?> cleanItinerary() {
        try{
        itineraryCodeService.cleanItinerary();
        return Result.success();
    } catch (Exception e) {
        return Result.failed(e.getMessage());
        }
    }
}
