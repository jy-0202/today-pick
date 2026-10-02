package com.thejoeun.todaypick.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.thejoeun.todaypick.domain.Menu;
import com.thejoeun.todaypick.domain.Restaurant;
import com.thejoeun.todaypick.dto.AiReviewSummaryResponse;
import com.thejoeun.todaypick.dto.MenuResponse;
import com.thejoeun.todaypick.dto.RestaurantLabelResponse;
import com.thejoeun.todaypick.dto.RestaurantResponse;
import com.thejoeun.todaypick.dto.ReviewResponse;
import com.thejoeun.todaypick.dto.ReviewStatsResponse;
import com.thejoeun.todaypick.repository.AiReviewSummaryRepository;
import com.thejoeun.todaypick.repository.MenuRepository;
import com.thejoeun.todaypick.repository.RestaurantFoodCategoryRepository;
import com.thejoeun.todaypick.repository.RestaurantRepository;
import com.thejoeun.todaypick.repository.RestaurantTagRepository;
import com.thejoeun.todaypick.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {
	
	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;
	private final ReviewRepository reviewRepository;
	private final AiReviewSummaryRepository aiReviewSummaryRepository;
	private final RestaurantFoodCategoryRepository restaurantFoodCategoryRepository;
	private final RestaurantTagRepository restaurantTagRepository;


	// 전체 음식점 조회
	public List<RestaurantResponse> findAll() {
		
		return restaurantRepository.findAll()
				.stream()
				.map(RestaurantResponse::from)
				.toList();
	}
	
	
	// 랜덤 추천 음식점 조회
	public List<RestaurantResponse> findRandom(int count) {
	    List<RestaurantResponse> restaurants = new ArrayList<>(
	            restaurantRepository.findAll()
	                    .stream()
	                    .map(RestaurantResponse::from)
	                    .toList()
	    );

	    Collections.shuffle(restaurants);

	    return restaurants.stream()
	            .limit(count)
	            .toList();
	}
	
	
	// 검색어로 맛집 조회
	public List<RestaurantResponse> findByKeyword(String keyword) {
	    List<Restaurant> nameRestaurants =
	            restaurantRepository.findByNameContainingIgnoreCase(keyword);

	    List<Menu> menus =
	            menuRepository.findByMenuNameContainingIgnoreCase(keyword);

	    Map<Long, Restaurant> restaurantMap = new LinkedHashMap<>();

	    // 음식점 이름 검색 결과
	    for (Restaurant restaurant : nameRestaurants) {
	        restaurantMap.put(restaurant.getId(), restaurant);
	    }

	    // 메뉴명 검색 결과
	    for (Menu menu : menus) {
	        Restaurant restaurant = menu.getRestaurant();
	        restaurantMap.put(restaurant.getId(), restaurant);
	    }

	    return restaurantMap.values().stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 기존 검색 결과에 검색어 조건 추가 적용
	public List<RestaurantResponse> filterByKeyword(
	        List<RestaurantResponse> restaurants, String keyword) {

	    if (keyword == null || keyword.isBlank()) {
	        return restaurants;
	    }

	    List<RestaurantResponse> keywordRestaurants = findByKeyword(keyword);
	    Set<Long> restaurantIds = new HashSet<>();

	    for (RestaurantResponse restaurant : keywordRestaurants) {
	        restaurantIds.add(restaurant.getId());
	    }

	    return restaurants.stream()
	            .filter(restaurant -> restaurantIds.contains(restaurant.getId()))
	            .toList();
	}
	
	
	// 지역 조회 - city
	public List<RestaurantResponse> findByCity(String city) {

	    return restaurantRepository.findByCity(city)
	            .stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 지역 조회 - city & district
	public List<RestaurantResponse> findByCityAndDistrict(String city, String district) {

	    return restaurantRepository
	            .findByCityAndDistrict(city, district)
	            .stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 지역 조회 - city & district & town
	public List<RestaurantResponse> findByCityAndDistrictAndTown(String city, String district, String town) {

	    return restaurantRepository
	            .findByCityAndDistrictAndTown(city, district, town)
	            .stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 지역 선택 목록 조회
	public List<String> findCities() {
	    return restaurantRepository.findDistinctCities();
	}

	public List<String> findDistrictsByCity(String city) {
	    return restaurantRepository.findDistinctDistrictsByCity(city);
	}

	public List<String> findTownsByCityAndDistrict(String city, String district) {
	    return restaurantRepository.findDistinctTownsByCityAndDistrict(city, district);
	}
	
	
	// 조회 - 음식 카테고리 단독 조회
	public List<RestaurantResponse> findByFoodCategories(List<String> categoryNames) {

	    return restaurantRepository
	            .findDistinctByRestaurantFoodCategoriesFoodCategoryNameIn(categoryNames)
	            .stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 조회 - 음식 카테고리 & 지역
	public List<RestaurantResponse> findByRegionAndFoodCategories(String city, String district, String town, List<String> categoryNames) {

	    List<Restaurant> restaurants;

	    if (city == null || city.isBlank()) {
	        restaurants = restaurantRepository
	                .findDistinctByRestaurantFoodCategoriesFoodCategoryNameIn(categoryNames);
	    } else if (district == null || district.isBlank()) {
	        restaurants = restaurantRepository
	                .findDistinctByCityAndRestaurantFoodCategoriesFoodCategoryNameIn(city, categoryNames);
	    } else if (town == null || town.isBlank()) {
	        restaurants = restaurantRepository
	                .findDistinctByCityAndDistrictAndRestaurantFoodCategoriesFoodCategoryNameIn(
	                		city, district, categoryNames);
	    } else {
	        restaurants = restaurantRepository
	                .findDistinctByCityAndDistrictAndTownAndRestaurantFoodCategoriesFoodCategoryNameIn(
	                		city, district, town, categoryNames);
	    }

	    return restaurants.stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 조회 - 지역 & 가격대
	public List<RestaurantResponse> findByRegionAndPriceRange(String city, String district, String town, String priceRange) {

	    List<Restaurant> restaurants;

	    if (city == null || city.isBlank()) {
	        restaurants = restaurantRepository.findByPriceRange(priceRange);
	    } else if (district == null || district.isBlank()) {
	        restaurants = restaurantRepository.findByCityAndPriceRange(city, priceRange);
	    } else if (town == null || town.isBlank()) {
	        restaurants = restaurantRepository
	                .findByCityAndDistrictAndPriceRange(city, district, priceRange);
	    } else {
	        restaurants = restaurantRepository
	                .findByCityAndDistrictAndTownAndPriceRange(
	                        city, district, town, priceRange);
	    }

	    return restaurants.stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 조회 - 지역 & 가격대 & 음식 카테고리
	public List<RestaurantResponse> findByRegionAndFoodCategoriesAndPriceRange(
	        String city, String district, String town, List<String> categoryNames, String priceRange) {

	    List<Restaurant> restaurants;

	    if (city == null || city.isBlank()) {
	        restaurants = restaurantRepository
	                .findDistinctByPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(priceRange, categoryNames);
	    } else if (district == null || district.isBlank()) {
	        restaurants = restaurantRepository
	                .findDistinctByCityAndPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(city, priceRange, categoryNames);
	    } else if (town == null || town.isBlank()) {
	        restaurants = restaurantRepository
	                .findDistinctByCityAndDistrictAndPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(
	                        city, district, priceRange, categoryNames);
	    } else {
	        restaurants = restaurantRepository
	                .findDistinctByCityAndDistrictAndTownAndPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(
	                        city, district, town, priceRange, categoryNames);
	    }

	    return restaurants.stream()
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// 조회 - 상세 조건 단독 조회
	public List<RestaurantResponse> findByFeatures(List<String> features) {

	    if (features == null || features.isEmpty()) {
	        return findAll();
	    }

	    List<Restaurant> restaurants = restaurantRepository.findDistinctByRestaurantTagsTagName(features.get(0));
	    Set<Long> restaurantIds = new HashSet<>();

	    for (Restaurant restaurant : restaurants) {
	        restaurantIds.add(restaurant.getId());
	    }

	    for (int i = 1; i < features.size(); i++) {
	        List<Restaurant> featureRestaurants = restaurantRepository.findDistinctByRestaurantTagsTagName(features.get(i));
	        Set<Long> featureRestaurantIds = new HashSet<>();

	        for (Restaurant restaurant : featureRestaurants) {
	            featureRestaurantIds.add(restaurant.getId());
	        }

	        // 두 Set에서 공통으로 존재하는 값만 남김
	        restaurantIds.retainAll(featureRestaurantIds);
	    }

	    return restaurantRepository.findAll().stream()
	            .filter(restaurant -> restaurantIds.contains(restaurant.getId()))
	            .map(RestaurantResponse::from)
	            .toList();
	}
	
	
	// restaurants : 지역/음식/가격 조건으로 검색된 결과를 대상으로 상세조건을 적용
	// 상세조건 필터 메서드
	public List<RestaurantResponse> filterByFeatures(List<RestaurantResponse> restaurants, List<String> features) {

	    if (features == null || features.isEmpty()) {
	        return restaurants;
	    }

	    Set<Long> restaurantIds = new HashSet<>();
	    List<Restaurant> firstFeatureRestaurants = restaurantRepository.findDistinctByRestaurantTagsTagName(features.get(0));

	    for (Restaurant restaurant : firstFeatureRestaurants) {
	        restaurantIds.add(restaurant.getId());
	    }

	    for (int i = 1; i < features.size(); i++) {
	        List<Restaurant> featureRestaurants = restaurantRepository.findDistinctByRestaurantTagsTagName(features.get(i));
	        Set<Long> featureRestaurantIds = new HashSet<>();

	        for (Restaurant restaurant : featureRestaurants) {
	            featureRestaurantIds.add(restaurant.getId());
	        }

	        restaurantIds.retainAll(featureRestaurantIds);
	    }

	    return restaurants.stream()
	            .filter(restaurant -> restaurantIds.contains(restaurant.getId()))
	            .toList();
	}
	
	
	// 검색 결과에 평균 평점과 리뷰 수 추가
	public List<RestaurantResponse> applyReviewStats(List<RestaurantResponse> restaurants) {
	    if (restaurants == null || restaurants.isEmpty()) {
	        return restaurants;
	    }

	    List<Long> restaurantIds = restaurants.stream()
	            .map(RestaurantResponse::getId)
	            .toList();

	    Map<Long, ReviewStatsResponse> statsMap = reviewRepository.findStatsByRestaurantIds(restaurantIds)
	            .stream()
	            .collect(Collectors.toMap(ReviewStatsResponse::getRestaurantId, Function.identity()));

	    return restaurants.stream()
	            .map(restaurant -> {
	                ReviewStatsResponse stats = statsMap.get(restaurant.getId());

	                if (stats == null) {
	                    return restaurant.toBuilder()
	                            .averageRating(0.0)
	                            .reviewCount(0L)
	                            .build();
	                }

	                double averageRating = Math.round(stats.getAverageRating() * 10) / 10.0;

	                return restaurant.toBuilder()
	                        .averageRating(averageRating)
	                        .reviewCount(stats.getReviewCount())
	                        .build();
	            })
	            .toList();
	}
	
	
	// 검색 결과에 음식 카테고리와 태그 추가
	public List<RestaurantResponse> applyCategoriesAndTags(List<RestaurantResponse> restaurants) {
	    if (restaurants == null || restaurants.isEmpty()) {
	        return restaurants;
	    }

	    List<Long> restaurantIds = restaurants.stream()
	            .map(RestaurantResponse::getId)
	            .toList();

	    Map<Long, List<String>> categoryMap = restaurantFoodCategoryRepository.findNamesByRestaurantIds(restaurantIds)
	            .stream()
	            .collect(Collectors.groupingBy(
	                    RestaurantLabelResponse::getRestaurantId,
	                    Collectors.mapping(RestaurantLabelResponse::getName, Collectors.toList())
	            ));

	    Map<Long, List<String>> tagMap = restaurantTagRepository.findNamesByRestaurantIds(restaurantIds)
	            .stream()
	            .collect(Collectors.groupingBy(
	                    RestaurantLabelResponse::getRestaurantId,
	                    Collectors.mapping(RestaurantLabelResponse::getName, Collectors.toList())
	            ));

	    return restaurants.stream()
	            .map(restaurant -> restaurant.toBuilder()
	                    .categories(categoryMap.getOrDefault(restaurant.getId(), List.of()))
	                    .tags(tagMap.getOrDefault(restaurant.getId(), List.of()))
	                    .build())
	            .toList();
	}
	
	
	// 맛집 상세 조회 ----------
	public RestaurantResponse findById(Long id) {
	    Restaurant restaurant = restaurantRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("맛집을 찾을 수 없습니다. ID: " + id));

	    RestaurantResponse response = RestaurantResponse.from(restaurant);

	    return applyCategoriesAndTags(List.of(response)).get(0);
	}
	
	
	// 맛집ID로 메뉴 목록 조회 ---
	public List<MenuResponse> findMenusByRestaurantId(Long restaurantId) {
		
		return menuRepository.findByRestaurantId(restaurantId)
				.stream()
				.map(MenuResponse::from)
				.toList();
	}
	
	
	// 리뷰: 여러 건
	public List<ReviewResponse> findReviewsByRestaurantId(Long restaurantId) {

	    return reviewRepository.findByRestaurantId(restaurantId)
	            .stream()
	            .map(ReviewResponse::from)
	            .toList();
	}
	
	
	// 리뷰 평균 평점 계산
	public Double calculateAverageRating(List<ReviewResponse> reviews) {

	    double averageRating = reviews.stream()
	            .filter(review -> review.getRating() != null)
	            .mapToInt(review -> review.getRating())
	            .average()
	            .orElse(0.0);

	    return Math.round(averageRating * 10) / 10.0;
	}
	
	
	// AI 리뷰 요약: 한 건
	public AiReviewSummaryResponse findAiReviewSummaryByRestaurantId(Long restaurantId) {

	    return aiReviewSummaryRepository.findByRestaurantId(restaurantId)
	            .map(AiReviewSummaryResponse::from)
	            .orElse(null);
	}
	
	
}
