package com.thejoeun.todaypick.dto;

import com.thejoeun.todaypick.domain.Menu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponse {
	
    private Long id;
    private String menuName;
    private Integer price;

    // Menu Entity를 MenuResponse DTO로 변환
    public static MenuResponse from(Menu menu) {

        return MenuResponse.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .build();
    }	

}
