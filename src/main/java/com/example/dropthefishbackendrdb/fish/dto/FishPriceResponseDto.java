package com.example.dropthefishbackendrdb.fish.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FishPriceResponseDto {
    private final List<FishPriceDto> fishPriceDtoList;

    private FishPriceResponseDto(List<FishPriceDto> fishPriceDtoList) {
        this.fishPriceDtoList = fishPriceDtoList;
    }

    public static FishPriceResponseDto from(List<FishPriceDto> fishPriceDtoList) {
        return new FishPriceResponseDto(fishPriceDtoList);
    }
}
