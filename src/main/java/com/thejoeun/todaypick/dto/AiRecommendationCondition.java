package com.thejoeun.todaypick.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiRecommendationCondition {
// AI가 사용자 문장에서 추출한 조건을 담는 역할
	
	private String city;
    private String district;
    private String town;

    private List<String> foodTypes;

    private String priceRange;

    private List<String> features;	
	
}
