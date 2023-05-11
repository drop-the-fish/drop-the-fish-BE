package com.example.dropthefishbackendrdb.fish.dto;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import lombok.Getter;

@Getter
public class SeasonFishResponseDto {
    private final String name;
    private final int seasonStart;
    private final int seasonEnd;

    private SeasonFishResponseDto(String name, int seasonStart, int seasonEnd) {
        this.name = name;
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
    }

    public static SeasonFishResponseDto from(Fish fish) {
        return new SeasonFishResponseDto(fish.getName(), fish.getSeasonStart(), fish.getSeasonEnd());
    }
}
