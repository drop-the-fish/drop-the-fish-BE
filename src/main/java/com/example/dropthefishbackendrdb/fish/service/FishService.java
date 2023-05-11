package com.example.dropthefishbackendrdb.fish.service;

import com.example.dropthefishbackendrdb.common.exception.ErrorCode;
import com.example.dropthefishbackendrdb.common.exception.InternalServerErrorException;
import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishImage;
import com.example.dropthefishbackendrdb.fish.dto.FishDetailResponseDto;
import com.example.dropthefishbackendrdb.fish.repository.FishImageRepository;
import com.example.dropthefishbackendrdb.fish.repository.FishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FishService {
    private static final int MONTH_OF_YEAR = 12;
    private final FishRepository fishRepository;
    private final FishImageRepository fishImageRepository;
    private final FishImageService fishImageService;

    public boolean isSeasonFish(Fish fish) {
        int curMonth = Integer.parseInt(LocalDate.now().toString().split("-")[1]);

        int seasonStart = fish.getSeasonStart();
        int seasonEnd = fish.getSeasonEnd();

        if(seasonStart > seasonEnd) {
            seasonEnd += MONTH_OF_YEAR;

            if(curMonth < seasonStart) {
                curMonth += MONTH_OF_YEAR;
            }
        }

        return seasonStart <= curMonth && curMonth <= seasonEnd;
    }

    public Fish findFishByName(String name) {
        Optional<Fish> findFish = fishRepository.findByName(name);

        if (findFish.isEmpty()) {
            throw new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR, "");
        }

        return findFish.get();
    }

    public FishDetailResponseDto findFishDetailByName(String name) {
        Fish findFish = findFishByName(name);
        List<FishImage> fishImageList = fishImageRepository.findAllByFish(findFish);

        Fish availabeImageFish = fishImageService.getAvailabeImageFish(findFish);

        return FishDetailResponseDto.from(availabeImageFish, fishImageList.stream().map(FishImage::getImageUrl).toList());
    }
}
