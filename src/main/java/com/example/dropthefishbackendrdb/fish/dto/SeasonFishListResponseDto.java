package com.example.dropthefishbackendrdb.fish.dto;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SeasonFishListResponseDto {
    private final List<SeasonFishResponseDto> seasonFishResponseDtoList;

    private SeasonFishListResponseDto(List<Fish> fishList) {
        seasonFishResponseDtoList = new ArrayList<>();
        for (Fish fish: fishList) {
            seasonFishResponseDtoList.add(SeasonFishResponseDto.from(fish));
        }
    }

    public static SeasonFishListResponseDto from(List<Fish> fishList) {
        return new SeasonFishListResponseDto(fishList);
    }
}
