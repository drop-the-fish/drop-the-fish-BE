package com.example.dropthefishbackendrdb.fish.repository;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class FishRepositoryTest {
    @Autowired
    private FishRepository fishRepository;

    private Fish salmon, tuna;
    @BeforeEach
    void setUp() {
        salmon = Fish.of("salmon", "des", 8, 2, "tasty", "salmon.jpeg");
        tuna = Fish.of("tuna", "des", 8, 2, "tasty", "tuna.jpeg");

        fishRepository.save(salmon);
        fishRepository.save(tuna);
    }

    @Test
    void successFindByName() {
        Fish findSalmon = fishRepository.findByName("salmon").get();
        Fish findTuna = fishRepository.findByName("tuna").get();

        assertThat(findSalmon.getName()).isEqualTo("salmon");
        assertThat(findSalmon.getImageUrl()).isEqualTo("salmon.jpeg");
        assertThat(findTuna.getName()).isEqualTo("tuna");
        assertThat(findTuna.getImageUrl()).isEqualTo("tuna.jpeg");
    }

    @Test
    void emptyFindByName() {
        Optional<Fish> findSalmon = fishRepository.findByName("noFish");

        assertThat(findSalmon).isEmpty();
    }
}