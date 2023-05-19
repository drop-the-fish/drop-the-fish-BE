package com.example.dropthefishbackendrdb.fish.service;

import com.example.dropthefishbackendrdb.fish.domain.enums.KamisApiUrl;
import com.example.dropthefishbackendrdb.fish.dto.FishPriceDto;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class FishPriceServiceTest {

    @InjectMocks
    FishPriceService fishPriceService;

    @Test
    public void testGetTodayPrice() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String mockApiResponse = "{\"data\":{\"error_code\":\"000\",\"item\":[" +
                "{\"item_name\":\"고등어\",\"item_code\":\"611\",\"kind_name\":\"생선(10kg)\",\"kind_code\":\"01\",\"rank\":\"중품\",\"rank_code\":\"05\",\"unit\":\"10kg\",\"day1\":\"당일 (05/18)\",\"dpr1\":\"61,600\",\"day2\":\"1일전 (05/17)\",\"dpr2\":\"72,300\",\"day3\":\"1주일전 (05/11)\",\"dpr3\":\"71,600\",\"day4\":\"2주일전 (05/04)\",\"dpr4\":\"-\",\"day5\":\"1개월전\",\"dpr5\":\"64,840\",\"day6\":\"1년전\",\"dpr6\":\"-\",\"day7\":\"일평년\",\"dpr7\":\"46,800\"}," +
                "{\"item_name\":\"고등어\",\"item_code\":\"611\",\"kind_name\":\"냉동(10kg)\",\"kind_code\":\"02\",\"rank\":\"중품\",\"rank_code\":\"05\",\"unit\":\"10kg\",\"day1\":\"당일 (05/18)\",\"dpr1\":\"56,000\",\"day2\":\"1일전 (05/17)\",\"dpr2\":\"55,000\",\"day3\":\"1주일전 (05/11)\",\"dpr3\":\"55,000\",\"day4\":\"2주일전 (05/04)\",\"dpr4\":\"54,300\",\"day5\":\"1개월전\",\"dpr5\":\"53,980\",\"day6\":\"1년전\",\"dpr6\":\"56,000\",\"day7\":\"일평년\",\"dpr7\":\"41,720\"}]}}";

        RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

        BDDMockito.given(restTemplateMock.getForObject(KamisApiUrl.DAILY_PRICE.getUrl() + "&p_regday=" + yesterday, String.class))
                        .willReturn(mockApiResponse);

        List<FishPriceDto> fishPriceDtoList = fishPriceService.getTodayPrice(restTemplateMock);

        Assertions.assertEquals(2, fishPriceDtoList.size());
    }
}