package com.thejoeun.todaypick.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thejoeun.todaypick.dto.RestaurantResponse;
import com.thejoeun.todaypick.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecommendationController {

	private final RestaurantService restaurantService;
	
	@GetMapping("/ai-recommend")
	public String aiRecommend() {
		return "ai-recommend";
	}
	
	
	@GetMapping("/ai-recommend-result")
	public String aiRecommendResult(@RequestParam String prompt, Model model) {

	    System.out.println("AI 추천 요청 = " + prompt);

	    model.addAttribute("prompt", prompt);

	    return "ai-recommend";
	}
	
	
	@GetMapping("/condition-recommend")
	public String conditionRecommend(Model model) {
	    model.addAttribute("cities", restaurantService.findCities());
	    return "condition-recommend";
	}
	
	
	//// 추천 결과 페이지 //////////////////////////////////////////
	// 현재 조회 조건: city, district, ...
	// 예시 주소
	//	http://localhost:8080/recommend-result
	//	http://localhost:8080/recommend-result?city=서울특별시
	//	http://localhost:8080/recommend-result?city=서울특별시&district=강남구
	//	http://localhost:8080/recommend-result?city=서울특별시&district=강남구&town=역삼동
	// 조건없으면 전체 조회
	// 추천 결과 조회 - 지역, 가격대, 음식카테고리, 상세조건
	@GetMapping("/recommend-result")
	public String recommendResult(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String city, 
			@RequestParam(required = false) String district,
			@RequestParam(required = false) String town,
			@RequestParam(required = false) List<String> foodType,
			@RequestParam(required = false) String priceRange,
			@RequestParam(required = false) List<String> feature,
			Model model) {
		
		// 검색 조건 여부
		boolean hasFoodType = foodType != null && !foodType.isEmpty();
		boolean hasPriceRange = priceRange != null && !priceRange.isBlank();
		
		// 검색 결과
		List<RestaurantResponse> restaurants;
		
		if (hasFoodType && hasPriceRange) {
	        // 음식 종류 + 가격대
	        restaurants = restaurantService.findByRegionAndFoodCategoriesAndPriceRange(city, district, town, foodType, priceRange);

	    } else if (hasFoodType) {
	        // 음식 종류
	        restaurants = restaurantService.findByRegionAndFoodCategories(city, district, town, foodType);

	    } else if (hasPriceRange) {
	        // 가격대
	        restaurants = restaurantService.findByRegionAndPriceRange(city, district, town, priceRange);

	    } else if (city == null || city.isBlank()) {
	        // 조건 없음
	        restaurants = restaurantService.findAll();

	    } else if (district == null || district.isBlank()) {
	        // city
	        restaurants = restaurantService.findByCity(city);

	    } else if (town == null || town.isBlank()) {
	        // city + district
	        restaurants = restaurantService.findByCityAndDistrict(city, district);

	    } else {
	        // city + district + town
	        restaurants = restaurantService.findByCityAndDistrictAndTown(city, district, town);
	    }
		
		// 기존 검색 결과에 검색어 조건 추가 적용
		restaurants = restaurantService.filterByKeyword(restaurants, keyword);

		// 기존 검색 결과에 상세 조건 추가 적용
		restaurants = restaurantService.filterByFeatures(restaurants, feature);
		
		// 검색 결과 평점 및 리뷰 수 추가
		restaurants = restaurantService.applyReviewStats(restaurants);
		
		restaurants = restaurantService.applyCategoriesAndTags(restaurants);

		// 최종 결과를 Model에 한 번만 전달
		model.addAttribute("restaurants", restaurants);
		
		model.addAttribute("keyword", keyword);
	    model.addAttribute("city", city);
	    model.addAttribute("district", district);
	    model.addAttribute("town", town);
	    model.addAttribute("foodType", foodType);
	    model.addAttribute("priceRange", priceRange);
	    model.addAttribute("feature", feature);

	    return "recommend-result";
	}
	
}
