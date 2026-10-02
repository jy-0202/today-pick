package com.thejoeun.todaypick.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thejoeun.todaypick.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	
	// 검색어 조회
	List<Restaurant> findByNameContainingIgnoreCase(String keyword);
	
	// 지역 조회
	// 예) city=서울특별시
	List<Restaurant> findByCity(String city);
	
	// 예) district=강남구
	List<Restaurant> findByCityAndDistrict(String city, String district);
	
	// 예) city = 서울특별시, district = 강남구, town = 역삼동
	List<Restaurant> findByCityAndDistrictAndTown(String city, String district, String town);
	
	@Query("SELECT DISTINCT r.city FROM Restaurant r ORDER BY r.city")
	List<String> findDistinctCities();

	@Query("SELECT DISTINCT r.district FROM Restaurant r WHERE r.city = :city ORDER BY r.district")
	List<String> findDistinctDistrictsByCity(@Param("city") String city);

	@Query("""
	        SELECT DISTINCT r.town FROM Restaurant r
	        WHERE r.city = :city AND r.district = :district AND r.town IS NOT NULL
	        ORDER BY r.town
	        """)
	List<String> findDistinctTownsByCityAndDistrict(@Param("city") String city, @Param("district") String district);
	
	
	
	// 음식 카테고리 조회
	List<Restaurant> findDistinctByRestaurantFoodCategoriesFoodCategoryNameIn(List<String> categoryNames);
	
	
	// 음식 카테고리 & 지역 조회
	// 시/도 + 음식 카테고리
	List<Restaurant> findDistinctByCityAndRestaurantFoodCategoriesFoodCategoryNameIn(
	        String city, List<String> categoryNames);

	// 시/도 + 구/군 + 음식 카테고리
	List<Restaurant> findDistinctByCityAndDistrictAndRestaurantFoodCategoriesFoodCategoryNameIn(
	        String city, String district, List<String> categoryNames);

	// 시/도 + 구/군 + 동/읍/면 + 음식 카테고리
	List<Restaurant> findDistinctByCityAndDistrictAndTownAndRestaurantFoodCategoriesFoodCategoryNameIn(
	        String city, String district, String town, List<String> categoryNames);

	
	// 가격대 조회
	List<Restaurant> findByPriceRange(String priceRange);
	
	// 가격대 + 지역 조회
	// 시/도 + 가격대
	List<Restaurant> findByCityAndPriceRange(String city, String priceRange);

	// 시/도 + 구/군 + 가격대
	List<Restaurant> findByCityAndDistrictAndPriceRange(String city, String district, String priceRange);

	// 시/도 + 구/군 + 동/읍/면 + 가격대
	List<Restaurant> findByCityAndDistrictAndTownAndPriceRange(String city, String district, String town, String priceRange);
	
	
	// (지역X) 가격대 조회 + 음식 카테고리
	List<Restaurant> findDistinctByPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(
			String priceRange, List<String> categoryNames);
	
	// 시/도 + 가격대 + 음식 카테고리
	List<Restaurant> findDistinctByCityAndPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(
			String city, String priceRange, List<String> categoryNames);
	
	// 시/도 + 구/군 + 가격대 + 음식 카테고리
	List<Restaurant> findDistinctByCityAndDistrictAndPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(
	        String city, String district, String priceRange, List<String> categoryNames);
	
	// 시/도 + 구/군 + 동/읍/면 + 가격대 + 음식 카테고리
	List<Restaurant> findDistinctByCityAndDistrictAndTownAndPriceRangeAndRestaurantFoodCategoriesFoodCategoryNameIn(
	        String city, String district, String town, String priceRange, List<String> categoryNames);
	
	
	// 상세 조건 태그 조회
	List<Restaurant> findDistinctByRestaurantTagsTagName(String tagName);
	
	
	
}