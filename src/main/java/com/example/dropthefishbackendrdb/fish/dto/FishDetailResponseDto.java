package com.example.dropthefishbackendrdb.fish.dto;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import lombok.Getter;

import java.util.List;

@Getter
public class FishDetailResponseDto {
    private final String fishImageUrl;
    private final List<String> fishDishImageUrl;
    private final String feature;
    private final String description;

    private FishDetailResponseDto(String fishImageUrl, List<String> fishDishImageUrl, String feature, String description) {
        this.fishImageUrl = fishImageUrl;
        this.fishDishImageUrl = fishDishImageUrl;
        this.feature = feature;
        this.description = description;
    }

    public static FishDetailResponseDto from(Fish fish, List<String> fishDishImageUrl) {
        return new FishDetailResponseDto(fish.getImageUrl(), fishDishImageUrl, fish.getFeature(), fish.getDescription());
    }
}
