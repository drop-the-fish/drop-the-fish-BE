package com.example.dropthefishbackendrdb.fish.dto;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FishPriceDto {
    private final String itemName;
    private final List<Integer> priceList;
    private final String unit;
    private final String rank;

    private FishPriceDto(String itemName, List<Integer> priceList, String unit, String rank) {
        this.itemName = itemName;
        this.priceList = priceList;
        this.unit = unit;
        this.rank = rank;
    }

    public static FishPriceDto from(JSONObject fishObject) {
        String itemName = (String) fishObject.get("item_name");
        String unit = (String) fishObject.get("unit");
        String rank = (String) fishObject.get("rank");
        List<Integer> priceList = parsePriceList(fishObject);

        return new FishPriceDto(itemName, priceList, unit, rank);
    }

    public static List<Integer> parsePriceList(JSONObject fishObject) {
        List<Integer> priceList = new ArrayList<>();

        priceList.add(parsePriceToInt((String) fishObject.get("dpr1")));
        priceList.add(parsePriceToInt((String) fishObject.get("dpr2")));
        priceList.add(parsePriceToInt((String) fishObject.get("dpr3")));
        priceList.add(parsePriceToInt((String) fishObject.get("dpr4")));
        priceList.add(parsePriceToInt((String) fishObject.get("dpr5")));
        priceList.add(parsePriceToInt((String) fishObject.get("dpr6")));
        priceList.add(parsePriceToInt((String) fishObject.get("dpr7")));

        return priceList;
    }

    public static int parsePriceToInt(String price) {
        if (price.equals("-")) {
            return 0;
        }

        return Integer.parseInt(price.replaceAll(",", ""));
    }
}
