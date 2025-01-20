/**
 * @file search_filter.js
 * @doc 검색 필터 기능
 */

$(function () {
  $(".show-detail").click(function () {
    location.href = "/goodsDetail";
  })

  // URL에서 쿼리 파라미터 읽기
  const urlParams = new URLSearchParams(window.location.search);
  const currentSort = urlParams.get("sort") || "latest"; // 기본값: 최신순
  const currentSize = urlParams.get("size") || "20"; // 기본값: 20개씩 보기

  // 드롭다운 버튼 텍스트 설정
  const sortTextMap = {
    latest: "최신순",
    sales: "판매량순",
    highPrice: "높은가격순",
    lowPrice: "낮은가격순",
  };
  const sizeTextMap = {
    "20": "20개씩 보기",
    "50": "50개씩 보기",
  };

  $("#dropdownMenu").text(sortTextMap[currentSort] || "최신순");
  $("#dropdownView").text(sizeTextMap[currentSize] || "20개씩 보기");

  // 드롭다운 아이템 클릭 이벤트
  $(".dropdown-item").click(function () {
    const selectedSort = $(this).attr("data-sort") || "latest";
    const selectedText = $(this).text();
    $("#dropdownMenu").text(selectedText);

    urlParams.set("sort", selectedSort);
    urlParams.set("page", "1"); // 페이지를 1로 초기화
    window.location.search = urlParams.toString(); // 페이지 새로고침
  });

  $(".dropdown-view").click(function () {
    const selectedSize = $(this).attr("data-size") || "20";
    const selectedText = $(this).text();
    $("#dropdownView").text(selectedText);

    urlParams.set("size", selectedSize);
    urlParams.set("page", "1"); // 페이지를 1로 초기화
    window.location.search = urlParams.toString(); // 페이지 새로고침
  });

  // 페이지 네이션 액션
  $(".pagination .page-link").click(function (event) {
    event.preventDefault(); // 기본 동작 방지

    const selectedPage = $(this).attr("data-page") || "1"; // 기본값 설정
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set("page", selectedPage);

    window.location.search = urlParams.toString(); // 페이지 새로고침
  });
})

// 카테고리 버튼 동작
$(function () {
  $("#category").click(function () {
    const category = $(this).find(".category");
    const closeIcon = $(this).find(".close-icon");
    if (category.hasClass("open")) {
      // 햄버거 버튼으로 복귀
      category.removeClass("open");
      closeIcon.addClass("d-none");
      category.removeClass("d-none");
      $(this).css("background-color", "white");
      $("#categoryView").hide(); // 카테고리 전체보기 숨기기
    } else {
      // X 버튼으로 전환
      category.addClass("open");
      closeIcon.removeClass("d-none");
      category.addClass("d-none");
      $(this).css("background-color", "black");
      $("#categoryView").show(); // 카테고리 전체보기 보이기
    }
  })

  // 찜 버튼 클릭 시
  $(".heart").click(function () {
    // CSRF 토큰
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute(
        'content');
    const csrfHeader = document.querySelector(
        'meta[name="_csrf_header"]').getAttribute('content');

    const goodsNo = $(this).data("id");
    const heartIcon = $(this);

    $.ajax({
      url: "/wish/wishlist",
      type: "POST",
      headers: {[csrfHeader]: csrfToken}, // CSRF 사용시 헤더에 추가
      data: {goodsNo: goodsNo},
      dataType: "json",
      success: function (response) {
        if (response.status === "added") {
          alert("찜 설정 되었습니다.");
          heartIcon.attr("src", "/images/user/redHeart.png");
        } else if (response.status === "removed") {
          alert("찜 해제 되었습니다.");
          heartIcon.attr("src", "/images/user/heart.png");
        }
      },
      error: function (xhr, status, error) {
        if (status === "parsererror") {
          location.href = '/member/login'
        }
      }
    });
  });

  // 장바구니 버튼 클릭 시
  $(".cart").click(function () {
    // CSRF 토큰
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute(
        'content');
    const csrfHeader = document.querySelector(
        'meta[name="_csrf_header"]').getAttribute('content');

    const goodsNo = $(this).data("id");

    $.ajax({
      url: "/cart/cartList",
      type: "POST",
      // CSRF 토큰 헤더 설정
      headers: {[csrfHeader]: csrfToken},
      data: {goodsNo: goodsNo},
      dataType: "json",
      success: function (response) {
        // response.status 가 "added" 또는 "updated"
        if (response.status === "added") {
          const moveConfirm = confirm("상품이 장바구니에 담겼습니다. 장바구니로 이동하시겠습니까?");
          if (moveConfirm) {
            location.href = "/member/cart";
          }
        } else if (response.status === "updated") {
          const moveConfirm = confirm(
              "장바구니에 이미 담은 상품이 있어 수량이 추가 되었습니다. 장바구니로 이동하시겠습니까?");
          if (moveConfirm) {
            location.href = "/member/cart";
          }
        }
      },
      error: function (xhr, status, error) {
        if (status === "parsererror") {
          location.href = '/member/login'
        }
      }
    });
  });

  $(".buy-now-btn").click(function () {
    const goodsNo = $(this).data("id");

    // 바로구매 페이지로 이동
    location.href = `/payments?goodsNo=${goodsNo}`;
  })
});

