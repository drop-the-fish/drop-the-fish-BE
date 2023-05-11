package com.example.dropthefishbackendrdb.fish.service;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishPair;
import com.example.dropthefishbackendrdb.fish.dto.FishPairDto;
import com.example.dropthefishbackendrdb.fish.dto.FishPriceDto;
import com.example.dropthefishbackendrdb.fish.dto.SeasonFishListResponseDto;
import com.example.dropthefishbackendrdb.fish.repository.FishPairRepository;
import com.example.dropthefishbackendrdb.fish.repository.FishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishRecommendationService {
    private final FishService fishService;
    private final FishRepository fishRepository;

    private final FishPairRepository fishPairRepository;

    public List<FishPriceDto> sortCheapMonthlyFish(List<FishPriceDto> fishPriceDtoList) {
        fishPriceDtoList.sort((a, b) -> calcMontlyPriceDiff(a) - calcMontlyPriceDiff(b));

        return fishPriceDtoList.subList(0, 5);
    }

    public List<FishPriceDto> sortCheapYearlyFish(List<FishPriceDto> fishPriceDtoList) {
        fishPriceDtoList.sort((a, b) -> calcYearlyPriceDiff(a) - calcYearlyPriceDiff(b));

        return fishPriceDtoList.subList(0, 5);
    }

    private int calcMontlyPriceDiff(FishPriceDto fishPriceDto) {
        int monthAverage = fishPriceDto.getPriceList().get(4);
        int todayPrice = fishPriceDto.getPriceList().get(0);

        if(todayPrice == 0) {
            return Integer.MAX_VALUE;
        }

        return todayPrice - monthAverage;
    }

    private int calcYearlyPriceDiff(FishPriceDto fishPriceDto) {
        int monthAverage = fishPriceDto.getPriceList().get(5);
        int todayPrice = fishPriceDto.getPriceList().get(0);

        if(todayPrice == 0) {
            return Integer.MAX_VALUE;
        }

        return todayPrice - monthAverage;
    }

    public SeasonFishListResponseDto recommendSeasonFish() {
        List<Fish> fishList = fishRepository.findAll();
        List<Fish> seasonFishList = fishList.stream().filter(fishService::isSeasonFish).toList();

        return SeasonFishListResponseDto.from(seasonFishList);
    }

    public List<FishPairDto> recommendFishPairs(List<FishPriceDto> fishPriceDtoList) {
        fishPriceDtoList = deleteDuplicateName(fishPriceDtoList);
        List<FishPairDto> fishPairDtoList = addFishPairs(fishPriceDtoList);

        return fishPairDtoList.stream().filter(fishPairDto ->
            fishPairDto.getPairList().size() != 0
        ).toList();
    }

    private static List<FishPriceDto> deleteDuplicateName(List<FishPriceDto> fishPriceDtoList) {
        fishPriceDtoList = fishPriceDtoList.stream().collect(
                Collectors.toMap(FishPriceDto::getItemName, p -> p, (p, q) -> p))
                .values().stream().toList();
        return fishPriceDtoList;
    }

    private List<FishPairDto> addFishPairs(List<FishPriceDto> fishPriceDtoList) {
        return fishPriceDtoList.stream().map(fishPriceDto -> {
            String fishName = parseFishName(fishPriceDto.getItemName());
            Optional<Fish> findFish = fishRepository.findByName(fishName);
            if (findFish.isEmpty()) {
                return FishPairDto.from(fishName, new ArrayList<>());
            }

            List<FishPair> fishPairList = fishPairRepository.findAllByFish(findFish.get());
            return FishPairDto.from(fishName, fishPairList.stream().map(FishPair::getPair).toList());
        }).toList();
    }

    private String parseFishName(String kindName) {
        return kindName.split("\\(")[0];
    }
}
