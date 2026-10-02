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
@Table(name = "restaurant_food_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantFoodCategory {

    @EmbeddedId
    private RestaurantFoodCategoryId id;

    @MapsId("restaurantId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @MapsId("foodCategoryId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "food_category_id", nullable = false)
    private FoodCategory foodCategory;

    public RestaurantFoodCategory(
            Restaurant restaurant,
            FoodCategory foodCategory) {

        this.restaurant = restaurant;
        this.foodCategory = foodCategory;

        this.id = new RestaurantFoodCategoryId(
            restaurant.getId(),
            foodCategory.getId()
        );
    }
}