package com.thejoeun.todaypick.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thejoeun.todaypick.domain.RestaurantFoodCategory;
import com.thejoeun.todaypick.domain.RestaurantFoodCategoryId;
import com.thejoeun.todaypick.dto.RestaurantLabelResponse;

public interface RestaurantFoodCategoryRepository extends JpaRepository<RestaurantFoodCategory, RestaurantFoodCategoryId> {

    @Query("""
            SELECT new com.thejoeun.todaypick.dto.RestaurantLabelResponse(
                rfc.restaurant.id, rfc.foodCategory.name
            )
            FROM RestaurantFoodCategory rfc
            WHERE rfc.restaurant.id IN :restaurantIds
            """)
    List<RestaurantLabelResponse> findNamesByRestaurantIds(@Param("restaurantIds") List<Long> restaurantIds);
    
}