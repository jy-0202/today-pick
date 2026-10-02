package com.thejoeun.todaypick.dto;

import com.thejoeun.todaypick.domain.FoodCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodCategoryResponse {

    private Long id;
    private String name;

    public static FoodCategoryResponse from(FoodCategory foodCategory) {
        return FoodCategoryResponse.builder()
                .id(foodCategory.getId())
                .name(foodCategory.getName())
                .build();
    }
    
    
}