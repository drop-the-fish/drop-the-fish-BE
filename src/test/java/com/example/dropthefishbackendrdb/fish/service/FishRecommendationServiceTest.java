package com.example.dropthefishbackendrdb.fish.service;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishPair;
import com.example.dropthefishbackendrdb.fish.dto.FishPairDto;
import com.example.dropthefishbackendrdb.fish.dto.FishPriceDto;
import com.example.dropthefishbackendrdb.fish.dto.SeasonFishListResponseDto;
import com.example.dropthefishbackendrdb.fish.repository.FishPairRepository;
import com.example.dropthefishbackendrdb.fish.repository.FishRepository;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FishRecommendationServiceTest {

    @Mock
    FishRepository fishRepository;
    @Mock
    FishPairRepository fishPairRepository;

    @Mock
    FishService fishService;

    private List<FishPriceDto> fishPriceDtoList;

    @InjectMocks
    FishRecommendationService fishRecommendationService;

    @BeforeEach
    void setUp() {
        fishPriceDtoList = new ArrayList<>();
        HashMap<String, String> fishMap = getMackerelMap();
        JSONObject fishJsonData = new JSONObject(fishMap);

        HashMap<String, String> salmonMap = getSalmonMap();
        JSONObject salmonJsonData = new JSONObject(salmonMap);

        HashMap<String, String> tunaMap = getTunaMap();
        JSONObject tunaJsonData = new JSONObject(tunaMap);

        fishPriceDtoList.add(FishPriceDto.from(fishJsonData));
        fishPriceDtoList.add(FishPriceDto.from(fishJsonData));
        fishPriceDtoList.add(FishPriceDto.from(salmonJsonData));
        fishPriceDtoList.add(FishPriceDto.from(salmonJsonData));
        fishPriceDtoList.add(FishPriceDto.from(tunaJsonData));
    }

    private static HashMap<String, String> getMackerelMap() {
        HashMap<String, String> fishMap = new HashMap<>();
        fishMap.put("item_name", "고등어");
        fishMap.put("unit", "1마리");
        fishMap.put("rank", "상품");
        fishMap.put("dpr1", "10000");
        fishMap.put("dpr2", "10000");
        fishMap.put("dpr3", "12000");
        fishMap.put("dpr4", "12000");
        fishMap.put("dpr5", "12000");
        fishMap.put("dpr6", "12000");
        fishMap.put("dpr7", "12000");
        return fishMap;
    }

    private static HashMap<String, String> getSalmonMap() {
        HashMap<String, String> fishMap = new HashMap<>();
        fishMap.put("item_name", "연어");
        fishMap.put("unit", "1마리");
        fishMap.put("rank", "상품");
        fishMap.put("dpr1", "10000");
        fishMap.put("dpr2", "10000");
        fishMap.put("dpr3", "11000");
        fishMap.put("dpr4", "11000");
        fishMap.put("dpr5", "11000");
        fishMap.put("dpr6", "11000");
        fishMap.put("dpr7", "11000");
        return fishMap;
    }

    private static HashMap<String, String> getTunaMap() {
        HashMap<String, String> fishMap = new HashMap<>();
        fishMap.put("item_name", "참치");
        fishMap.put("unit", "1마리");
        fishMap.put("rank", "상품");
        fishMap.put("dpr1", "0");
        fishMap.put("dpr2", "0");
        fishMap.put("dpr3", "0");
        fishMap.put("dpr4", "0");
        fishMap.put("dpr5", "0");
        fishMap.put("dpr6", "0");
        fishMap.put("dpr7", "0");
        return fishMap;
    }

    @Test
    @DisplayName("Success case for Sorting Monthly price Fish")
    void successSortCheapMonthlyFish() {
        List<FishPriceDto> sortedList = fishRecommendationService.sortCheapMonthlyFish(fishPriceDtoList);

        Assertions.assertThat(sortedList.size()).isEqualTo(5);

        int lastDiff = Integer.MIN_VALUE;
        for(FishPriceDto fishPriceDto : sortedList) {
            List<Integer> priceList = fishPriceDto.getPriceList();
            int monthDiff = priceList.get(0) - priceList.get(4);

            if (priceList.get(0) == 0) {
                monthDiff = Integer.MIN_VALUE;
            }

            Assertions.assertThat(monthDiff).isGreaterThanOrEqualTo(lastDiff);

            lastDiff = monthDiff;
        }
    }

    @Test
    @DisplayName("Success case for Sorting Monthly price Fish")
    void successSortCheapYearlyFish() {
        List<FishPriceDto> sortedList = fishRecommendationService.sortCheapYearlyFish(fishPriceDtoList);

        Assertions.assertThat(sortedList.size()).isEqualTo(5);

        int lastDiff = Integer.MIN_VALUE;
        for(FishPriceDto fishPriceDto : sortedList) {
            List<Integer> priceList = fishPriceDto.getPriceList();

            int monthDiff = priceList.get(0) - priceList.get(5);

            if (priceList.get(0) == 0) {
                monthDiff = Integer.MIN_VALUE;
            }

            Assertions.assertThat(monthDiff).isGreaterThanOrEqualTo(lastDiff);

            lastDiff = monthDiff;
        }
    }

    @Test
    @DisplayName("Success case for recommendation season fish")
    void successRecommendSeasonFish() {
        Fish salmon = Fish.of("salmon", "des", 8, 2, "tasty", "salmon.jpeg");
        Fish squid = Fish.of("squid", "des", 5, 7, "tasty", "squid.jpeg");

        List<Fish> fishList = new ArrayList<>();
        fishList.add(salmon);
        fishList.add(squid);
        int curMonth = Integer.parseInt(LocalDate.now().toString().split("-")[1]);

        BDDMockito.given(this.fishRepository.findAll()).willReturn(fishList);
        BDDMockito.given(this.fishService.isSeasonFish(salmon, curMonth)).willReturn(true);
        BDDMockito.given(this.fishService.isSeasonFish(squid, curMonth)).willReturn(false);

        SeasonFishListResponseDto seasonFishListResponseDto = fishRecommendationService.recommendSeasonFish();

        Assertions.assertThat(seasonFishListResponseDto.getSeasonFishResponseDtoList().size()).isEqualTo(1);
        Assertions.assertThat(seasonFishListResponseDto.getSeasonFishResponseDtoList().get(0).getName())
                .isEqualTo(salmon.getName());
    }

    @Test
    @DisplayName("Success case for recommendation fish pairs")
    void successRecommendFishPairs() {
        Fish salmon = Fish.of("연어", "des", 8, 2, "tasty", "salmon.jpeg");

        FishPair salmonFair1 = FishPair.of(salmon, "와인");
        FishPair salmonFair2 = FishPair.of(salmon, "치즈");

        List<FishPair> salmonPairList = new ArrayList<>();
        salmonPairList.add(salmonFair1);
        salmonPairList.add(salmonFair2);

        BDDMockito.given(fishRepository.findByName("고등어")).willReturn(Optional.empty());
        BDDMockito.given(fishRepository.findByName("참치")).willReturn(Optional.empty());
        BDDMockito.given(fishRepository.findByName("연어")).willReturn(Optional.of(salmon));
        BDDMockito.given(fishPairRepository.findAllByFish(salmon)).willReturn(salmonPairList);

        List<FishPairDto> fishPairDtoList = fishRecommendationService.recommendFishPairs(fishPriceDtoList);

        for(FishPairDto fishPairDto: fishPairDtoList) {
            if(fishPairDto.getFishName().equals("연어")) {
                Assertions.assertThat(fishPairDto.getPairList().size()).isEqualTo(2);
                Assertions.assertThat(fishPairDto.getPairList().get(0)).isEqualTo("와인");
                Assertions.assertThat(fishPairDto.getPairList().get(1)).isEqualTo("치즈");
            }
        }
    }

    @Test
    @DisplayName("Empty case for recommendation fish pairs")
    void emptyRecommendFishPairs() {
        BDDMockito.given(fishRepository.findByName("고등어")).willReturn(Optional.empty());
        BDDMockito.given(fishRepository.findByName("연어")).willReturn(Optional.empty());
        BDDMockito.given(fishRepository.findByName("참치")).willReturn(Optional.empty());

        List<FishPairDto> fishPairDtoList = fishRecommendationService.recommendFishPairs(fishPriceDtoList);

        for(FishPairDto fishPairDto: fishPairDtoList) {
            Assertions.assertThat(fishPairDto.getPairList().size()).isEqualTo(0);
        }
    }
}