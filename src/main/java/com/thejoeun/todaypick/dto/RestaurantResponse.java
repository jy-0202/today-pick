package com.thejoeun.todaypick.dto;

import java.math.BigDecimal;
import java.util.List;

import com.thejoeun.todaypick.domain.Restaurant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class RestaurantResponse {
	
	private Long id;
	private String name;
	private String address;
	private String priceRange;
	private String description;
	private String openingHours;
	private String phone;
	private String imageUrl;
	private BigDecimal latitude;
    private BigDecimal longitude;
	
	private Double averageRating;
	private Long reviewCount;
	private List<String> categories;
	private List<String> tags;
	
	// Restaurant Entity를 RestaurantResponse DTO로 변환
	// from	: Entity를 DTO로 변환하는 정적 메서드
	public static RestaurantResponse from(Restaurant restaurant) {
		
		return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .priceRange(restaurant.getPriceRange())
                .description(restaurant.getDescription())
                .openingHours(restaurant.getOpeningHours())
                .phone(restaurant.getPhone())
                .imageUrl(restaurant.getImageUrl())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .build();
		
	}

}
