package com.example.dropthefishbackendrdb.fish.domain;

import com.example.dropthefishbackendrdb.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "fish_image")
@NoArgsConstructor
public class FishImage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fish_id")
    private Fish fish;

    @Column(name = "image_url")
    private String imageUrl;

    private FishImage(
            Fish fish,
            String imageUrl
    ) {
        this.fish = fish;
        this.imageUrl = imageUrl;
    }

    public static FishImage of(
            Fish fish,
            String imageUrl
    ) {
        return new FishImage(fish, imageUrl);
    }
}
