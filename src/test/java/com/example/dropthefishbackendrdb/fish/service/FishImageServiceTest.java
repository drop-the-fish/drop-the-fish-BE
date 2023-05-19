package com.example.dropthefishbackendrdb.fish.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FishImageServiceTest {

    @InjectMocks
    FishImageService fishImageService;

    @Test
    @DisplayName("success case for encoding image")
    void successEncodeImage() {
        // 테스트용 이미지 파일 생성
        byte[] imageBytes = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01, 0x02, 0x00, 0x00, 0x64 };
        MultipartFile imageFile = new MockMultipartFile("fishImage", "image.jpg", "image/jpeg", imageBytes);

        // 테스트 대상 함수 호출
        String encodedImage = fishImageService.encodeImage(imageFile);

        // 기대하는 결과와 실제 결과 비교
        assertThat(encodedImage).isEqualTo("/9j/4AAQSkZJRgABAgAAZA==");
    }
}