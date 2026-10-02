package com.thejoeun.todaypick.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @Column(name = "town", length = 30)
    private String town;

    @Column(
        name = "latitude",
        nullable = false,
        precision = 10,
        scale = 7
    )
    private BigDecimal latitude;

    @Column(
        name = "longitude",
        nullable = false,
        precision = 10,
        scale = 7
    )
    private BigDecimal longitude;

    @Column(name = "price_range", length = 20)
    private String priceRange;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "opening_hours", length = 200)
    private String openingHours;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @OneToMany(mappedBy = "restaurant")
    private final List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private final List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "restaurant")
    private AiReviewSummary aiReviewSummary;

    @OneToMany(mappedBy = "restaurant")
    private final List<RestaurantFoodCategory> restaurantFoodCategories = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private final List<RestaurantTag> restaurantTags = new ArrayList<>();
}