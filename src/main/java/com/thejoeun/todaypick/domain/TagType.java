package com.thejoeun.todaypick.domain;

/**
 * 맛집 검색 및 추천에 사용하는 태그 유형입니다.
 * DB의 tag.tag_type 값과 이름이 동일해야 합니다.
 */

public enum TagType {

    WEATHER,
    PURPOSE,
    MOOD,
    FACILITY,
    FEATURE,
    TIME,
    COMPANION
    
}