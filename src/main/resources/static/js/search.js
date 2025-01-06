// 검색 기능 스크립트

var isRcntShow = true; // 최근 검색어 노출 여부 설정

$(function() {
  $('#searchKeyword').on('input', function() {
    let inputVal = $(this).val().trim(); // 입력값 공백 제거

    // 입력값 유무에 따라 보여지는 요소 변경
    if (inputVal !== '') {
      $('.inKeyword').show();
      $('.noKeyword').hide();

      // 추천 검색어 삽입
      let recommendKeywords = getRecomKeyword();
      $('#recommendKeywordBox').append(recommendKeywords);








    } else {
      $('.inKeyword').hide();
      $('.noKeyword').show();









    }


  });
});

// 추천 검색어 가져오기
function getRecomKeyword() {
  let result = '';

  // TODO : 로직 구현하기

  // 나이, 성별 기본값
  let age = 0;
  let gender = "0";

  // User 성별, 나이 정보 불러와서 지정



  $.ajax({
    url: '/getRecommendKeyword',
    type: 'GET',
    dataType: 'json',
    data: {
      gender: gender,
      age: age
    },
    success: function(rst) {
      let $temp = rst.data.resultDocuments;
      let addHtml = "";
      if ( $temp != "" && $temp != null) {

      }
    }
  });

  return result;
}