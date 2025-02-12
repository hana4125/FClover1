/**
 * @file search_common.js
 * @doc 공통 스크립트
 */

$(function () {

  let message = /*[[${message}]]*/ null;
  if (message != null) {
    alert(message);
  }

  $("#logo").click(function () {
    location.href = "/";
  })

  $("#cart").click(function () {
    location.href = "/member/cart";
  })

  $("#myPage").click(function () {
    location.href = "/myPage";
  })

  $(".show-detail").click(function () {
    location.href = "/goods/GoodsDetail/{no}";
  })

  $(".show-plus-best").click(function () {
    // e.preventDefault(); // 기본 동작 방지
    // if ($(".best-title").hasClass("active")) {
    //     // 베스트셀러 탭이 활성화된 경우
    location.href = "/goods/bestSeller";
    // } else if ($(".steady-title").hasClass("active")) {
    //     // 스테디셀러 탭이 활성화된 경우
    //     location.href = "/goods/steadySeller";
    // }
  });

  $(".show-plus-steady").click(function () {
    // e.preventDefault(); // 기본 동작 방지
    // if ($(".best-title").hasClass("active")) {
    //     // 베스트셀러 탭이 활성화된 경우
    location.href = "/goods/steadySeller";
    // } else if ($(".steady-title").hasClass("active")) {
    //     // 스테디셀러 탭이 활성화된 경우
    //     location.href = "/goods/steadySeller";
    // }
  });

  $(".best").click(function () {
    location.href = "/goods/bestSeller";
  })

  $(".steady").click(function () {
    location.href = "/goods/steadySeller";
  })

  $(".new").click(function () {
    location.href = "/goods/newItems";
  })

  const $searchBar = $("#searchBarToggle");
  const $dropdown = $(".dropdown-menu");

  // searchBar 클릭시 드롭다운 열기
  $searchBar.on("click", function (e) {
    e.stopPropagation();

    // 드롭 다운 위치 설정
    const dropdownInstance = bootstrap.Dropdown.getOrCreateInstance($dropdown[0], {
      reference: $searchBar[0],
      popperConfig: {
        modifiers: [
          {
            name: 'offset',
            options: {
              offset: [0, 0],
            },
          },
        ],
      },
    });

    dropdownInstance.show();
  });

  // 외부 클릭시 드롭다운 닫기
  $(document).on("click", function () {
    const dropdownInstance = bootstrap.Dropdown.getInstance($dropdown[0]);
    if (dropdownInstance) {
      dropdownInstance.hide();
    }
  });

  // 드롭다운 내부 클릭 시 이벤트 전파 방지
  $dropdown.on("click", function (e) {
    e.stopPropagation();
  });

  // 닫기 버튼 클릭 시 드롭다운 닫기
  $(".close-dropdown").on("click", function () {
    const dropdownInstance = bootstrap.Dropdown.getInstance($dropdown[0]);
    if (dropdownInstance) {
      dropdownInstance.hide();
    }
  });
})
