package com.example.dropthefishbackendrdb.fish.domain.enums;

import lombok.Getter;

@Getter
public enum KamisApiUrl {
    DAILY_PRICE("https://www.kamis.or.kr/service/price/xml.do?action=dailyPriceByCategoryList" +
            "&p_product_cls_code=02&p_country_code=1101&p_convert_kg_yn=N&p_item_category_code=600" +
            "&p_cert_key=3b9d1fa2-96c4-4670-883e-9df7ed23a575&p_cert_id=3340&p_returntype=json"),
    ;

    private final String url;

    KamisApiUrl(String url) {
        this.url = url;
    }
}
