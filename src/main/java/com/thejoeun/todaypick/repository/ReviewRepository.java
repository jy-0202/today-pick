package com.thejoeun.todaypick.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thejoeun.todaypick.domain.Review;
import com.thejoeun.todaypick.dto.ReviewStatsResponse;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 리뷰: 한 맛집에 여러 건
    List<Review> findByRestaurantId(Long restaurantId);

    
    // 여러 맛집의 평균 평점 및 리뷰 수 조회
    @Query("""
            SELECT new com.thejoeun.todaypick.dto.ReviewStatsResponse(
                r.restaurant.id, AVG(r.rating), COUNT(r)
            )
            FROM Review r
            WHERE r.restaurant.id IN :restaurantIds
            GROUP BY r.restaurant.id
            """)
    List<ReviewStatsResponse> findStatsByRestaurantIds(@Param("restaurantIds") List<Long> restaurantIds);


}