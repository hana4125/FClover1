/**
 * @file search_filter.js
 * @doc 검색 필터 기능
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
   * @param {String} value - 설정할 값 (빈 문자열이나 null이면 해당 파라미터 삭제)
   * @returns {URLSearchParams} 업데이트된 URLSearchParams 객체
   */
  function updateQueryString(param, value) {
    const urlParams = new URLSearchParams(window.location.search);

    // 값이 있으면 설정, 없으면 해당 파라미터 삭제
    if (value && value.trim() !== "") {
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
    $('#resultKeyword').text("'" + (new URLSearchParams(window.location.search)).get('keyword') + "' 검색 결과");
    $('#resultCount').text("전체 " + data.totalCount + "건");

    // 결과 리스트 업데이트 (기존 내용 지우기)
    const $prodList = $('.prod_list');
    $prodList.empty();

    if (!data.searchResults || data.searchResults.length === 0) {
      $prodList.append('<li>검색 결과가 없습니다.</li>');
      return;
    }

    // 검색 결과가 있을 경우 각 항목을 추가
    $.each(data.searchResults, function (index, goods) {
      const listItem = `
        <li class="prod_item d-flex justify-content-between align-items-start">
          <div class="prod_area d-flex align-items-start mb-5">
            <a href="/goods/GoodsDetail/${goods.goodsNo}" class="show-detail">
              <div class="img-box">
                <img src="${goods.mainImage ? goods.mainImage.goodsUrl + '/' + goods.mainImage.goodsImageName : '/images/user/book/img.png'}"
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
                <div class="prod_publish">
                  ${ goods.companyName ? `<span class="prod_publish">${goods.companyName}</span>` : '<span class="prod_publish">출판사 없음</span>' }
                  <span class="date">${formatDate(goods.goodsCreateAt)}</span>
                </div>
                <div class="prod_price">
                  <span class="val">${formatPrice(goods.goodsPrice)}</span>
                  <span class="unit">원</span>
                </div>
              </div>
            </div>
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
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('ko-KR', options);
  }

  function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  function renderPagination(data) {
    // 예시: 페이징 영역 업데이트 (필요에 따라 구현)
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

  // 필터나 정렬 변경 시 (예를 들어 드롭다운, input 등)
  // 각각의 요소에 change 또는 keyup 이벤트를 붙여 바로 updateQueryString() 호출
  $('.filter, .sort').on('change keyup', function () {
    // 예를 들어, 요소의 name 속성이 쿼리 파라미터 이름으로 사용됨
    const param = $(this).attr('name');
    const value = $(this).val();
    updateQueryString(param, value);
    fetchSearchResults();
  });

  // 카테고리 클릭 이벤트 (예: 카테고리 목록의 각 항목에 .category-item 클래스와 value 속성이 있다고 가정)
  $('.category-item').on('click', function (e) {
    e.preventDefault();
    const catNo = $(this).val();
    updateQueryString('cateNo', catNo);
    fetchSearchResults();
  });

  // 페이징 버튼 클릭 (페이징 영역에 data-page 속성을 활용)
  $('#pagination').on('click', '.page-link', function (e) {
    e.preventDefault();
    const page = $(this).data('page');
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
  $('.btn_filter_depth1').on('click', function(){
    // 버튼에 active 클래스를 토글하여 스타일 변경
    $(this).toggleClass('active');
    // 같은 depth1 메뉴 내의 filter_cont_box를 slideToggle으로 보이거나 숨김
    $(this).closest('.menu_item.item_depth1').find('.filter_cont_box').slideToggle();
  });


});
