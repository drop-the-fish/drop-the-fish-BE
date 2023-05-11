package com.example.dropthefishbackendrdb.fish.service;

import com.example.dropthefishbackendrdb.common.exception.ErrorCode;
import com.example.dropthefishbackendrdb.common.exception.InternalServerErrorException;
import com.example.dropthefishbackendrdb.fish.domain.enums.KamisApiUrl;
import com.example.dropthefishbackendrdb.fish.dto.FishPriceDto;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FishPriceService {
    public List<FishPriceDto> getTodayPrice() {
        String response;
        RestTemplate template = new RestTemplate();
        List<FishPriceDto> fishPriceDtoList = new ArrayList<>();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        try {
            response =
                    template.getForObject(KamisApiUrl.DAILY_PRICE.getUrl() + "&p_regday=" + yesterday, String.class);
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(response);
            JSONObject fishDataJsonObj = (JSONObject) jsonObj.get("data");

            JSONArray fishArray = (JSONArray) fishDataJsonObj.get("item");

            fishArray.forEach(fish -> fishPriceDtoList.add(FishPriceDto.from((JSONObject) fish)));

        } catch (ParseException e) {
            throw new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR, "가격을 불러오는데 실패했습니다.");
        }

        return fishPriceDtoList;
    }
}
