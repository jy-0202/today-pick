# Today Pick

## 조건 기반 맛집 탐색 및 추천 웹 서비스

사용자의 **지역·음식 카테고리·가격대·분위기 등의 조건을 기반으로
음식점을 탐색**하고, 상세 정보와 메뉴·리뷰 및 지도 위치를 확인할 수
있도록 구현한 Spring Boot 기반 개인 프로젝트입니다.

조건 검색과 지도 기반 탐색 기능을 중심으로 구현했으며, 향후 자연어
입력을 기존 조건 검색과 연결하는 AI 추천 기능으로 확장할 수 있도록 기본
구조를 구성했습니다.

-   **개발 기간**: 2026.07 \~ 2026.09
-   **개발 형태**: 개인 프로젝트

------------------------------------------------------------------------

## Tech Stack

![Java](https://img.shields.io/badge/Java_21-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring
Boot](https://img.shields.io/badge/Spring_Boot_3.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Data
JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=thymeleaf&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css3&logoColor=white)
![Kakao
Maps](https://img.shields.io/badge/Kakao_Maps-FFCD00?style=flat-square&logo=kakao&logoColor=black)
![Git](https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)

------------------------------------------------------------------------

## 1. Key Features

### 조건 기반 맛집 검색

-   지역·음식 카테고리·가격대·상세 태그 조건 검색
-   음식점명 및 메뉴명 검색
-   여러 조건을 조합한 음식점 조회

### 음식점 상세 조회

-   음식점 기본 정보 및 주소
-   메뉴 및 가격
-   리뷰
-   평균 평점 및 리뷰 수
-   Kakao Map 기반 위치 확인

### 지도와 음식점 목록 동기화

Kakao Maps JavaScript API를 이용하여 사용자가 지도를 이동하면 **현재
지도 영역에 포함되는 음식점만 사이드바에 표시**하고 결과 개수도 함께
갱신하도록 구현했습니다.

``` text
지도 이동
    ↓
현재 지도 영역(bounds) 확인
    ↓
영역 내부 음식점 판별
    ↓
사이드바 목록 갱신
    ↓
결과 개수 갱신
```

### AI 추천 확장 구조

자연어 기반 추천 기능을 향후 추가할 수 있도록 AI 추천 입력 화면과
Service·DTO 기본 구조를 구성했습니다.

> 현재 실제 AI API 또는 추천 모델 연동은 구현하지 않았으며, 향후
> 자연어에서 추천 조건을 추출하여 기존 조건 검색 기능과 연결하는
> 방향으로 확장할 예정입니다.

------------------------------------------------------------------------

## 2. Application Structure

Spring Boot의 계층 구조를 기준으로 역할을 분리했습니다.

``` text
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL
```

  계층         역할
  ------------ -----------------------------------
  Controller   사용자 요청 처리 및 화면 연결
  Service      음식점 조회 및 비즈니스 로직 처리
  Repository   JPA 기반 데이터베이스 접근
  DTO          계층 및 화면 간 데이터 전달
  Entity       데이터베이스 테이블과 객체 매핑
  Thymeleaf    서버 데이터를 이용한 화면 렌더링
  JavaScript   지도 및 사용자 인터랙션 처리

------------------------------------------------------------------------

## 3. Implementation

### 조건 검색

지역·카테고리·가격대·태그 등 여러 조건을 조합하고 음식점명과 메뉴명도
검색할 수 있도록 구성했습니다.

``` text
사용자 검색 조건
    ↓
Controller
    ↓
Service
    ↓
Repository
    ↓
음식점 조회
    ↓
검색 결과 화면
```

### 음식점·메뉴·리뷰 데이터 연동

음식점 상세 화면에서 음식점 정보와 메뉴·리뷰 데이터를 함께 조회하고,
리뷰 데이터를 이용하여 평균 평점과 리뷰 수를 제공합니다.

### Kakao Map 연동

지도 이동 완료 시 현재 영역을 확인하고 각 음식점 좌표가 해당 영역에
포함되는지를 기준으로 사이드바 목록과 결과 개수를 함께 갱신합니다.

------------------------------------------------------------------------

## 4. Troubleshooting

### 지도 이동 후 사이드바 목록 불일치

**문제**\
지도를 이동하면 현재 영역의 음식점 개수는 변경되었지만 사이드바에는 기존
전체 음식점 목록이 계속 표시되어 지도와 목록의 정보가 일치하지
않았습니다.

**원인**\
지도 영역 변경 후 결과 개수는 갱신하고 있었지만, 사이드바의 음식점
카드에는 동일한 지도 영역 기준이 적용되지 않고 있었습니다.

**해결** - 지도 이동 완료 시 현재 지도 영역 조회 - 음식점 좌표의 영역
포함 여부 확인 - 영역 내부 음식점 카드만 표시 - 동일한 기준으로 결과
개수 갱신

**결과**\
지도 이동 후 현재 화면에 포함된 음식점만 사이드바에 표시되고, 표시된
음식점 수와 결과 개수도 일치하도록 개선했습니다.

이 과정을 통해 **지도 상태와 화면의 검색 결과를 동일한 기준으로 관리하는
것의 중요성**을 확인했습니다.

------------------------------------------------------------------------

## 5. Current Status

### 구현 완료

-   [x] 조건 기반 맛집 검색
-   [x] 음식점명 및 메뉴명 검색
-   [x] 음식점 상세 조회
-   [x] 메뉴·리뷰 조회
-   [x] 평균 평점 및 리뷰 수 표시
-   [x] Kakao Map 연동
-   [x] 지도 이동 범위에 따른 음식점 목록 동기화
-   [x] 지도 영역 기준 결과 개수 갱신
-   [x] AI 추천 화면 및 Service·DTO 기본 구조

### 향후 개선

-   [ ] 현재 위치 기반 주변 음식점 조회
-   [ ] 거리순 음식점 정렬
-   [ ] 지도 이동 범위와 검색 조건을 결합한 탐색
-   [ ] AI API 또는 추천 모델 연동
-   [ ] 자연어 기반 추천 조건 추출

------------------------------------------------------------------------

## 6. What I Learned

이 프로젝트를 통해 다음 내용을 경험했습니다.

-   Spring Boot 기반 웹 애플리케이션 계층 구조 설계
-   JPA를 이용한 MySQL 데이터 연동
-   여러 검색 조건을 이용한 데이터 조회
-   음식점·메뉴·리뷰 관계 데이터의 화면 연동
-   Thymeleaf 기반 서버 사이드 화면 구성
-   JavaScript와 Kakao Maps API를 이용한 지도 기능 구현
-   지도 상태와 화면 목록 간 데이터 동기화
-   기존 검색 기능을 향후 AI 기능과 연결하기 위한 확장 구조 설계

------------------------------------------------------------------------

## Run

**Requirements** - Java 21 - MySQL

프로젝트 실행 전 MySQL 데이터베이스 연결 정보를 설정해야 합니다. DB 접속
정보 등 보안이 필요한 값은 Git에 포함하지 않고 별도 설정 파일로
관리합니다.
