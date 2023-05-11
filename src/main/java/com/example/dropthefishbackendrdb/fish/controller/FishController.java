package com.example.dropthefishbackendrdb.fish.controller;

import com.example.dropthefishbackendrdb.fish.dto.FishDetailResponseDto;
import com.example.dropthefishbackendrdb.fish.dto.FishPriceResponseDto;
import com.example.dropthefishbackendrdb.fish.service.FishPriceService;
import com.example.dropthefishbackendrdb.fish.service.FishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fish")
public class FishController {
    private final FishService fishService;
    private final FishPriceService fishPriceService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public FishDetailResponseDto getFishDetail(@RequestParam("fishName") String fishName) {
        return fishService.findFishDetailByName(fishName);
    }


    @GetMapping("/price")
    @ResponseStatus(value = HttpStatus.OK)
    public FishPriceResponseDto getTodayPrice() {
        return FishPriceResponseDto.from(fishPriceService.getTodayPrice());
    }
}
