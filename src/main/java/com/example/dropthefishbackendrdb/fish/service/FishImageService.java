package com.example.dropthefishbackendrdb.fish.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.dropthefishbackendrdb.common.exception.BadRequestException;
import com.example.dropthefishbackendrdb.common.exception.ErrorCode;
import com.example.dropthefishbackendrdb.common.exception.InternalServerErrorException;
import com.example.dropthefishbackendrdb.fish.domain.Fish;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FishImageService {
    private final AmazonS3Client amazonS3Client;

    @Value("${ml.analyze.fish.api}")
    private String analyzeUri;

    @Value("${ml.analyze.sushi.api}")
    private String sushiAnalyzeUri;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String encodeImage(MultipartFile fishImage) {
        String encodedImage;
        Base64.Encoder encoder = Base64.getEncoder();

        try {
            byte[] photoEncode = encoder.encode(fishImage.getBytes());
            encodedImage = new String(photoEncode, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BadRequestException(ErrorCode.WRONG_IMAGE, "잘못된 이미지입니다.");
        }

        return encodedImage;
    }

    public String uploadImage(MultipartFile file) {
        String fileName=file.getOriginalFilename();
        System.out.println(fileName);

        try {
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR, "사진 업로드에 실패했습니다.");
        }

        assert fileName != null;
        return openImageUrl(fileName);
    }

    public String openImageUrl(String fileName) {
        // set expiration
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, (fileName).replace(File.separatorChar, '/'))
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    public Fish getAvailabeImageFish(Fish fish) {
        return Fish.of(fish.getName(), fish.getDescription(),
                fish.getSeasonStart(), fish.getSeasonEnd(),
                fish.getFeature(), openImageUrl(fish.getImageUrl()));
    }

    public String analyzeImage(String encodedImage) {
        RestTemplate restTemplate = new RestTemplate();

        //set header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", encodedImage);

        //create request
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(analyzeUri, requestEntity, String.class);

        return StringEscapeUtils.unescapeJava(response.getBody());
    }

    public String anlayzeSushiImage(String encodedImage) {
        RestTemplate restTemplate = new RestTemplate();

        //set header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", encodedImage);

        //create request
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(sushiAnalyzeUri, requestEntity, String.class);

        return StringEscapeUtils.unescapeJava(response.getBody());
    }
}
