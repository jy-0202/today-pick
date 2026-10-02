package com.thejoeun.todaypick.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RestaurantTagId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "tag_id")
    private Long tagId;
}