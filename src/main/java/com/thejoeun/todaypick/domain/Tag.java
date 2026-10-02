package com.thejoeun.todaypick.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(
	    name = "tag",
	    uniqueConstraints = @UniqueConstraint(
	        name = "uk_tag_name_type",
	        columnNames = {"name", "tag_type"}
	    )
	)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_type", nullable = false, length = 20)
    private TagType tagType;

    @OneToMany(mappedBy = "tag")
    private final List<RestaurantTag> restaurantTags = new ArrayList<>();

    public Tag(String name, TagType tagType) {
        this.name = name;
        this.tagType = tagType;
    }

    public void change(String name, TagType tagType) {
        this.name = name;
        this.tagType = tagType;
    }
	
}
