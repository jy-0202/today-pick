package com.thejoeun.todaypick.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "restaurant_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTag {

    @EmbeddedId
    private RestaurantTagId id;

    @MapsId("restaurantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    public RestaurantTag(Restaurant restaurant, Tag tag) {
        this.restaurant = restaurant;
        this.tag = tag;
        this.id = new RestaurantTagId(restaurant.getId(), tag.getId());
    }
}