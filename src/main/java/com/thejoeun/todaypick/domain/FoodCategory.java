package com.thejoeun.todaypick.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "food_category",
		uniqueConstraints = @UniqueConstraint(name = "uk_food_category_name", columnNames = "name")
	)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "food_category_id")
	private Long id;
	
	@Column(name = "name", nullable = false, length = 30)
	private String name;
	
	@OneToMany(mappedBy = "foodCategory")
	private final List<RestaurantFoodCategory> restaurantFoodCategories = new ArrayList<>();
	
	public FoodCategory(String name) {
		this.name = name;
	}
	
	public void changeName(String name) {
		this.name = name;
	}
	
}
