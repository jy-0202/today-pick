package com.thejoeun.todaypick.dto;

import java.time.LocalDateTime;

import com.thejoeun.todaypick.domain.Review;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponse {
	
	private Long id;
	private Byte rating;
	private String content;
	private LocalDateTime createdAt;
	
	// Review Entity를 ReviewResponse DTO로 변환
	public static ReviewResponse from(Review review) {
		
		return ReviewResponse.builder()
				.id(review.getId())
				.rating(review.getRating())
				.content(review.getContent())
				.createdAt(review.getCreatedAt())
				.build();
	}
	
	
	

}
