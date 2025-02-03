$(function () {
	// 기간 검색 버튼 클릭 시
	$("#period-search-btn").click(function () {
		const period = $("#period-select").val();
		const startDate = $("#start-date").val();
		const endDate = $("#end-date").val();

		// 서버로 전송할 데이터
		const data = {
			period: period,
			startDate: startDate,
			endDate: endDate
		};

		// AJAX 요청
		$.ajax({
			url: '/inquiry/question/filter', // 서버 측 필터링 처리 URL
			method: 'GET',
			data: data,
			success: function (response) {
				// 응답 데이터로 테이블 갱신
				updateTable(response);
			},
			error: function (xhr, status, error) {
				console.error("Error:", error);
			}
		});
	});

	// 테이블 갱신 함수
	function updateTable(data) {
		const tbody = $("table tbody");
		tbody.empty(); // 기존 테이블 내용 비우기

		// 새로운 데이터로 테이블 채우기
		data.forEach((item, index) => {
			const row = `
        <tr>
          <td>${item.qno}</td>
          <td>
            <div class="ntitle">
              <a href="/inquiry/question/detail?qno=${item.qno}">${item.qtitle}</a>
            </div>
          </td>
          <td>${item.qname}</td>
          <td>${item.qcreateat.substring(2, 10)}</td>
        </tr>
      `;
			tbody.append(row);
		});
	}
});