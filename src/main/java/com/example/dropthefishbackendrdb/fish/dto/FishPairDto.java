package com.example.dropthefishbackendrdb.fish.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FishPairDto {
    private final String fishName;
    private final List<String> pairList;

    private FishPairDto(String fishName, List<String> pairList) {
        this.fishName = fishName;
        this.pairList = pairList;
    }

    public static FishPairDto from(String fishName, List<String> pairList) {
        return new FishPairDto(fishName, pairList);
    }
}
