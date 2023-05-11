package com.example.dropthefishbackendrdb.fish.domain;

import com.example.dropthefishbackendrdb.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "fish_pair")
@NoArgsConstructor
public class FishPair extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fish_id")
    private Fish fish;

    @Column(name = "pair")
    private String pair;

    private FishPair(Fish fish, String pair) {
        this.fish = fish;
        this.pair = pair;
    }

    public static FishPair of(Fish fish, String pair) {
        return new FishPair(fish, pair);
    }
}
