document.addEventListener("DOMContentLoaded", () => {
    if (typeof kakao === "undefined") {
        console.error("Kakao Maps SDK를 불러오지 못했습니다.");
        return;
    }

    kakao.maps.load(initMap);
});

function initMap() {
    const mapContainer = document.getElementById("map");
    const restaurantItems = document.querySelectorAll(".map-restaurant-item");

    if (!mapContainer) {
        return;
    }
	
	window.addEventListener("resize", () => {
	    const center = map.getCenter();

	    map.relayout();
	    map.setCenter(center);
	});

/*	// 전체맵 수준으로 시작
    const map = new kakao.maps.Map(mapContainer, {
        center: new kakao.maps.LatLng(37.5665, 126.9780),
        level: 5
    });*/
	
	// 	서울·강남 주변 - 적당한 확대 수준에서 시작
	const map = new kakao.maps.Map(mapContainer, {
	    center: new kakao.maps.LatLng(37.4979, 127.0276),
	    level: 6
	});

    const overlay = new kakao.maps.CustomOverlay({ yAnchor: 1.15, zIndex: 10 });

	const restaurantMarkers = [];

	restaurantItems.forEach((item) => {
	    const latitudeText = item.dataset.latitude;
	    const longitudeText = item.dataset.longitude;

	    if (!latitudeText || !longitudeText) {
	        item.hidden = true;
	        return;
	    }

	    const latitude = Number(latitudeText);
	    const longitude = Number(longitudeText);

	    if (!Number.isFinite(latitude) || !Number.isFinite(longitude)) {
	        item.hidden = true;
	        return;
	    }

	    const position = new kakao.maps.LatLng(latitude, longitude);
	    const marker = new kakao.maps.Marker({ map, position });

	    restaurantMarkers.push({ item, marker, position });

	    kakao.maps.event.addListener(marker, "click", () => {
	        selectRestaurant(item, position);
	        showRestaurantOverlay(item, position);
	    });

	    item.addEventListener("click", (event) => {
	        if (event.target.closest("a, button")) {
	            return;
	        }

	        selectRestaurant(item, position);
	        showRestaurantOverlay(item, position);
	    });
	});
	
	kakao.maps.event.addListener(map, "idle", updateVisibleRestaurants);
	updateVisibleRestaurants();

	function selectRestaurant(item, position) {
	    restaurantItems.forEach((restaurant) => restaurant.classList.remove("active"));

	    item.classList.add("active");
	    item.scrollIntoView({ behavior: "smooth", block: "center" });

	    map.setLevel(3);
	    map.panTo(position);
	}

    function showRestaurantOverlay(item, position) {
        const content = createOverlayContent(item);

        overlay.setContent(content);
        overlay.setPosition(position);
        overlay.setMap(map);
    }

    function createOverlayContent(item) {
        const overlayBox = document.createElement("div");
        overlayBox.className = "restaurant-overlay";

        const closeButton = document.createElement("button");
        closeButton.type = "button";
        closeButton.className = "overlay-close";
        closeButton.textContent = "×";

        closeButton.addEventListener("click", (event) => {
            event.stopPropagation();
            overlay.setMap(null);
        });

        const category = document.createElement("span");
        category.className = "overlay-category";
        category.textContent = item.dataset.category || "맛집";

        const name = document.createElement("h3");
        name.textContent = item.dataset.name;

        const address = document.createElement("p");
        address.textContent = item.dataset.address;

        const reviewInfo = document.createElement("p");
        reviewInfo.className = "overlay-rating";

        const reviewCount = Number(item.dataset.reviewCount);

        if (reviewCount > 0) {
            reviewInfo.textContent = `★ ${item.dataset.rating} (${reviewCount})`;
        } else {
            reviewInfo.textContent = "등록된 리뷰가 없습니다.";
        }

        const detailLink = document.createElement("a");
        detailLink.className = "overlay-detail";
        detailLink.href = item.dataset.detailUrl;
        detailLink.textContent = "상세보기";

        overlayBox.append(closeButton, category, name, address, reviewInfo, detailLink);

        return overlayBox;
    }
	
	function updateVisibleRestaurants() {
	    const bounds = map.getBounds();
	    const resultCount = document.getElementById("mapResultCount");
	    const emptyMessage = document.getElementById("mapEmptyMessage");

	    let visibleCount = 0;

	    restaurantMarkers.forEach(({ item, position }) => {
	        const visible = bounds.contain(position);

	        item.hidden = !visible;

	        if (visible) {
	            visibleCount++;
	        } else {
	            item.classList.remove("active");
	        }
	    });

	    if (resultCount) {
	        resultCount.textContent = visibleCount;
	    }

	    if (emptyMessage) {
	        emptyMessage.hidden = visibleCount > 0;
	    }
	}
	
}