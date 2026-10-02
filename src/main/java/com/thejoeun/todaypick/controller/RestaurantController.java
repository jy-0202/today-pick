package com.thejoeun.todaypick.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thejoeun.todaypick.dto.AiReviewSummaryResponse;
import com.thejoeun.todaypick.dto.MenuResponse;
import com.thejoeun.todaypick.dto.RestaurantResponse;
import com.thejoeun.todaypick.dto.ReviewResponse;
import com.thejoeun.todaypick.service.RestaurantService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	
	// 맛집 상세 조회 ---------------
	// 예시 : /restaurants/1
	@GetMapping("/{restaurantId}")
	public String restaurantDetail(@PathVariable Long restaurantId, Model model) {

	    RestaurantResponse restaurant = restaurantService.findById(restaurantId);
	    List<MenuResponse> menus = restaurantService.findMenusByRestaurantId(restaurantId);
	    List<ReviewResponse> reviews = restaurantService.findReviewsByRestaurantId(restaurantId);
	    AiReviewSummaryResponse aiReviewSummary = restaurantService.findAiReviewSummaryByRestaurantId(restaurantId);

	    Double averageRating = restaurantService.calculateAverageRating(reviews);

	    model.addAttribute("restaurant", restaurant);
	    model.addAttribute("menus", menus);
	    model.addAttribute("reviews", reviews);
	    model.addAttribute("aiReviewSummary", aiReviewSummary);
	    model.addAttribute("averageRating", averageRating);

	    return "restaurant-detail";
	}
	
	
}