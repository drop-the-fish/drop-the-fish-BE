package com.example.dropthefishbackendrdb.e2e;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FishRecommendationE2ETest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void recommendCheapMonthlyFish() {
        ExtractableResponse<Response> createdResponse = RestAssured
                .given().log().all()
                .when()
                .get("/api/recommendation/price/month")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();

        ResponseBodyExtractionOptions body = createdResponse.body();
        String jsonString = body.jsonPath().get().toString();


        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            int lastPrice = Integer.MAX_VALUE;

            assertThat(jsonArray.length()).isEqualTo(5);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject fish = jsonArray.getJSONObject(i);
                JSONArray priceList = fish.getJSONArray("priceList");
                int priceMonthDiff = (int) priceList.get(4) - (int) priceList.get(0);

                assertThat(priceMonthDiff).isLessThanOrEqualTo(lastPrice);

                lastPrice = priceMonthDiff;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    void recommendCheapYearlyFish() {
        ExtractableResponse<Response> createdResponse = RestAssured
                .given().log().all()
                .when()
                .get("/api/recommendation/price/year")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();

        ResponseBodyExtractionOptions body = createdResponse.body();
        String jsonString = body.jsonPath().get().toString();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            int lastPrice = Integer.MAX_VALUE;

            assertThat(jsonArray.length()).isEqualTo(5);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject fish = jsonArray.getJSONObject(i);
                JSONArray priceList = fish.getJSONArray("priceList");
                int priceMonthDiff = (int) priceList.get(5) - (int) priceList.get(0);

                assertThat(priceMonthDiff).isLessThanOrEqualTo(lastPrice);

                lastPrice = priceMonthDiff;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    void recommendFishPairs() {
        ExtractableResponse<Response> createdResponse = RestAssured
                .given().log().all()
                .when()
                .get("/api/recommendation/pair")
                .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();

        ResponseBodyExtractionOptions body = createdResponse.body();
        String jsonString = body.jsonPath().get().toString();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject fish = jsonArray.getJSONObject(i);
                JSONArray pairList = fish.getJSONArray("pairList");

                assertThat(pairList.length()).isGreaterThan(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
