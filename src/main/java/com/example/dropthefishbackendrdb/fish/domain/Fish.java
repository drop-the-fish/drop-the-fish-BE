package com.example.dropthefishbackendrdb.fish.domain;

import com.example.dropthefishbackendrdb.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "fish")
@NoArgsConstructor
public class Fish extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "season_start", nullable = false)
    private int seasonStart;
    @Column(name = "season_end", nullable = false)
    private int seasonEnd;
    @Column(name = "feature")
    private String feature;
    @Column(name = "image_url")
    private String imageUrl;

    private Fish(
            String name,
            String description,
            int seasonStart,
            int seasonEnd,
            String feature,
            String imageUrl
    ) {
        this.name = name;
        this.description = description;
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
        this.feature = feature;
        this.imageUrl = imageUrl;
    }

    public static Fish of(
            String name,
            String description,
            int seasonStart,
            int seasonEnd,
            String feature,
            String imageUrl
    ) {
        return  new Fish(name, description, seasonStart, seasonEnd, feature, imageUrl);
    }
}
