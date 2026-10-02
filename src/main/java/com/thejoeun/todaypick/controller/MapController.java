package com.thejoeun.todaypick.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thejoeun.todaypick.dto.RestaurantResponse;
import com.thejoeun.todaypick.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MapController {

    private final RestaurantService restaurantService;

    @Value("${kakao.map.javascript-key}")
    private String kakaoMapKey;

    @GetMapping("/map-explore")
    public String mapExplore(Model model) {
        List<RestaurantResponse> restaurants = restaurantService.findAll();

        restaurants = restaurantService.applyReviewStats(restaurants);
        restaurants = restaurantService.applyCategoriesAndTags(restaurants);

        model.addAttribute("restaurants", restaurants);
        model.addAttribute("kakaoMapKey", kakaoMapKey);

        return "map-explore";
    }
    
}