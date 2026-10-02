package com.thejoeun.todaypick.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thejoeun.todaypick.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	// 맛집 ID로 메뉴 목록 조회
	List<Menu> findByRestaurantId(Long restaurantId);
	
	// 메뉴명 검색
	List<Menu> findByMenuNameContainingIgnoreCase(String keyword);
	
	
}
