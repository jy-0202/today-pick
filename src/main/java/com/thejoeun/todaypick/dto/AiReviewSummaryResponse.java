package com.thejoeun.todaypick.dto;

import java.time.LocalDateTime;

import com.thejoeun.todaypick.domain.AiReviewSummary;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiReviewSummaryResponse {
	
	private Long id;
    private String summary;
    private String positiveKeywords;
    private String negativeKeywords;
    private LocalDateTime updatedAt;
    
    
    // AiReviewSummary Entity -> DTO 변환
    public static AiReviewSummaryResponse from(AiReviewSummary aiReviewSummary) {

        return AiReviewSummaryResponse.builder()
                .id(aiReviewSummary.getId())
                .summary(aiReviewSummary.getSummary())
                .positiveKeywords(aiReviewSummary.getPositiveKeywords())
                .negativeKeywords(aiReviewSummary.getNegativeKeywords())
                .updatedAt(aiReviewSummary.getUpdatedAt())
                .build();
    }
	
}
