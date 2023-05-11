package com.example.dropthefishbackendrdb.fish.dto;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishImage;
import lombok.Getter;

@Getter
public class FIshAnalyzeResponseDto {
    private final FishImage[] fishImages;
    private final Fish fish;

    private FIshAnalyzeResponseDto(FishImage[] fishImages, Fish fish) {
        this.fishImages = fishImages;
        this.fish = fish;
    }

    public static FIshAnalyzeResponseDto from(FishImage[] fishImages, Fish fish) {
        return new FIshAnalyzeResponseDto(fishImages, fish);
    }
}
