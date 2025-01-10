/**
 * @file search_detail.js
 * @doc 상세검색
 */
/** @todo selectmenu 메소드 대체하기 */
$(function () {
  //출간일 날짜 조정
  $("#selMonInterval").on('selectmenuchange', function () {
    adjustDate(this.value);
  });

  //기본값: 최근 1개월로 세팅
  adjustDate('1m');

  //날짜가 변경될 경우 직접입력으로 선택되는 이벤트
  $("#rlseStr, #rlseEnd").change(function () {
    $("#selMonInterval").val("").selectmenu("refresh").trigger(
        "selectmenuselect");
  });

  //시작 날짜 체크
  $("#rlseStr").on('change', function () {

    //날짜 형식 체크
    //var regex = RegExp(/^\d{4}. (0[1-9]|1[012]). (0[1-9]|[12][0-9]|3[01])$/);

    if ($("#rlseStr").val() != "" && uncomma($("#rlseStr").val()).length != 8) {
      alert("시작 날짜 형식이 맞지 않습니다.");
      $("#rlseStr").val("");
      return false;
    }

    if ($("#rlseEnd").val() < $("#rlseStr").val()) {
      alert("시작 날짜는 종료 날짜와 같거나 더 빨라야 합니다.");
      $("#rlseStr").val("");
      return false;
    }
  });

  //종료 날짜 체크
  $("#rlseEnd").on('change', function () {

    //날짜 형식 체크
    //var regex = RegExp(/^\d{4}. (0[1-9]|1[012]). (0[1-9]|[12][0-9]|3[01])$/);

    if ($("#rlseEnd").val() != "" && uncomma($("#rlseEnd").val()).length != 8) {
      alert("종료 날짜 형식이 맞지 않습니다.");
      $("#rlseEnd").val("");
      return false;
    }

    if ($("#rlseEnd").val() < $("#rlseStr").val()) {
      alert("종료 날짜는 시작 날짜와 같거나 더 늦어야 합니다.");
      $("#rlseEnd").val("");
      return false;
    }
  });

  $('#saprmax').focusout(function () {
    if (parseInt(uncomma($("#saprmax").val())) < parseInt(
        uncomma($("#saprmin").val()))) {
      alert("최대가격은 최소가격 보다 커야 합니다.");
      $("#saprmax").val("");
    }
  });

  $('#saprmin').focusout(function () {
    if (parseInt(uncomma($("#saprmax").val())) < parseInt(
        uncomma($("#saprmin").val()))) {
      alert("최소가격은 최대가격 보다 작아야 합니다.");
      $("#saprmin").val("");
    }
  });

  $("#cateSel").on('selectmenuchange', function () {
    if ($("#typeSel").val() == "") {
      alert({
        "title": "상품군을 선택해주세요.",
        "confirm": {"label": "확인"}
      });
    }
  });

});

//상세검색 실행
/** @todo 프론트에서 보내지는 양식에 맞춰 상세검색 로직 짜기 */
function goDetailSearchResult() {
  var cateVal = $("#cateSel").val();

  //Input으로 끝나는 id객체중 맨 처음값이 대표 키워드로 설정되게 세팅
  $("input[id$='Input']").each(function (index, element) {
    if ($(this).val() != "") {
      var repKeywordVal = $(this).val().trim();
      $("#repKeywordHidden").val(repKeywordVal);
      $("#detailSearchForm").append(
          $('<input type="hidden" value="' + $(this).attr('id').replace(
              "Input", "") + '" name="repKeywordTarget">'));
      return false;
    }
  });

  var cnameVal = checkKeyword($("#cnameInput").val().trim());
  var chrcVal = checkKeyword($("#chrcDetailInput").val().trim());
  var pbcmVal = checkKeyword($("#pbcmDetailInput").val().trim());
  var combineVal = cnameVal + chrcVal + pbcmVal;

  if (combineVal == "") {
    alert("상품명/저자/출판사/검색키워드(주제어)중 하나 이상을 입력해주세요");
    return false;
  }
  if (($("#saprmin").val() != "" && $("#saprmax").val() == "") || ($(
      "#saprmin").val() == "" && $("#saprmax").val() != "")) {
    alert("숫자를 입력해주세요.");
    return false;
  }

  $("#cnameHidden").val(cnameVal);
  $("#chrcHidden").val(chrcVal);
  $("#pbcmHidden").val(pbcmVal);

  $("#detailSearchForm").append(
      $('<input type="hidden" value="' + uncomma($("#saprmin").val())
          + '" name="saprmin">'));
  $("#detailSearchForm").append(
      $('<input type="hidden" value="' + uncomma($("#saprmax").val())
          + '" name="saprmax">'));

  if ($("#rlseStr").val() != "" && $("#rlseEnd").val() != "") {
    $("#detailSearchForm").append(
        $('<input type="hidden" value="' + uncomma($("#rlseStr").val())
            + '" name="rlseStr">'));  // uncomma함수가 콤마 외에도 특수문자 모두 제거
    $("#detailSearchForm").append(
        $('<input type="hidden" value="' + uncomma($("#rlseEnd").val())
            + '" name="rlseEnd">'));  // uncomma함수가 콤마 외에도 특수문자 모두 제거
  }

  //카테고리 필터 세팅
  if (typeof cateVal == "undefined" || cateVal == "") {
    $("#detailSearchForm").append(
        $('<input type="hidden" value="전체@ALL" name="cat">'));
  } else {
    $("#detailSearchForm").append(
        $('<input type="hidden" value="' + cateVal + '" name="cat">'));
  }

  $("#detailSearchForm").submit();

}

//초기화
function initValue() {
  $("#cateSel").val("전체").selectmenu().selectmenu("refresh").trigger(
      "selectmenuselect");

  $("#selMonInterval").val("1m").selectmenu().selectmenu("refresh").trigger(
      "selectmenuselect");
  adjustDate('1m');
  $("#saprmin").val("");
  $("#saprmax").val("");

  $("#cnameInput").val("");
  $("#chrcDetailInput").val("");
  $("#pbcmDetailInput").val("");
}

//날짜 조정
function adjustDate(param) {

  var $startDt = $("#rlseStr");
  var $endDt = $("#rlseEnd");

  var nowDate = new Date();
  var yy = nowDate.getFullYear() + "";
  var mm = nowDate.getMonth() + 1;
  var mm2 = leadingZeros(mm, 2) + "";
  var dd = leadingZeros(nowDate.getDate(), 2) + "";
  var today = yy + ". " + mm2 + ". " + dd;

  if (param == "all") {
    $startDt.val("");
    $endDt.val("");
  } else {
    if (param == "1m") {
      nowDate.setMonth(nowDate.getMonth() - 1);
    } else if (param == "2m") {
      nowDate.setMonth(nowDate.getMonth() - 2);
    } else if (param == "3m") {
      nowDate.setMonth(nowDate.getMonth() - 3);
    } else if (param == "6m") {
      nowDate.setMonth(nowDate.getMonth() - 6);
    }

    yy = nowDate.getFullYear() + "";
    mm = nowDate.getMonth() + 1;
    mm2 = leadingZeros(mm, 2) + "";
    dd = leadingZeros(nowDate.getDate(), 2) + "";
    $startDt.val(yy + ". " + mm2 + ". " + dd);
    $endDt.val(today);
  }
}

function leadingZeros(n, digits) {
  var zero = '';
  n = n.toString();

  if (n.length < digits) {
    for (var i = 0; i < digits - n.length; i++) {
      zero += '0';
    }
  }
  return zero + n;
}

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

// 검색어 체크
function checkKeyword(text) {
  var checkValue = text + "";
  checkValue = checkValue.replace(/&/gi, "&amp;");
  checkValue = checkValue.replace(/\\/gi, "&#92;");
  checkValue = checkValue.replace(/</gi, "&lt;");
  checkValue = checkValue.replace(/>/gi, "&gt;");
  checkValue = checkValue.replace(/'/gi, "&#39;");
  checkValue = checkValue.replace(/\[/gi, "&#91;");
  checkValue = checkValue.replace(/\]/gi, "&#93;");
  checkValue = checkValue.replace(/"/gi, "&#34;");
  return checkValue;
}