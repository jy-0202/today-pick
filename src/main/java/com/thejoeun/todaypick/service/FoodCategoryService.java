package com.thejoeun.todaypick.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.thejoeun.todaypick.dto.FoodCategoryResponse;
import com.thejoeun.todaypick.repository.FoodCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodCategoryService {

    private final FoodCategoryRepository foodCategoryRepository;

    public List<FoodCategoryResponse> findAll() {
        return foodCategoryRepository.findAll()
                .stream()
                .map(FoodCategoryResponse::from)
                .toList();
    }
    
    // 인기카테고리 : 전체 카테고리 -> 랜덤 n개
    public List<FoodCategoryResponse> findRandom(int count) {
        List<FoodCategoryResponse> categories = new ArrayList<>(
                foodCategoryRepository.findAll()
                        .stream()
                        .map(FoodCategoryResponse::from)
                        .toList()
        );

        Collections.shuffle(categories);

        return categories.stream()
                .limit(count)
                .toList();
    }
}