package com.thejoeun.todaypick.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thejoeun.todaypick.domain.RestaurantTag;
import com.thejoeun.todaypick.domain.RestaurantTagId;
import com.thejoeun.todaypick.dto.RestaurantLabelResponse;

public interface RestaurantTagRepository extends JpaRepository<RestaurantTag, RestaurantTagId> {

    @Query("""
            SELECT new com.thejoeun.todaypick.dto.RestaurantLabelResponse(
                rt.restaurant.id, rt.tag.name
            )
            FROM RestaurantTag rt
            WHERE rt.restaurant.id IN :restaurantIds
            """)
    List<RestaurantLabelResponse> findNamesByRestaurantIds(@Param("restaurantIds") List<Long> restaurantIds);
    
}