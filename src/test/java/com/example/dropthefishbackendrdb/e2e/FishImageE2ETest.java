package com.example.dropthefishbackendrdb.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.io.File;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FishImageE2ETest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void successAnalyzeImage() {
        File flatFishImage = new File("src/test/resources/e2eTestImages/flatFish.jpeg");

        ExtractableResponse<Response> createdResponse = RestAssured
                .given().log().all()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", flatFishImage)
                .when()
                .post("/api/fish/analyze")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();

        ResponseBodyExtractionOptions body = createdResponse.body();
        String jsonString = body.jsonPath().get().toString();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            assertThat(jsonObject.has("광어")).isTrue();
            assertThat((int) jsonObject.get("광어")).isPositive();
            assertThat((int) jsonObject.get("광어")).isLessThanOrEqualTo(100);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void nonFishAnalyzeImage() {
        File nonFishImage = new File("src/test/resources/e2eTestImages/nonFish.png");

        ExtractableResponse<Response> createdResponse = RestAssured
                .given().log().all()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", nonFishImage)
                .when()
                .post("/api/fish/analyze")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract();

        ResponseBodyExtractionOptions body = createdResponse.body();
        String nullString = body.jsonPath().get().toString();

        assertThat(nullString).contains("NULL");
        assertThat(nullString).contains("어종 인식에 실패했습니다.");
    }
}
