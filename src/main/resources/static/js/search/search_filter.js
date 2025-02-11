/**
 * @file search_filter.js
 * @doc 검색 필터 기능
 */

/* TODO : 에러 핸들링 부족
*/
$(function () {
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
    name: "상품명순"
  };
  const sizeTextMap = {
    "20": "20개씩 보기",
    "50": "50개씩 보기",
    "100": "100개씩 보기"
  };

  $("#dropdownMenu").text(sortTextMap[currentSort] || "최신순");
  $("#dropdownView").text(sizeTextMap[currentSize] || "20개씩 보기");

  // 드롭다운 아이템 클릭 이벤트
  $(".dropdown-item").click(function (event) {
    event.preventDefault();
    const selectedSort = $(this).attr("data-sort") || "latest";
    const selectedText = $(this).text();
    $("#dropdownMenu").text(selectedText);

    updateQueryString("sort", selectedSort);
    updateQueryString("page", "1"); // 페이지를 1로 초기화
    fetchSearchResults();
  });

  $(".dropdown-view").click(function (event) {
    event.preventDefault();
    const selectedSize = $(this).attr("data-size") || "20";
    const selectedText = $(this).text();
    $("#dropdownView").text(selectedText);

    updateQueryString("size", selectedSize);
    updateQueryString("page", "1"); // 페이지를 1로 초기화
    fetchSearchResults();
  });

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

  /**
   * URL 쿼리스트링을 업데이트합니다.
   * @param {String} param - 쿼리 파라미터 이름
   * @param {String} value - 설정할 값 (빈 문자열이나 null 이면 해당 파라미터 삭제)
   * @returns {URLSearchParams} 업데이트된 URLSearchParams 객체
   */
  function updateQueryString(param, value) {
    const urlParams = new URLSearchParams(window.location.search);

    // value 가 null 이나 undefined 가 아니면 문자열로 변환 후 trim, 그렇지 않으면 빈 문자열 처리
    if (value != null && String(value).trim() !== "") {
      urlParams.set(param, value);
    } else {
      urlParams.delete(param);
    }

    // URL 업데이트 (페이지 리로딩 없이)
    const newUrl = window.location.pathname + '?' + urlParams.toString();
    history.pushState(null, '', newUrl);

    return urlParams;
  }

  /**
   * 현재 URL의 쿼리스트링을 기반으로 Ajax GET 요청을 보내 검색 결과를 업데이트합니다.
   */
  function fetchSearchResults() {
    const urlParams = new URLSearchParams(window.location.search);
    $.ajax({
      url: '/search/refineAjax',       // 검색 엔드포인트 (필요에 따라 변경)
      type: 'GET',
      data: urlParams.toString(), // 쿼리스트링 데이터를 그대로 전송 (ex: category=12&sort=latest&...)
      dataType: 'json',
      success: function (response) {
        renderResults(response);
      },
      error: function (xhr, status, error) {
        console.error('검색 결과 불러오기 실패:', error);
      }
    });
  }

  /**
   * Ajax 응답으로 받은 데이터를 기반으로 결과 영역을 갱신합니다.
   * (예시에서는 기존 <ul class="prod_list"> 영역을 지우고 새로운 항목들을 추가)
   */
  function renderResults(data) {
    // 예시: 헤더 업데이트
    $('#resultKeyword').text(
        "'" + (new URLSearchParams(window.location.search)).get('keyword')
        + "' 검색 결과");
    $('.result_count b').text(data.totalCount);

    // 결과 리스트 업데이트 (기존 내용 지우기)
    const $prodList = $('.prod_list');
    $prodList.empty();

    if (!data.searchResults || data.searchResults.length === 0) {
      let keyword = (new URLSearchParams(window.location.search)).get('keyword') || '';
      $('.title_size_lg .title_heading').html(
          `<span style="color:#1e5f1e">'<span class="search_value">${keyword}</span>'</span>
       <span>상품 검색 결과가 없습니다.</span>`
      );
      $prodList.append('<li>검색 결과가 없습니다.</li>');
      // 페이지네이션 영역 숨기기
      $('#pagination').hide();
      return;
    } else {
      let keyword = (new URLSearchParams(window.location.search)).get('keyword') || '';
      let totalGoodsCount = $('#totalGoodsCount').val();
      $('.title_size_lg .title_heading').html(
          `<span style="color:#1e5f1e">'<span class="search_value">${keyword}</span>'</span>
       <span>에 대한 ${totalGoodsCount}개의 검색 결과</span>`
      );
      // 페이지네이션 영역 보이기 (검색 결과가 있을 때)
      $('#pagination').show();
    }

    // 검색 결과가 있을 경우 각 항목을 추가
    $.each(data.searchResults, function (index, goods) {
      const listItem = `
        <li class="prod_item d-flex justify-content-between align-items-start">
          <div class="prod_area d-flex align-items-start mb-5">
            <a href="/goods/GoodsDetail/${goods.goodsNo}" class="show-detail">
              <div class="img-box">
                <img src="${goods.mainImage ? goods.mainImage.goodsUrl + '/'
          + goods.mainImage.goodsImageName : '/images/user/book/img.png'}"
                     style="width:200px; height:280px;"/>
              </div>
            </a>
            <div class="item-info ms-3">
              <a href="/goods/GoodsDetail/${goods.goodsNo}" class="show-detail">
                <span class="item-name">${goods.goodsName}</span>
              </a>
              <div class="prod_desc_info">
                <span>${goods.goodsContent}</span>
              </div>
              <div class="prod_author_info">
                <span class="item-author">${goods.goodsWriter}</span>
                <span class="type">저자(글)</span>
                <div class="prod_publish">
                  ${goods.companyName
          ? `<span class="prod_publish">${goods.companyName}</span>`
          : '<span class="prod_publish">출판사 없음</span>'}
                  <span class="gap"> · </span>                
                  <span class="date">${formatDate(goods.goodsCreateAt)}</span>
                </div>
                <div class="prod_price">
                  <span class="val">${formatPrice(goods.goodsPrice)}</span>
                  <span class="unit">원</span>
                </div>
              </div>
            </div>
          </div>
          <div class="prod_btn_wrap d-flex flex-column align-items-end" style="flex-shrink: 0;">
            <div class="icon ms-auto mb-4">
              <!-- 찜 상태에 따른 하트 아이콘 -->
              <img src="${goods.wishStatus === 'Y' ? '/images/user/redHeart.png' : '/images/user/heart.png'}"
                          class="heart me-2"
                          data-id="${goods.goodsNo}"
                          width="36"
                          height="36"
                          alt="찜">
              <img src="/images/user/cart.png" class="cart me-2" data-id="${goods.goodsNo}"
                   width="36" height="36" alt="장바구니">
            </div>
            <!-- 바로구매 버튼 -->
            <button class="btn btn-primary buy-now-btn" data-id="${goods.goodsNo}">바로구매</button>
          </div>
        </li>
      `;
      $prodList.append(listItem);
    });

    // 필요 시 페이징 렌더링 등 추가 처리
    renderPagination(data);
  }

  // 날짜 및 가격 포맷 함수 (필요 시 수정)
  function formatDate(dateStr) {
    const date = new Date(dateStr);
    const options = {year: 'numeric', month: 'long', day: 'numeric'};
    return date.toLocaleDateString('ko-KR', options);
  }

  function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  function renderPagination(data) {
    const pagination = $('#pagination ul');
    pagination.empty();

    const currentPage = data.currentPage;
    const totalPages = data.totalPages;
    const startPage = data.startPage;
    const endPage = data.endPage;

    // 이전 페이지 버튼
    const prevClass = (currentPage <= 1) ? 'disabled' : '';
    const prevPage = currentPage - 1;
    pagination.append(`<li class="page-item ${prevClass}"><a class="page-link" href="#" data-page="${prevPage}">&lt;</a></li>`);

    // 페이지 번호 버튼들
    for (let i = startPage; i <= endPage; i++) {
      const activeClass = (currentPage === i) ? 'active' : '';
      pagination.append(`<li class="page-item ${activeClass}"><a class="page-link" href="#" data-page="${i}">${i}</a></li>`);
    }

    // 다음 페이지 버튼
    const nextClass = (currentPage >= totalPages) ? 'disabled' : '';
    const nextPage = currentPage + 1;
    pagination.append(`<li class="page-item ${nextClass}"><a class="page-link" href="#" data-page="${nextPage}">&gt;</a></li>`);
  }

  /* 이벤트 핸들러 */

  // 카테고리 클릭 이벤트 (예: 카테고리 목록의 각 항목에 .category-item 클래스와 value 속성이 있다고 가정)
  $('.category-item').on('click', function (e) {
    e.preventDefault();
    const catNo = $(this).val();
    updateQueryString('cateNo', catNo);
    fetchSearchResults();
  });

  // 페이징 버튼 클릭 이벤트 (이벤트 위임 방식)
  $(document).on('click', '#pagination .page-link', function (e) {
    e.preventDefault();
    const page = $(this).data('page');
    // 부모 li가 disabled 가 아니라면
    if (page && !$(this).parent().hasClass('disabled')) {
      updateQueryString('page', page);
      fetchSearchResults();
    }
  });

  // 브라우저 뒤로/앞으로 이동 시 처리
  window.onpopstate = function () {
    // URL의 쿼리스트링에 따라 UI(필터, 정렬, 카테고리 등)를 재설정하는 로직을 추가할 수 있음.
    // 여기서는 단순히 Ajax 요청을 재실행합니다.
    fetchSearchResults();
  };

  // depth1 버튼 클릭 시 해당 filter_cont_box 토글
  $('.btn_filter_depth1').on('click', function () {
    // 버튼에 active 클래스를 토글하여 스타일 변경
    $(this).toggleClass('active');
    // 같은 depth1 메뉴 내의 filter_cont_box를 slideToggle으로 보이거나 숨김
    $(this).closest('.menu_item.item_depth1').find(
        '.filter_cont_box').slideToggle();
  });

  // 필터 초기화 버튼 클릭 이벤트 핸들러
  $(".btn_reset").click(function (e) {
    e.preventDefault();

    // 현재 쿼리스트링에서 keyword 값만 유지하고 나머지는 제거
    const currentParams = new URLSearchParams(window.location.search);
    const keyword = currentParams.get("keyword"); // keyword는 유지

    // 새로운 URLSearchParams 생성 (keyword만 포함)
    const newParams = new URLSearchParams();
    if (keyword) {
      newParams.set("keyword", keyword);
    }

    // URL 업데이트 (페이지 리로딩 없이)
    const newUrl = window.location.pathname + "?" + newParams.toString();
    history.pushState(null, "", newUrl);

    // 드롭다운 메뉴 초기화 (기본값: 최신순, 20개씩 보기)
    $("#dropdownMenu").text(sortTextMap["latest"] || "최신순");
    $("#dropdownView").text(sizeTextMap["20"] || "20개씩 보기");

    // 검색조건 체크박스 초기화 (예: 상품명, 저자/역자, 출판사)
    $("input[name='searchCondition']").prop("checked", false);

    // 가격 필터 라디오 초기화
    $("input[name='filterPrice']").prop("checked", false);

    // 발행일 필터 초기화 (기본값: 전체 선택)
    $("input[name='filterRlseDateRdo']").prop("checked", false);
    $("#filterRlseDate_ALL").prop("checked", true);

    // 카테고리 필터 초기화 (선택된 카테고리 active 클래스 제거)
    $(".category-item").removeClass("active");

    // 가격 범위 텍스트 입력 필드 초기화
    $("#saprminFilter").val("");
    $("#saprmaxFilter").val("");

    // 확장된 필터 UI 초기화
    // 모든 btn_filter_depth1 버튼에서 active 클래스를 제거하고, 모든 filter_cont_box 숨김하되,
    // "검색조건" 영역은 제외 (검색조건 영역은 기본 active 상태로 유지)
    $(".btn_filter_depth1").not(function(){
      return $.trim($(this).text()) === "검색조건";
    }).removeClass("active");

    $(".filter_cont_box").not(function(){
      return $.trim($(this).siblings(".btn_filter_depth1").text()) === "검색조건";
    }).hide();

    // "검색조건" 영역은 항상 active 상태와 보임 상태로 유지
    $(".btn_filter_depth1").filter(function(){
      return $.trim($(this).text()) === "검색조건";
    }).addClass("active").siblings(".filter_cont_box").show();

    // 재검색 입력 필드 초기화
    $("#reKeyword").val("");

    // Ajax를 통해 초기화된 필터 기준 검색 결과 업데이트 호출
    fetchSearchResults();
  });

  // [1] 검색조건 체크박스 (상품명, 저자/역자, 출판사)
  $("input[name='searchCondition']").on("change", function () {
    // 체크박스의 value가 쿼리 파라미터의 이름이 됨 (예: "cname", "chrc", "pbcm")
    const paramKey = $(this).val();
    // URL의 쿼리스트링에서 항상 존재하는 keyword 값을 읽어옴
    const urlParams = new URLSearchParams(window.location.search);
    const currentKeyword = urlParams.get("keyword") || "";

    if ($(this).is(":checked")) {
      // 체크되면 해당 파라미터를 keyword 값과 동일하게 설정
      updateQueryString(paramKey, currentKeyword);
    } else {
      // 체크 해제 시 해당 파라미터 삭제
      updateQueryString(paramKey, "");
    }
    // 조건 변경 시 페이지 번호를 1로 초기화 후 결과 업데이트
    updateQueryString("page", "1");
    fetchSearchResults();
  });

  // [2] 가격 필터 라디오 버튼 (preset 값: "0~1", "1~3", "3~10", "10~99999999")
  $("input[name='filterPrice']").on("change", function () {
    const value = $(this).val();
    if (value) {
      // 값이 "최소~최대" 형태이므로 분리해서 각각 saprMin, saprMax 파라미터 업데이트
      const range = value.split("~");
      if (range.length === 2) {
        updateQueryString("saprMin", range[0]);
        updateQueryString("saprMax", range[1]);
      }
    } else {
      // 값이 없으면 두 파라미터 삭제
      updateQueryString("saprMin", "");
      updateQueryString("saprMax", "");
    }
    updateQueryString("page", "1");
    fetchSearchResults();
  });

  // [3] 발행일 필터 라디오 버튼 (예: "", "3M", "6M", "1Y", "3Y", "5Y")
  $("input[name='filterRlseDateRdo']").on("change", function () {
    const value = $(this).val();
    updateQueryString("rlseDate", value);
    updateQueryString("page", "1");
    fetchSearchResults();
  });


  $("#btnPriceApply").on("click", function(e) {
    e.preventDefault();
    searchFilterSaprText();
  });

  function searchFilterSaprText() {
    // 최소 금액과 최대 금액 입력값 가져오기 (공백 제거)
    const saprMin = $("#saprminFilter").val().trim();
    const saprMax = $("#saprmaxFilter").val().trim();

    // 쿼리 스트링에 가격 파라미터 업데이트 (입력값이 없으면 해당 파라미터 삭제됨)
    updateQueryString("saprMin", saprMin);
    updateQueryString("saprMax", saprMax);

    // 필터 변경 시 페이지 번호를 1로 초기화
    updateQueryString("page", "1");

    // 변경된 쿼리 스트링을 기준으로 검색 결과 Ajax 호출
    fetchSearchResults();
  }

  // 재검색 영역: 검색 버튼 클릭 시 처리 (재검색어를 쿼리스트링에 추가)
  $(".filter_search_box .btn_search").on("click", function(e) {
    e.preventDefault();
    const reKeyword = $("#reKeyword").val().trim();
    // 재검색어 쿼리 파라미터 업데이트 (빈 문자열이면 삭제)
    updateQueryString("reKeyword", reKeyword);
    // 페이지 번호를 1로 초기화
    updateQueryString("page", "1");
    // Ajax로 재검색 결과 호출
    fetchSearchResults();
  });

});

function inputNumberFormat(obj) {
  obj.value = comma(uncomma(obj.value));
}

//콤마찍기
function comma(str) {
  str = String(str);
  return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

//콤마풀기
function uncomma(str) {
  str = String(str);
  return str.replace(/[^\d]+/g, '');
}