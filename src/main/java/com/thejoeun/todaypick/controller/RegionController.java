package com.thejoeun.todaypick.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thejoeun.todaypick.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RestaurantService restaurantService;

    @GetMapping("/districts")
    public List<String> districts(@RequestParam String city) {
        return restaurantService.findDistrictsByCity(city);
    }

    @GetMapping("/towns")
    public List<String> towns(@RequestParam String city, @RequestParam String district) {
        return restaurantService.findTownsByCityAndDistrict(city, district);
    }
    
}