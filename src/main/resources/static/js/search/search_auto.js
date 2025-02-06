/**
 * @file search_auto.js
 * @doc 자동 완성 검색 기능
 */

let isRcntShow = true; // 최근 검색어 노출 여부 설정
let enterKeySubmitFlag = true;

let autoKeywordCaller = [];
/*
    const SCF; (SearchCommonFunctions)
*/
const SCF = {
  "isEmpty" : function(str){
    if(typeof str == "undefined" || str == null || str == "")
      return true;
    else
      return false ;
  },
  "isNotEmpty" : function(str){
    if(typeof str == "undefined" || str == null)
      return false;
    else
      return true ;
  },
  "doAutoKeywordEmpty" : function(){
    prevSearchKeyword = "";
    if(!$("#hFrame").hasClass("active")){
      $("#hFrame").addClass('active');
    }
    $(".noKeyword").show();
    $(".inKeyword").hide();
    rcntKeywordShow();
  },
  "getSearchKeyword" : function(){
    return SCF.isEmpty($("#searchKeyword").val()) ? "" : $("#searchKeyword").val().trim();
  },
  "isSearchKeywordEmpty" : function(){
    return SCF.getSearchKeyword() == "";
  }
};

let gapTime = 175;
let prevSearchKeyword = SCF.getSearchKeyword();


$(function() {

  isRcntShow = getAutoCompleteCookie("rcntShow")=="off" ? false : true;
  splitTempAll = new Array(0);

  // 최근검색어 남기기-pc
  if($("#searchKeyword").val() !=null && $("#searchKeyword").val() !=''){
    addRcentSchList($("#searchKeyword").val());
  }

  // 검색버튼 클릭시 이벤트
  $(".search-btn").click(function(e){
    e.preventDefault();
    if($("#searchKeyword").val() == ""){
      alert("검색어를 입력해 주세요!");
    }else{
      submitSearchKeywordPage();
    }
  });

  //메인 검색창 이벤트 설정
  $("#searchKeyword").on({
    //엔터키 이벤트 검색 가능 상태 변경
    keydown: function (key) {    //엔터키가 검색어창에서 눌려진 상태면 검색 호출 가능 상태로 변경 - 레이어 팝업창 확인누를 시 엔터키 이벤트 호출 방지
      if(key.keyCode == 13){
        key.preventDefault();
        enterKeySubmitFlag = true;
      }
    },

    //키워드 엔터키로 검색 설정
    keyup: function (key) {
      if(key.keyCode == 13 && enterKeySubmitFlag){
        submitSearchKeywordPage();
      }else {
        if( !SCF.isSearchKeywordEmpty() ){
          let _keyword = SCF.getSearchKeyword();
          autoClearTimeout();
          if(prevSearchKeyword == _keyword){
            //console.log("Same keyword requested : " + prevSearchKeyword);
          }else if(prevSearchKeyword != _keyword){
            let _caller = setTimeout(function(e){
              //console.log("SendAutoKeyword : " + _keyword);
              prevSearchKeyword = _keyword;
              sendAutoKeyword(); // 나중에 구현
            }, gapTime);
            autoKeywordCaller.push(_caller);
          }
        }else{
          SCF.doAutoKeywordEmpty();
        }
      }
    },
    click: function () {
      if(!$("#hFrame").hasClass("active")) {
        let keyword = $("#searchKeyword").val();
        if(keyword != ""){
          sendAutoKeyword();
        }else{
          if(!$("#hFrame").hasClass("active")){
            $("#hFrame").addClass('active');
          }
          $(".noKeyword").show();
          $(".inKeyword").hide();
          rcntKeywordShow();
        }
      }
    }

  });

  $("#ageSel").on('selectmenuchange', function() {
    ageChange(this.value);
  });

  $("#genderSel").on('selectmenuchange', function() {
    genderChange(this.value);
  });

  // 자동완성 활성 영역이 아닌 곳 클릭 시 자동완성 창 닫기
  $(document).on('click', function(e) {
    if(e.target.id != "searchKeyword" && !$(e.target).hasClass("auto_complete_maintain") && $(e.target).parents('.auto_complete_maintain').length <= 0) {
      $("#hFrame").removeClass('active');
    }
  });

  //닫기
  $(document).on('click', '#close_search_auto', function() {
    $("#hFrame").removeClass('active');
  });

});

function autoClearTimeout(){
  if(autoKeywordCaller.length > 0){
    for(var i=0; i<autoKeywordCaller.length; i++){
      try{
        clearTimeout(autoKeywordCaller.pop());
      }catch(e){
        //console.log("Clear Timeout Error");
      }
    }
  }
}

//자동검색어 쿠키
function getAutoCompleteCookie (name) {
  let arg = name + "=";
  let alen = arg.length;
  let clen = document.cookie.length;
  let i = 0;
  while (i < clen) {
    let j = i + alen;
    if (document.cookie.substring(i, j) == arg)
      return getAutoCompleteCookieVal (j);
    i = document.cookie.indexOf(" ", i) + 1;
    if (i == 0) break;
  }
  return null;
}

function getAutoCompleteCookieVal (offset) {
  let endstr = document.cookie.indexOf (";", offset);
  if (endstr == -1)
    endstr = document.cookie.length;
  return unescape(document.cookie.substring(offset, endstr));
}

function setAutoCompleteCookie (name, value, expire) {
  let expire_date = new Date(expire);
  let domain = "";
  document.cookie = name + "=" + escape (value) + "; path=/; expires=" + expire_date.toUTCString() + "; domain=" + domain + ";";
}

function delAutoCompleteCookie(name) {
  let today = new Date();
  let expire_date = new Date(today.getTime() - 60*60*24*1000);
  let domain = "";
  document.cookie = name + "=; path=/; expires=" + expire_date.toUTCString() + "; domain=" + domain + ";";
}

/**
 * 자동완성 호출
 * @param value 키워드
 * @returns
 */
function sendAutoKeyword(){
  autoClearTimeout();
  $(".noKeyword").hide();
  $(".inKeyword").show();
}

// 검색 시작 및 페이지 이동
function goSearchKeywordPage(keyvalue){
  let inkeyword = keyvalue.trim();
  if(inkeyword == ""){
    alert("검색어를 입력해 주세요.");
    enterKeySubmitFlag = false;
    return false;
  }

  let paramStr = "";


  paramStr += "keyword="+encodeURIComponent(inkeyword);
  location.href = "/search/searchKeyword?"+paramStr;
}

function submitSearchKeywordPage() {
  let keyword = $("#searchKeyword").val();
  goSearchKeywordPage(keyword);
}


//검색어저장 꺼짐
function getRcntKeywordNoShow() {

  $(".recent_keyword_box").empty();

  let addHtml  ="";
  addHtml  += "                              <div class='title_wrap title_size_def'>";
  addHtml  += "                                  <p class='title_heading'>최근 검색어</p>";
  addHtml  += "                              </div>";
  addHtml  += "                              <div class='recent_keyword_list_wrap'>";
  addHtml  += "                                    <div class='no_data size_sm'>";
  addHtml  += "                                         <div class='no_data_desc'>검색어저장이 꺼져 있습니다.<br />검색어저장 켜기를 클릭해 주세요.</div>";
  addHtml  += "                                    </div>";
  addHtml  += "                                    <div class='recent_keyword_bottom auto_complete_maintain'>";
  addHtml  += "                                        <div class='btn_wrap'>";
  addHtml  += "                                            <button type='button' class='btn_save_keyword'><span class='text'>검색어저장 <span class='val' onclick='keywordShowOn(); '>켜기</span></span></button>";
  addHtml  += "                                        </div>";
  addHtml  += "                                    </div>";
  addHtml  += "                               </div>";

  $(".recent_keyword_box").append(addHtml);
}


//기본틀
function showAutoKeyword() {

  $("#hFrame").empty();
  $(".search_content_wrap").remove();
  let addHtml;
  addHtml  ="";
  addHtml  += "                   <div class=\"search_content_wrap\">";
  addHtml  += "                       <div class=\"scroll_wrap\">";
  addHtml  += "                           <div class=\"keyword_contents_area\">";
  addHtml  += "                              <div class=\"recent_keyword_box\">";
  addHtml  += "                              </div>";
  addHtml  +="                                <div class=\"hot_keyword_box\" id=\"hotKeywordBox\">";
  addHtml  +="                                </div>";
  addHtml  +="                            </div>";
  addHtml  +="                            <div class=\"util_area\">";
  addHtml  +="                                <div class=\"util_button_area\">";
  addHtml  +="                                    <a href=\"/inquiry/notice/noti_list\" class=\"btn_xs\"><span class=\"ico_link\"></span><span class=\"text\">도움말</span></a>";
  addHtml  +="                                    <a href=\"/search/searchDetail\" class=\"btn_xs\"><span class=\"ico_search\"></span><span class=\"text\">상세검색</span></a>";
  addHtml  +="                                </div>";
  addHtml  += "                               <button type=\"button\" class=\"button_layer_close\" id=\"close_search_auto\"><span class=\"text\"> 닫기</span></button>";
  addHtml  +="                            </div>";
  addHtml  +="                        </div>";
  addHtml  +="                    </div>";

  $("#hFrame").html(addHtml);
}

//자동검색어
function rcntKeywordShow() {
  //기본틀
  showAutoKeyword();
  //최근 검색어
  if(isRcntShow == false) {
    getRcntKeywordNoShow();
  } else {
    let recentSch =  getAutoCompleteCookie("recentSch");
    if(recentSch != null && recentSch != "") {
      getRcntKeywordList(recentSch);
    } else {
      getRcntKeywordNoList();
    }
  }
  //실시간 급등 검색어
  riseCall();
}

//최근검색어 끄기
function keywordShowOff() {
  isRcntShow = false;
  let today = new Date();
  let expire_date = new Date(today.getTime() + 365*60*60*24*1000);
  setAutoCompleteCookie ("rcntShow", "off", expire_date);
  getRcntKeywordNoShow();
}

//최근검색어 켜기
function keywordShowOn() {
  isRcntShow = true;
  let today = new Date();
  let expire_date = new Date(today.getTime() - 60*60*24*1000);
  setAutoCompleteCookie ("rcntShow", "on", expire_date);
  let recentSch =  getAutoCompleteCookie("recentSch");
  if(recentSch != null && recentSch != "") {
    getRcntKeywordList(recentSch);
  } else {
    getRcntKeywordNoList();
  }
}

//최근검색어 전체 삭제
function removeKeywordAll() {
  delAutoCompleteCookie("recentSch");
  getRcntKeywordNoList();
}

//최근검색어 개별 삭제
function removeKeyword(idx) {
  delSchList(idx);
  rcntKeywordShow();
}

//실시간인기검색어 연령변경
function ageChange(age) {
  riseCall(age,$("#genderSel").find(':selected').val());
}

//실시간인기검색어 성별변경
function genderChange(gender) {
  riseCall($("#ageSel").find(':selected').val(),gender);
}

//최근검색어 삭제
function delSchList(idx) {
  let recentSch =  getAutoCompleteCookie("recentSch");
  if( recentSch != null && recentSch != ""){
    let recentSchArray = recentSch.split("$|");
    let str = "";
    for( let i=0; i<recentSchArray.length - 1; i++ ){
      if( i != idx ){
        str += recentSchArray[i] + "$|";
      }
    }
    let today = new Date();
    let expire_date = new Date(today.getTime() + 365*60*60*24*1000);
    setAutoCompleteCookie("recentSch", str, expire_date);
  }
}

//최근검색어 추가
function addRcentSchList(sKeyword) {

  if(isRcntShow) {
    let maxLength = 10; //최대 저장갯수
    let delimiter = "$|"; //String 구분자
    let delimiter2 = "$^|"; //String 구분자
    let date = new Date();
    let mm = (date.getMonth() + 1);
    let dd = date.getDate();

    if (("" + mm).length == 1) { mm = "0" + mm; }
    if (("" + dd).length   == 1) { dd   = "0" + dd;   }

    let dateForm = mm +"."+ dd;

    let recentSch = getAutoCompleteCookie("recentSch");
    let recentWord = "";
    sKeyword = suggest_cutString(sKeyword,52);
    recentWord = sKeyword + delimiter2 + dateForm;
    if(recentSch == null){
      recentSch = delimiter;
    }else{
      recentSch =  delimiter + recentSch;
    }

    //과거날짜 중복 체크
    if(recentSch.indexOf(recentWord+delimiter) <= 0 && recentSch.indexOf(sKeyword + delimiter2) > 0){   //현재날짜 중복체크
      let tot = parseInt(recentSch.indexOf(sKeyword + delimiter2))+ parseInt(delimiter2.length) + parseInt(sKeyword.length) + parseInt(dateForm.length)+parseInt(delimiter.length);
      let dup_data = recentSch.substring(recentSch.indexOf(sKeyword + delimiter2), tot);
      //console.log("dup_data:" + dup_data);
      recentSch = recentSch.replace(dup_data,"");
    }

    if(recentSch.indexOf(recentWord+delimiter) > 0){    //현재날짜 중복체크
      recentSch = recentSch.replace(delimiter+recentWord+delimiter,delimiter);
    }
    recentSch = recentWord + recentSch;

    let recentSchCount = recentSch.split(delimiter).length;

    //최대 저장갯수를 초과하면 처음들어온거 삭제.
    if(recentSchCount>maxLength+1){
      recentSch = recentSch.substring(0,recentSch.lastIndexOf(delimiter));
      recentSch = recentSch.substring(0,recentSch.lastIndexOf(delimiter)+delimiter.length);
    }
    let today = new Date();
    let expire_date = new Date(today.getTime() + 365*60*60*24*1000);
    setAutoCompleteCookie("recentSch" ,recentSch, expire_date);
  }
}

//limitByte 의 byte 만큼 str 을 자른 후 반환한다.
function suggest_cutString( str , limitByte){
  let inc = 0;
  let nbytes = 0;
  let msg = "";
  let msglen = str.length;

  for (i=0; i<msglen; i++) {

    let ch = str.charAt(i);
    if (escape(ch).length > 4) {
      inc = 2;
    } else if (ch == '') {
      if (str.charAt(i-1) != '') {
        inc = 1;
      }
    } else if (ch == '<' || ch == '>') {
      inc = 4;
    } else {
      inc = 1;
    }
    if ((nbytes + inc) > limitByte ) {
      break;
    }
    nbytes += inc;
    msg += ch;

  }

  if( i != msglen ) msg = msg + "..";

  return msg;
}


//최근검색어
function getRcntKeywordList(recentSch) {

  $(".recent_keyword_box").empty();
  let recentWrdList = recentSch.split("$|");
  let addHtml = "";

  addHtml  += "                              <div class='title_wrap title_size_def'>";
  addHtml  += "                                  <p class='title_heading'>최근 검색어</p>";
  addHtml  += "                                  <div class='right_area'>";
  addHtml  += "                                      <button type='button' class='btn_delete_text'><a href='javascript:removeKeywordAll();'><span class='text'>전체삭제</span></a></button>";
  addHtml  += "                                  </div>";
  addHtml  += "                              </div>";
  addHtml  += "                              <div class='recent_keyword_list_wrap'>";
  addHtml  += "                                  <ul class='recent_keyword_list'>";

  for(let i = 0 ; i < recentWrdList.length-1 ; i++){
    let recentWrd = recentWrdList[i].split("$^|");
    let recentWrd0 = avoidQuotes(recentWrd[0]);
    addHtml  += "                                      <li class='recent_keyword_item'>";
    addHtml  += "                                          <div class='left_area'>";
    addHtml  += "                                              <a href='javascript:goSearchKeywordPage(`"+encodeURIComponent(recentWrd[0])+"`)' class='recent_keyword_link'>";
    addHtml  += "                                                  <span class='keyword'>" + escapetHtml(recentWrd[0]) + "</span>";
    addHtml  += "                                              </a>";
    addHtml  += "                                          </div>";
    addHtml  += "                                          <div class='right_area'>";
    addHtml  += "                                              <span class='date'>" + recentWrd[1] + "</span>";
    addHtml  += "                                              <a href='javascript:removeKeyword('"+i+"');'>";
    addHtml  += "                                                   <button type='button' class='btn_keyword_del'><span class='hidden'>이 검색어 삭제</span></button>";
    addHtml  += "                                              </a> ";
    addHtml  += "                                          </div>";
    addHtml  += "                                      </li>";
  }

  addHtml  += "                                    </ul>";
  addHtml  += "                                    <div class='recent_keyword_bottom auto_complete_maintain'>";
  addHtml  += "                                        <div class='btn_wrap'>";
  addHtml  += "                                            <button type='button' class='btn_save_keyword'><span class='text'>검색어저장 <span class='val' onclick='keywordShowOff(); '>끄기</span></span></button>";
  addHtml  += "                                        </div>";
  addHtml  += "                                    </div>";
  addHtml  += "                               </div>";

  $(".recent_keyword_box").append(addHtml);
}

// avoid quot conflict
function avoidQuotes(str){
  return "decodeURIComponent(&quot;"+encodeURIComponent(str)+"&quot;)";
}


//최근검색어 없음
function getRcntKeywordNoList() {

  $(".recent_keyword_box").empty();

  let addHtml  ="";
  addHtml  += "                              <div class='title_wrap title_size_def'>";
  addHtml  += "                                  <p class='title_heading'>최근 검색어</p>";
  addHtml  += "                              </div>";
  addHtml  += "                              <div class='recent_keyword_list_wrap'>";
  addHtml  += "                                    <div class='no_data size_sm'>";
  addHtml  += "                                         <div class='no_data_desc'>최근 검색어가 없습니다.</div>";
  addHtml  += "                                    </div>";
  addHtml  += "                                    <div class='recent_keyword_bottom auto_complete_maintain'>";
  addHtml  += "                                        <div class='btn_wrap'>";
  addHtml  += "                                            <button type='button' class='btn_save_keyword'><span class='text'>검색어저장 <span class='val' onclick='keywordShowOff();'>끄기</span></span></button>";
  addHtml  += "                                        </div>";
  addHtml  += "                                    </div>";
  addHtml  += "                               </div>";

  $(".recent_keyword_box").append(addHtml);
}

//실시간 급등 검색어
function riseCall(age,gender) {
  $.ajax({
    method: "GET",
    dataType : "json",
    url : "/search/api/popular-keywords",
    data: {
      "gender": gender,
      "ageRange" : age,
      "limit" : 10
    },
    success: function(response) {
      let addHtml = "";
      addHtml  +="                <div class='title_wrap title_size_def'>";
      addHtml  +="                    <p class='title_heading'>실시간 인기 검색어</p>";
      addHtml  +="                    <div class='right_area'>";
      addHtml  +="                        <div class='item_group'>";
      addHtml  +="                            <!-- form_sel -->";
      addHtml  +="                            <div class='form_sel type_arw' data-class='type_arw'>";
      addHtml  +="                                <select title='연령기준 정렬' id='ageSel'>";
      addHtml  +="                                    <option value='ALL'>전연령</option>";
      const ageRange = ['10대', '20대', '30대', '40대']
      for(let range of ageRange){
        let selected = "";
        let upperChr = "";
        if (range == age) selected = "selected";
        if (range == '40대') upperChr ="↑"
        addHtml  +="                                <option value='"+range+"' "+selected+">"+range+upperChr+"</option>";
      }
      addHtml  +="                                </select>";
      addHtml  +="                            </div>";
      addHtml  +="                            <!-- //form_sel -->"
      addHtml  +="                            <div class='form_sel type_arw' data-class='type_arw'>";
      addHtml  +="                                <select title='성별기준 정렬' id='genderSel'>";
      addHtml  +="                                    <option value='ALL'>전성별</option>";
      if(gender=='M'){
        addHtml  +="                                 <option value='M' selected>남성</option>";
        addHtml  +="                                 <option value='F'>여성</option>";
      }else if(gender=='F'){
        addHtml  +="                                 <option value='M'>남성</option>";
        addHtml  +="                                 <option value='F' selected>여성</option>";
      }else{
        addHtml  +="                                 <option value='M'>남성</option>";
        addHtml  +="                                 <option value='F'>여성</option>";
      }
      addHtml  +="                                </select>";
      addHtml  +="                            </div>";
      addHtml  +="                            <!-- //form_sel -->";
      addHtml  +="                        </div>";
      addHtml  +="                    </div>";
      addHtml  +="                </div>";
      addHtml  +="                <div class='hot_keyword_list_wrap'>";
      addHtml  +="                    <ul class='hot_keyword_list'>";

      if(response.keywords && response.keywords.length > 0) {
        response.keywords.forEach(function(keyword, index) {
          let rankClass = (index < 3) ? "hot_keyword_item top" : "hot_keyword_item";
          let rankDisplay = (index < 9) ? "0" + (index + 1) : (index + 1);
          addHtml += "<li class='" + rankClass + "'>";
          addHtml += "  <div class='left_area'>";
          addHtml += "    <a href='javascript:goSearchKeywordPage(\"" + encodeURIComponent(keyword) + "\")' class='hot_keyword_link'>";
          addHtml += "      <span class='rank'>" + rankDisplay + "</span>";
          addHtml += "      <span class='keyword'>" + keyword + "</span>";
          addHtml += "    </a>";
          addHtml += "  </div>";
          addHtml += "</li>";
        });
        addHtml +="                 </ul>";
        addHtml +="                 <div class='hot_keyword_bottom'>";

        let dateStr = getRiseCallYmdStr(response.baseTime);
        addHtml +="                     <p class='info_text font_size_xxs'>"+dateStr+" 기준</p>";
      }

      addHtml +="                     </div>";
      addHtml +="                 </div>";
      $("#hotKeywordBox").html(addHtml);

      // UI 초기화 (selectmenu 등)
      $("#ageSel, #genderSel").on('change', function() {
        if (this.id === 'ageSel') {
          ageChange(this.value);
        } else if (this.id === 'genderSel') {
          genderChange(this.value);
        }
      });

      $("#ageSel-menu, #genderSel-menu").parent().addClass("type_arw auto_complete_maintain");
    }
  });
}

// 실시간 검색어 기준 날짜 형식 변환
function getRiseCallYmdStr(date){
  let dateTemp = date.split(".");
  return dateTemp[1].trim()+"."+dateTemp[2].trim()+" "+dateTemp[3].trim();
}
