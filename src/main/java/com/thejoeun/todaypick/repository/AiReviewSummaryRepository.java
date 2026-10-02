package com.thejoeun.todaypick.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thejoeun.todaypick.domain.AiReviewSummary;

public interface AiReviewSummaryRepository extends JpaRepository<AiReviewSummary, Long> {
	
	// AI 요약: 한 맛집에 한 건
	Optional<AiReviewSummary> findByRestaurantId(Long restaurantId);
	
}
