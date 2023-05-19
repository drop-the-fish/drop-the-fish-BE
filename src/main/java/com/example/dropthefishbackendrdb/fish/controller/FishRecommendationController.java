package com.example.dropthefishbackendrdb.fish.controller;

import com.example.dropthefishbackendrdb.fish.dto.FishPairDto;
import com.example.dropthefishbackendrdb.fish.dto.FishPriceDto;
import com.example.dropthefishbackendrdb.fish.dto.SeasonFishListResponseDto;
import com.example.dropthefishbackendrdb.fish.service.FishPriceService;
import com.example.dropthefishbackendrdb.fish.service.FishRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class FishRecommendationController {
    private final FishPriceService fishPriceService;
    private final FishRecommendationService fishRecommendationService;

    @GetMapping("/price/month")
    @ResponseStatus(HttpStatus.OK)
    public List<FishPriceDto> recommendCheapMonthlyFish() {
        List<FishPriceDto> todayPriceList = fishPriceService.getTodayPrice(new RestTemplate());

        return fishRecommendationService.sortCheapMonthlyFish(todayPriceList);
    }

    @GetMapping("/price/year")
    @ResponseStatus(HttpStatus.OK)
    public List<FishPriceDto> recommendCheapYearlyFish() {
        List<FishPriceDto> todayPriceList = fishPriceService.getTodayPrice(new RestTemplate());

        return fishRecommendationService.sortCheapYearlyFish(todayPriceList);
    }

    @GetMapping("/season")
    @ResponseStatus(HttpStatus.OK)
    public SeasonFishListResponseDto recommendSeasonFish() {
        return fishRecommendationService.recommendSeasonFish();
    }

    @GetMapping("/pair")
    @ResponseStatus(HttpStatus.OK)
    public List<FishPairDto> recommendFishPairs() {
        List<FishPriceDto> todayPriceList = fishPriceService.getTodayPrice(new RestTemplate());

        return fishRecommendationService.recommendFishPairs(todayPriceList);
    }


}
