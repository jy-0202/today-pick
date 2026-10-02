package com.thejoeun.todaypick.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thejoeun.todaypick.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{

}
