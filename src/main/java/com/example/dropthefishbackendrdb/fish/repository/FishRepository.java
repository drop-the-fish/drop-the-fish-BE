package com.example.dropthefishbackendrdb.fish.repository;

import com.example.dropthefishbackendrdb.fish.domain.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishRepository extends JpaRepository<Fish, Long> {
    Optional<Fish> findByName(String name);
}
