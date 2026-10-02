//// 메인 검색 ////
function goMainResult() {
    const keywordInput = document.getElementById("mainSearchInput");
    const citySelect = document.getElementById("city");
    const foodTypeSelect = document.getElementById("food_type");
    const priceRangeSelect = document.getElementById("price_range");
    const featureSelect = document.getElementById("features");

    const params = new URLSearchParams();

    // 검색어
    if (keywordInput && keywordInput.value.trim()) {
        params.append("keyword", keywordInput.value.trim());
    }

    // 지역
	if (citySelect && citySelect.value !== "all-city") {
	    params.append("city", citySelect.value);
	}

    // 음식 종류
    if (foodTypeSelect && foodTypeSelect.value !== "all-type") {
        const foodType = foodTypeSelect.options[foodTypeSelect.selectedIndex].text;
        params.append("foodType", foodType);
    }

    // 가격대
    if (priceRangeSelect && priceRangeSelect.value !== "all-price") {
        const priceRange = priceRangeSelect.options[priceRangeSelect.selectedIndex].text;
        params.append("priceRange", priceRange);
    }

    // 상세 조건
    if (featureSelect && featureSelect.value !== "all-features") {
        const feature = featureSelect.options[featureSelect.selectedIndex].text;
        params.append("feature", feature);
    }

    location.href = `/recommend-result?${params.toString()}`;
}


//// AI 추천 ////
/*
function goAiResult() {
    const aiPrompt = document.getElementById("aiPrompt");

    if (!aiPrompt) {
        return;
    }

    const prompt = aiPrompt.value.trim();

    if (!prompt) {
        alert("추천받고 싶은 내용을 입력해주세요.");
        aiPrompt.focus();
        return;
    }

    const params = new URLSearchParams();
    params.append("prompt", prompt);

    location.href = `/ai-recommend-result?${params.toString()}`;
}
*/
/// AI 연결 전까지 임시 연결 ////////////////////

/// AI 연결 전 임시 조건 분석 ///////////////
function buildTemporaryAiParams(prompt) {
    const params = new URLSearchParams();
    let remainingPrompt = prompt;

    // 음식 종류
	const foodTypes = [
	    "카페/디저트",
	    "한식",
	    "중식",
	    "일식",
	    "양식",
	    "분식",
	    "주점"
	];

    foodTypes.forEach((foodType) => {
        if (remainingPrompt.includes(foodType)) {
            params.append("foodType", foodType);
            remainingPrompt = remainingPrompt.replaceAll(foodType, " ");
        }
    });

    // 가격대
    const priceRanges = [
        "1만원 이하",
        "1~2만원",
        "2~3만원",
        "3~5만원",
        "5만원 이상"
    ];

    for (const priceRange of priceRanges) {
        if (remainingPrompt.includes(priceRange)) {
            params.append("priceRange", priceRange);
            remainingPrompt = remainingPrompt.replaceAll(priceRange, " ");
            break;
        }
    }

    // 상세 조건
    const featureMappings = [
        { words: ["비 오는 날", "비오는 날"], value: "비 오는 날" },
        { words: ["데이트"], value: "데이트" },
        { words: ["가성비 좋은 곳", "가성비 맛집", "가성비 좋은", "가성비"], value: "가성비 좋은 곳" },
        { words: ["분위기가 좋은 곳", "분위기 좋은 곳", "분위기 좋은"], value: "분위기 좋은 곳" },
        { words: ["조용한 곳", "조용한"], value: "조용한 곳" },
        { words: ["주차 가능", "주차"], value: "주차 가능" },
        { words: ["단체석 보유", "단체석"], value: "단체석 보유" },
        { words: ["특별한 날"], value: "특별한 날" }
    ];

    const features = new Set();

    featureMappings.forEach((mapping) => {
        mapping.words.forEach((word) => {
            if (remainingPrompt.includes(word)) {
                features.add(mapping.value);
                remainingPrompt = remainingPrompt.replaceAll(word, " ");
            }
        });
    });

    features.forEach((feature) => {
        params.append("feature", feature);
    });

    // 조건으로 처리되지 않은 나머지는 음식점명/메뉴명 검색어로 사용
    remainingPrompt = remainingPrompt.replace(/[,\s]+/g, " ").trim();

    if (remainingPrompt) {
        params.append("keyword", remainingPrompt);
    }

    // 아무 조건도 분석하지 못한 경우 기존 keyword 검색 사용
    if (!params.toString()) {
        params.append("keyword", prompt);
    }

    return params;
}

////

function goAiResult() {
    const aiPrompt = document.getElementById("aiPrompt");

    if (!aiPrompt) {
        return;
    }

    const prompt = aiPrompt.value.trim();

    if (!prompt) {
        alert("추천받고 싶은 내용을 입력해주세요.");
        aiPrompt.focus();
        return;
    }

    saveAiHistory(prompt);

    const params = buildTemporaryAiParams(prompt);

    location.href = `/recommend-result?${params.toString()}`;
}


/// AI페이지 - 예시문장 보기 버튼 ///
const aiExamples = [
    "파스타",
    "떡볶이",
    "삼겹살",
    "라멘",
    "김치찌개"
];

function showAiExample() {
    const aiPrompt = document.getElementById("aiPrompt");

    if (!aiPrompt) {
        return;
    }

    const randomIndex = Math.floor(Math.random() * aiExamples.length);

    aiPrompt.value = aiExamples[randomIndex];
    aiPrompt.focus();
}


function addAiKeyword(keyword) {
    const aiPrompt = document.getElementById("aiPrompt");

    if (!aiPrompt) {
        return;
    }

    const currentValue = aiPrompt.value.trim();

    aiPrompt.value = currentValue
            ? `${currentValue} ${keyword}`
            : keyword;

    aiPrompt.focus();
}


//// 추천 히스토리 ////
function saveAiHistory(prompt) {
    const histories =
            JSON.parse(localStorage.getItem("aiRecommendationHistory")) || [];

    const filteredHistories = histories.filter((history) => {
        return history.prompt !== prompt;
    });

    filteredHistories.unshift({
        prompt: prompt,
        createdAt: new Date().toLocaleDateString("ko-KR")
    });

    const recentHistories = filteredHistories.slice(0, 4);

    localStorage.setItem(
        "aiRecommendationHistory",
        JSON.stringify(recentHistories)
    );
}

////

function renderAiHistory() {
    const historyGrid = document.getElementById("aiHistoryGrid");

    if (!historyGrid) {
        return;
    }

    const histories =
            JSON.parse(localStorage.getItem("aiRecommendationHistory")) || [];

    historyGrid.innerHTML = "";

    if (histories.length === 0) {
        const emptyMessage = document.createElement("p");
        emptyMessage.className = "history-empty";
        emptyMessage.textContent = "최근 추천 내역이 없습니다.";

        historyGrid.appendChild(emptyMessage);

        return;
    }

    histories.forEach((history) => {
        const historyCard = document.createElement("button");

        historyCard.type = "button";
        historyCard.className = "history-card";

        const prompt = document.createElement("strong");
        prompt.textContent = history.prompt;

        const createdAt = document.createElement("span");
        createdAt.textContent = history.createdAt;

        historyCard.appendChild(prompt);
        historyCard.appendChild(createdAt);

        historyCard.addEventListener("click", () => {
            const aiPrompt = document.getElementById("aiPrompt");

            if (!aiPrompt) {
                return;
            }

            aiPrompt.value = history.prompt;
            aiPrompt.focus();
        });

        historyGrid.appendChild(historyCard);
    });
}

////



//// 조건 추천 ////
function goResult() {

	// 지역 선택 요소
    const citySelect = document.getElementById("city");
    const districtSelect = document.getElementById("district");
    const townSelect = document.getElementById("town");

    const params = new URLSearchParams();


	//// 지역 ////////////////////////////////////////
	// 시/도
	if (citySelect && citySelect.value !== "all-city") {
	    const city = citySelect.options[citySelect.selectedIndex].text;
	    params.append("city", city);
	}


	// 구/군
	if (districtSelect && districtSelect.value !== "all-district") {
	    const district = districtSelect.options[districtSelect.selectedIndex].text;
	    params.append("district", district);
	}


	// 동/읍/면
	if (townSelect && townSelect.value !== "all-town") {
	    const town = townSelect.options[townSelect.selectedIndex].text;
	    params.append("town", town);
	}

	
	//// 음식 종류 ////////////
	const selectedFoodTypes = document.querySelectorAll("#food-type-group .select-chip.active");
	
    selectedFoodTypes.forEach((button) => {
        const foodType = button.textContent.trim();

        if (foodType !== "전체") {
            params.append("foodType", foodType);
        }
    });
	
	
	//// 가격대 //////////
	const selectedPriceRange =
		document.querySelector("#price-range-group .select-chip.active");

	if (selectedPriceRange) {
		const priceRange = selectedPriceRange.textContent.trim();

		if (priceRange !== "전체") {
			params.append("priceRange", priceRange);
		}
	}


	//// 상세 조건 ///////////
	const selectedFeatures = document.querySelectorAll("#feature-group .select-chip.active");

	selectedFeatures.forEach((button) => {
	    params.append("feature", button.dataset.value);
	});

	
	

    location.href = `/recommend-result?${params.toString()}`;
}


function goCategoryResult(category) {
    // 인기 카테고리 바로가기용
}


document.addEventListener("DOMContentLoaded", () => {

    /* =========================
     * DOM 요소 선택
    ========================= */

    // 초기화 버튼
    const resetBtn = document.getElementById("resetBtn");
    const mainSearchInput = document.getElementById("mainSearchInput");

	// 
	const foodTypeSelect = document.getElementById("food_type");
	const priceRangeSelect = document.getElementById("price_range");
	const featureSelect = document.getElementById("features");
	
    // 지역 선택
    const citySelect = document.getElementById("city");
    const districtSelect = document.getElementById("district");
    const townSelect = document.getElementById("town");

	
	/* =======================================================
	* AI 추천 페이지 [전체 삭제 기능]
	======================================================== */
	const clearAiHistoryBtn =
	        document.getElementById("clearAiHistoryBtn");

	if (clearAiHistoryBtn) {
	    clearAiHistoryBtn.addEventListener("click", () => {
	        localStorage.removeItem("aiRecommendationHistory");
	        renderAiHistory();
	    });
	}

	renderAiHistory();
	
	
	/* =======================================================
	 * [↻ 초기화 버튼 기능]
	======================================================= */
	if (resetBtn) {
	    resetBtn.addEventListener("click", () => {

	        //// 메인 페이지 //////////////////////////////

	        // 검색어
	        if (mainSearchInput) {
	            mainSearchInput.value = "";
	        }

	        // 지역
	        if (citySelect) {
	            citySelect.value = "all-city";
	        }

	        // 음식 종류
	        if (foodTypeSelect) {
	            foodTypeSelect.value = "all-type";
	        }

	        // 가격대
	        if (priceRangeSelect) {
	            priceRangeSelect.value = "all-price";
	        }

	        // 상세 조건
	        if (featureSelect) {
	            featureSelect.value = "all-features";
	        }


	        //// 조건 추천 페이지 /////////////////////////

	        // 구/군
	        if (districtSelect) {
	            districtSelect.innerHTML =
	                    '<option value="all-district">구/군 선택</option>';
	        }

	        // 동/읍/면
	        if (townSelect) {
	            townSelect.innerHTML =
	                    '<option value="all-town">동/읍/면 선택</option>';
	        }

	        // 음식 종류
	        const foodTypeButtons =
	                document.querySelectorAll("#food-type-group .select-chip");

	        foodTypeButtons.forEach((button) => {
	            button.classList.remove("active");
	        });

	        if (foodTypeButtons.length > 0) {
	            foodTypeButtons[0].classList.add("active");
	        }

	        // 가격대
	        const priceRangeButtons =
	                document.querySelectorAll("#price-range-group .select-chip");

	        priceRangeButtons.forEach((button) => {
	            button.classList.remove("active");
	        });

	        if (priceRangeButtons.length > 0) {
	            priceRangeButtons[0].classList.add("active");
	        }

/*
	        // 인원
	        const peopleCountButtons =
	                document.querySelectorAll("#people-count-group .select-chip");

	        peopleCountButtons.forEach((button) => {
	            button.classList.remove("active");
	        });

	        if (peopleCountButtons.length > 0) {
	            peopleCountButtons[0].classList.add("active");
	        }
			*/

	        // 상세 조건
	        const featureButtons =
	                document.querySelectorAll("#feature-group .select-chip");

	        featureButtons.forEach((button) => {
	            button.classList.remove("active");
	        });
	    });
	}


    /*  document.querySelectorAll(".chip, .select-chip").forEach((btn) => {
        btn.addEventListener("click", () => {
          if (btn.classList.contains("select-chip")) {
            btn.classList.toggle("active");
          }
        });
      });*/


    // 단일 선택
    const setSingleSelect = (groupId) => {
        const buttons = document.querySelectorAll(`#${groupId} .select-chip`);

        buttons.forEach((btn) => {
            btn.addEventListener("click", () => {
                buttons.forEach((button) => {
                    button.classList.remove("active");
                });
                btn.classList.add("active");
            });
        });
    };

    setSingleSelect("price-range-group");
	// 음식점 수용 인원 데이터 추가 후 활성화
	// setSingleSelect("people-count-group");



    //// 음식 종류 - 복수 선택 //////////////////////
    const foodTypeButtons = document.querySelectorAll("#food-type-group .select-chip");

    foodTypeButtons.forEach((btn) => {
        btn.addEventListener("click", () => {

            // 전체 버튼 클릭
            if (btn.textContent.trim() === "전체") {
                foodTypeButtons.forEach((button) => {
                    button.classList.remove("active");
                });

                btn.classList.add("active");

                return;
            }

            // 음식 종류 선택/해제
            btn.classList.toggle("active");

            // 다른 음식 종류를 선택하면 "전체" 선택 해제
            const allButton = foodTypeButtons[0];
            allButton.classList.remove("active");

            // 아무 음식도 선택되지 않았으면 "전체" 선택
            const selectedButtons = document.querySelectorAll("#food-type-group .select-chip.active");

            if (selectedButtons.length === 0) {
                allButton.classList.add("active");
            }

        });
    });
	
	
	//// 상세 조건 - 복수 선택 ////////////////
    const featureButtons = document.querySelectorAll("#feature-group .select-chip");
	
    featureButtons.forEach((btn) => {
		btn.addEventListener("click", () => {
			btn.classList.toggle("active");
        });
    });
	





	//// 맛집 상세 탭 ////
	const tabs = document.querySelectorAll(".tabs button");
	const tabContents = document.querySelectorAll(".tab-content");

	tabs.forEach((tab) => {
	    tab.addEventListener("click", () => {
	        tabs.forEach((button) => {
	            button.classList.remove("active");
	        });

	        tabContents.forEach((content) => {
	            content.classList.remove("active");
	        });

	        tab.classList.add("active");

	        const target = document.getElementById(tab.dataset.tab);

	        if (target) {
	            target.classList.add("active");
	        }
	    });
	});


    /* =======================================================
     * [📍 지역 선택 동적 제어 기능]
    ======================================================= */
	// 지역 선택 - DB 값 자체를 사용
	if (citySelect && districtSelect && townSelect) {
	    citySelect.addEventListener("change", async function() {
	        districtSelect.innerHTML = '<option value="all-district">구/군 선택</option>';
	        townSelect.innerHTML = '<option value="all-town">동/읍/면 선택</option>';

	        if (this.value === "all-city") {
	            return;
	        }

	        const response = await fetch(`/api/regions/districts?city=${encodeURIComponent(this.value)}`);
	        const districts = await response.json();

	        districts.forEach((district) => {
	            const option = document.createElement("option");
	            option.value = district;
	            option.textContent = district;
	            districtSelect.appendChild(option);
	        });
	    });

	    districtSelect.addEventListener("change", async function() {
	        townSelect.innerHTML = '<option value="all-town">동/읍/면 선택</option>';

	        if (this.value === "all-district") {
	            return;
	        }

	        const city = citySelect.value;
	        const response = await fetch(`/api/regions/towns?city=${encodeURIComponent(city)}&district=${encodeURIComponent(this.value)}`);
	        const towns = await response.json();

	        towns.forEach((town) => {
	            const option = document.createElement("option");
	            option.value = town;
	            option.textContent = town;
	            townSelect.appendChild(option);
	        });
	    });
	}
	
	/*
    // 대한민국 대표 행정구역 데이터 구조 정의
    const regionData = {
        seoul: {
            districts: {
                gangnam: { name: "강남구", towns: ["역삼동", "삼성동", "신사동", "청담동"] },
                mapo: { name: "마포구", towns: ["연남동", "망원동", "서교동", "공덕동"] },
                seocho: { name: "서초구", towns: ["서초동", "반포동", "방배동", "양재동"] },
                yongsan: { name: "용산구", towns: ["이태원동", "한남동", "원효로동", "한강로동"] },
                jongno: { name: "종로구", towns: ["관철동", "삼청동", "혜화동", "인사동", "명륜동"] },
                jung: { name: "중구", towns: ["명동", "을지로동", "신당동", "회현동"] }
            }
        },
        incheon: {
            districts: {
                yeonsu: { name: "연수구", towns: ["송도동", "연수동", "동춘동"] },
                bupyeong: { name: "부평구", towns: ["부평동", "산곡동", "삼산동"] },
                namdong: { name: "남동구", towns: ["구월동", "간석동", "논현동"] }
            }
        },
        busan: {
            districts: {
                haeundae: { name: "해운대구", towns: ["우동", "중동", "좌동", "재송동"] },
                busanjin: { name: "부산진구", towns: ["부전동", "전포동", "양정동"] },
                suyeong: { name: "수영구", towns: ["광안동", "남천동", "망미동"] },
                saha: { name: "사하구", towns: ["하단동", "괴정동", "다대동"] }
            }
        },
        daegu: {
            districts: {
                jung: { name: "중구", towns: ["동성로동", "삼덕동", "대봉동"] },
                suseong: { name: "수성구", towns: ["범어동", "만촌동", "두산동"] },
                dalseo: { name: "달서구", towns: ["상인동", "월성동", "신당동"] }
            }
        },
        daejeon: {
            districts: {
                seogu: { name: "서구", towns: ["둔산동", "탄방동", "갈마동"] },
                yuseong: { name: "유성구", towns: ["궁동", "봉명동", "노은동"] }
            }
        },
        gwangju: {
            districts: {
                donggu: { name: "동구", towns: ["충장동", "동명동", "서석동"] },
                seogu: { name: "서구", towns: ["치평동", "화정동", "상무동"] }
            }
        },
        ulsan: {
            districts: {
                namgu: { name: "남구", towns: ["삼산동", "달동", "무거동"] },
                jung: { name: "중구", towns: ["성남동", "우정동", "태화동"] }
            }
        },
        sejong: {
            districts: {
                sejong: { name: "세종시", towns: ["보람동", "나성동", "도담동", "어진동"] }
            }
        },
        gyeonggi: {
            districts: {
                suwon: { name: "수원시", towns: ["인계동", "행궁동", "영통동", "광교동"] },
                seongnam: { name: "성남시 분당구", towns: ["삼평동", "정자동", "서현동", "야탑동"] },
                goyang: { name: "고양시 일산동구", towns: ["장항동", "마두동", "백석동"] },
                yongin: { name: "용인시 기흥구", towns: ["보정동", "신갈동", "구갈동"] }
            }
        },
        jeju: {
            districts: {
                jeju: { name: "제주시", towns: ["노형동", "연동", "이도동", "애월읍"] },
                seogwipo: { name: "서귀포시", towns: ["서귀동", "대륜동", "중문동", "성산읍"] }
            }
        }
    };

    // 시/도 변경 이벤트
    // 요소들이 존재할 때만 이벤트 바인딩 실행 (안전 처리)
    if (citySelect && districtSelect && townSelect) {
        // [이벤트 A] 시/도 변경 -> 구/군 동적 생성
        citySelect.addEventListener("change", function() {
            const selectedCity = this.value;

            districtSelect.innerHTML = '<option value="all-district">구/군 선택</option>';
            townSelect.innerHTML = '<option value="all-town">동/읍/면 선택</option>';

            if (regionData[selectedCity] && regionData[selectedCity].districts) {
                const districts = regionData[selectedCity].districts;

                for (const [key, value] of Object.entries(districts)) {
                    const option = document.createElement("option");
                    option.value = key;
                    option.textContent = value.name;
                    districtSelect.appendChild(option);
                }
            }
        });
        // [이벤트 B] 구/군 변경 -> 동/읍/면 동적 생성
        districtSelect.addEventListener("change", function() {
            const selectedCity = citySelect.value;
            const selectedDistrict = this.value;

            // 동/읍/면 선택창 초기화
            townSelect.innerHTML = '<option value="all-town">동/읍/면 선택</option>';

            // 데이터 존재 여부 안전하게 검사 후 옵션 생성
            if (
                regionData[selectedCity] &&
                regionData[selectedCity].districts[selectedDistrict]
            ) {
                const towns = regionData[selectedCity].districts[selectedDistrict].towns;

                towns.forEach(townName => {
                    const option = document.createElement("option");
                    // 한글 이름(예: 역삼동)을 value와 보여지는 텍스트에 모두 적용합니다.
                    option.value = townName;
                    option.textContent = townName;
                    townSelect.appendChild(option);
                });
            }
        });

    }

	*/

}); // DOMContentLoaded 끝



