package com.example.dropthefishbackendrdb.fish.repository;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class FishPairRepositoryTest {

    @Autowired
    private FishPairRepository fishPairRepository;

    @Autowired
    private FishRepository fishRepository;

    private Fish salmon, emptyFish;
    @BeforeEach
    void setUp() {
        salmon = Fish.of("salmon", "des", 8, 2, "tasty", "salmon.jpeg");
        emptyFish = Fish.of("empty", "des", 8, 2, "tasty", "empty.jpeg");
        fishRepository.save(salmon);
        fishRepository.save(emptyFish);

        FishPair salmonPair1 = FishPair.of(salmon, "와인");
        FishPair salmonPair2 = FishPair.of(salmon, "치즈");

        fishPairRepository.save(salmonPair1);
        fishPairRepository.save(salmonPair2);
    }

    @Test
    void successFindAllByFish() {
        List<FishPair> salmonPairs = fishPairRepository.findAllByFish(salmon);

        assertThat(salmonPairs.size()).isEqualTo(2);
        assertThat(salmonPairs.get(0).getPair()).isEqualTo("와인");
        assertThat(salmonPairs.get(1).getPair()).isEqualTo("치즈");
    }

    @Test
    void emptyFindAllByFish() {
        List<FishPair> emptyList = fishPairRepository.findAllByFish(emptyFish);

        assertThat(emptyList.size()).isEqualTo(0);
    }
}