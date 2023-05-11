package com.example.dropthefishbackendrdb.fish.repository;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishImageRepository extends JpaRepository<FishImage, Long> {
    List<FishImage> findAllByFish(Fish fish);
}
