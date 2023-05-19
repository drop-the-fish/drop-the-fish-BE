package com.example.dropthefishbackendrdb.fish.service;

import com.example.dropthefishbackendrdb.common.exception.ErrorCode;
import com.example.dropthefishbackendrdb.common.exception.InternalServerErrorException;
import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishImage;
import com.example.dropthefishbackendrdb.fish.dto.FishDetailResponseDto;
import com.example.dropthefishbackendrdb.fish.repository.FishImageRepository;
import com.example.dropthefishbackendrdb.fish.repository.FishRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FishServiceTest {
    @Mock
    FishRepository fishRepository;

    @Mock
    FishImageRepository fishImageRepository;

    @Mock
    FishImageService fishImageService;

    @InjectMocks
    FishService fishService;

    private Fish salmon;

    @BeforeEach
    void setUp() {
        salmon = Fish.of("salmon", "des", 8, 2, "tasty", "salmon.jpeg");
    }

    @Test
    @DisplayName("Success case for isSeasonFish")
    void successIsSeasonFish() {
        assertThat(fishService.isSeasonFish(salmon, 10)).isTrue();
        assertThat(fishService.isSeasonFish(salmon, 5)).isFalse();
    }

    @Test
    @DisplayName("Success case for find fish by name")
    void successFindFishByName() {
        BDDMockito.given(fishRepository.findByName("salmon")).willReturn(Optional.of(salmon));

        assertThat(fishService.findFishByName("salmon").getName()).isEqualTo("salmon");
        assertThat(fishService.findFishByName("salmon").getDescription()).isEqualTo("des");
        assertThat(fishService.findFishByName("salmon").getFeature()).isEqualTo("tasty");
    }

    @Test
    @DisplayName("No fish case for find by name")
    void NoFindFishByName() {
        BDDMockito.given(fishRepository.findByName("salmon")).willReturn(Optional.empty());

        final InternalServerErrorException ex = Assertions.assertThrows(InternalServerErrorException.class,
                () -> this.fishService.findFishByName("salmon"));

        assertThat(ex)
                .extracting("errorCode", "message")
                .contains(ErrorCode.INTERNAL_SERVER_ERROR, "");
    }

    @Test
    @DisplayName("Success case for find fish detail by name")
    void successFindFishDetailByName() {
        FishImage salmonImage1 = FishImage.of(salmon, "salmon1.jpeg");
        FishImage salmonImage2 = FishImage.of(salmon, "salmon2.jpeg");

        List<FishImage> fishImageList = new ArrayList<>();
        fishImageList.add(salmonImage1);
        fishImageList.add(salmonImage2);

        BDDMockito.given(fishRepository.findByName("salmon")).willReturn(Optional.of(salmon));
        BDDMockito.given(fishImageRepository.findAllByFish(salmon)).willReturn(fishImageList);
        BDDMockito.given(fishImageService.openImageUrl("salmon1.jpeg")).willReturn("salmonDish.jpeg");
        BDDMockito.given(fishImageService.openImageUrl("salmon2.jpeg")).willReturn("salmonDish.jpeg");
        BDDMockito.given(fishImageService.getAvailabeImageFish(salmon)).willReturn(
                Fish.of("salmon", "des", 8, 2, "tasty", "available_salmon.jpeg"));

        FishDetailResponseDto salmonDetailResponse = fishService.findFishDetailByName("salmon");

        assertThat(salmonDetailResponse.getFishImageUrl()).isEqualTo("available_salmon.jpeg");
        for(String dishImageUrl : salmonDetailResponse.getFishDishImageUrl()) {
            assertThat(dishImageUrl).isEqualTo("salmonDish.jpeg");
        }
    }
}