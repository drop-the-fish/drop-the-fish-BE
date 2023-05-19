package com.example.dropthefishbackendrdb.fish.repository;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class FishImageRepositoryTest {
    @Autowired
    private FishRepository fishRepository;

    @Autowired
    private FishImageRepository fishImageRepository;

    private Fish salmon;

    @BeforeEach
    void setUp() {
        salmon = Fish.of("salmon", "des", 8, 2, "tasty", "salmon.jpeg");
        fishRepository.save(salmon);

        fishImageRepository.save(FishImage.of(salmon, "salmon1.jpeg"));
        fishImageRepository.save(FishImage.of(salmon, "salmon2.jpeg"));
    }

    @Test
    void successFindAllByFish() {
        List<FishImage> allByFish = fishImageRepository.findAllByFish(salmon);

        assertThat(allByFish.size()).isEqualTo(2);
        assertThat(allByFish.get(0).getImageUrl()).isEqualTo("salmon1.jpeg");
        assertThat(allByFish.get(1).getImageUrl()).isEqualTo("salmon2.jpeg");
    }
}