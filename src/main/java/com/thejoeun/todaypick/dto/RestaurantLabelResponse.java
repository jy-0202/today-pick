package com.thejoeun.todaypick.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantLabelResponse {
	
    private Long restaurantId;
    private String name;
    
}