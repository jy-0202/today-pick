package com.thejoeun.todaypick.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewStatsResponse {

    private Long restaurantId;
    private Double averageRating;
    private Long reviewCount;
    
}