/**
 * @file search_filter.js
 * @doc 검색 필터 기능
 */

let saprminVal = "";
let saprmaxVal = "";
let saprFilterFlag = false;
let reKeywordSubmitFlag = false;
const filterArr = ['keyword', 'rekeyword', 'target', 'totalType', 'gbCode', 'page', 'ra', 'len', 'cat1', 'cat2', 'cat3', 'ebrent', 'ebdiv', 'exclFree', 'onlyFree', 'samPre', 'samUnlimit', 'cpn', 'evnt', 'rlseDate',
  'grd', 'pbcm', 'saprmin', 'saprmax', 'freeDlvr', 'stdvr', 'hotDlvr', 'ageOver', 'exclAgeOver', 'pod', 'separ', 'saleNo', 'exclOos', 'comb', 'revise', 'sale', 'kbcClst1', 'kbcClst2', 'saleCmdt',
  'repKeyword', 'repKeywordTarget', 'cname', 'chrcDetail', 'pbcmDetail','mdKeyword', 'composer', 'artist','conductor','performer','vocal','orchestra','akgi','label','tracksong','director','actor','rlseStr','rlseEnd','type','cate',
  'chrcCode','pbcmCode','chrc'];
const detailFilterStr = "|repKeyword|repKeywordTarget|cname|chrcDetail|pbcmDetail|mdKeyword|composer|artist|conductor|performer|vocal|orchestra|akgi|label|tracksong|director|actor|rlseStr|rlseEnd|type|cate|";
const detailFilterArr = ['repKeyword', 'repKeywordTarget', 'cname', 'chrcDetail', 'pbcmDetail', 'mdKeyword', 'composer', 'artist', 'conductor', 'performer', 'vocal', 'orchestra', 'akgi', 'label', 'tracksong', 'director', 'actor', 'rlseStr', 'rlseEnd', 'type', 'cate'];
const conditionFilterStr = "|chrcCode|pbcmCode|chrc|pbcm|cname|";

$(function () {

  let cat_depth = $("#categoryDepthHidden").val();
  let cat_id = $("#categoryIdHidden").val();
  let totalTypeVal = getAutoSearchParam('totalType');

  //재검색 키워드 이벤트 검색 가능 상태 변경
  $(document).on('keydown', '#rekeyword', function(key){
    if(key.key === 'Enter' && (key.originalEvent.isComposing === false)){
      reKeywordSubmitFlag = true;
    }
  });

  //재검색 키워드 엔터키로 검색 설정
  $(document).on('keyup', '#rekeyword', function(key){
    if(key.key === 'Enter' && reKeywordSubmitFlag && (key.originalEvent.isComposing === false)){
      submitSearchRekeywordPage();
    }
  });

  //재검색 돋보기 클릭시 이벤트
  $(document).on('click', '.btn_ip_search', function(){
    submitSearchRekeywordPage();
  });

  //필터 검색 초기화 이벤트
  $(document).on('click', '.btn_reset', function(){
    if($("#searchType").val()=="DETAIL_SEARCH") {
      clearDetailFilter();
    }else{
      clearFilter();
    }
    $(".filter_list_box").show();
    $("#selected_filter_box").show();
  });

  //최초 카테고리 필터 라벨 그리기
  drawCategoryLabel(cat_depth, cat_id, totalTypeVal);

  //최초 기타 필터 라벨 그리기
  firstDrawEtcLabel();

  //최초 함께 많이 본 카테고리 라벨 체크
  checkRecommendCategoryLabel();

  //검색 필터 cat1 클릭 시
  $(document).on('click', '.cat_filter_depth2', function(){

    var target = getAutoSearchParam('target');
    var totalType = getAutoSearchParam('totalType');

    if(totalType != ""){
      target = totalType;
    }

    if($(this).hasClass('active')){
      changeShopArea("", "");
      drawCategoryLabel("", "", target);
    }else{
      changeShopArea("cat1", this.value);
      drawCategoryLabel("cat1", this.value, target);
    }

    $("input[name='filterRdo']").prop('checked', false);
  });

  //검색 필터 cat2 클릭 시
  $(document).on('click', '.cat_filter_depth3', function(){

    var target = getAutoSearchParam('target');
    var totalType = getAutoSearchParam('totalType');

    if(totalType != ""){
      target = totalType;
    }

    $("input[name='filterRdo']").prop('checked', false);

    if($(this).hasClass('active')){
      search_catId = this.value.split('@@');
      changeShopArea("cat1", search_catId[0]);
      drawCategoryLabel("cat1", search_catId[0], target);
    }else{
      changeShopArea("cat2", this.value);
      drawCategoryLabel("cat2", this.value, target);
    }
  });

  //검색 필터 cat3 변경 시
  $(document).on('change', "input[name='filterRdo']", function(){

    var target = getAutoSearchParam('target');
    var totalType = getAutoSearchParam('totalType');

    if(totalType != ""){
      target = totalType;
    }

    changeShopArea("cat3", this.value);
    drawCategoryLabel("cat3", this.value, target);

  });










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


