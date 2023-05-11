package com.example.dropthefishbackendrdb.fish.repository;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import com.example.dropthefishbackendrdb.fish.domain.FishPair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishPairRepository extends JpaRepository<FishPair, Long> {
    List<FishPair> findAllByFish(Fish fish);
}
