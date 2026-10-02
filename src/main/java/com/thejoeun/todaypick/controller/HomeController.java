package com.thejoeun.todaypick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thejoeun.todaypick.service.FoodCategoryService;
import com.thejoeun.todaypick.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final FoodCategoryService foodCategoryService;
	private final RestaurantService restaurantService;

	@GetMapping("/")
	public String home(Model model) {
		
		// 지역 목록
		model.addAttribute("cities", restaurantService.findCities());
		
		// 인기 카테고리 : 전체카테고리 -> 랜덤 6개
	    model.addAttribute("foodCategories", foodCategoryService.findRandom(6));
	    
	    // 오늘의 추천 맛집 : 전체 맛집 -> 랜덤 4개
	    model.addAttribute("recommendedRestaurants", restaurantService.findRandom(4));
	    
	    return "index";
	}

}
