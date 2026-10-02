package com.thejoeun.todaypick.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thejoeun.todaypick.domain.FoodCategory;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long>{

}
