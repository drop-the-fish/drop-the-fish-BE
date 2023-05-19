package com.example.dropthefishbackendrdb.e2e;

import com.example.dropthefishbackendrdb.common.exception.ErrorCode;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FishE2ETest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void successGetFishDetail() {
        String expectedImageUrl = "https://dropthefishbucket.s3.ap-northeast-2.amazonaws.com/salmon.jpeg?";
        String expectedFeature = "연어의 형태는 몸은 약간 가늘고 긴 편으로 꼬리는 가는 것이 특징입니다. 양턱의 이빨은 송곳니 모양으로 뾰족합니다. 머리는 원추형이고, 배지느러미는 배의 정중앙에 위치하며, 각 지느러미에는 가시가 없습니다.";
        String expectedDescription = "연어(鰱魚)는 연어속에 속하는 물고기이다. 치어는 강에서 태어나 바다로 가서 살다가 성체가 되면 다시 강을 거슬러 올라와 상류에서 알을 낳는 회유성 어종이다. 이 독특한 회유 습성으로 인해 생태계에 영양을 제공하는 역할을 하고 있다. 횟감이나 구이, 샐러드 요리 등으로 인기가 많은 생선이다.";

        RestAssured
                .given().log().all()
                .param("fishName", "연어")
                .when()
                .get("/api/fish")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("fishImageUrl", response -> Matchers.containsString(expectedImageUrl))
                .body("feature", response ->  equalTo(expectedFeature))
                .body("description", response ->  equalTo(expectedDescription));

    }

    @Test
    void emptyGetFishDetail() {
        RestAssured
                .given().log().all()
                .param("fishName", "emptyFish")
                .when()
                .get("/api/fish")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("errorCode", response -> equalTo(ErrorCode.INTERNAL_SERVER_ERROR.getCode()));

    }

    @Test
    void testGetTodayPrice() {
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .when()
                .get("/api/fish/price")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();

        ResponseBodyExtractionOptions body = createResponse.body();
        List fishPriceDtoList = body.jsonPath().get("fishPriceDtoList");


        assertThat(fishPriceDtoList.size()).isEqualTo(14);
        for (Object fishPrice: fishPriceDtoList){
            try {
                JSONObject jsonObject = new org.json.JSONObject(fishPrice.toString());

                assertThat(jsonObject.has("itemName")).isTrue();
                assertThat(jsonObject.has("unit")).isTrue();
                assertThat(jsonObject.has("rank")).isTrue();
                assertThat(jsonObject.has("priceList")).isTrue();

                JSONArray priceList = jsonObject.getJSONArray("priceList");
                assertThat(priceList.length()).isEqualTo(7);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
