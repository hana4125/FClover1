$(function () {
	// 날짜 입력 필드와 버튼 요소
	const periodSelect = $("#period-select");
	const startDateInput = $(".start-date");
	const endDateInput = $(".end-date");
	const startDateDisplay = $(".date-picker-wrapper:first .selected-date");
	const endDateDisplay = $(".date-picker-wrapper:last .selected-date");

	// 달력 버튼 클릭 이벤트
	$(".start-calendar-btn").click(function() {
		startDateInput.trigger('click');
	});

	$(".end-calendar-btn").click(function() {
		endDateInput.trigger('click');
	});

	// 기간 선택 변경 이벤트
	periodSelect.change(function() {
		const period = $(this).val();
		const today = new Date();
		let startDate = new Date();

		if (period === "0") {
			// 직접 입력 모드
			startDateInput.show().prop('disabled', false);
			endDateInput.show().prop('disabled', false);
			startDateDisplay.text('');
			endDateDisplay.text('');
			return;
		}

		// 선택된 기간에 따라 시작일 계산
		startDate.setMonth(today.getMonth() - parseInt(period));

		// 날짜 포맷팅
		const formattedStartDate = formatDate(startDate);
		const formattedEndDate = formatDate(today);

		// 날짜 표시 및 값 설정
		startDateInput.val(formattedStartDate);
		endDateInput.val(formattedEndDate);
		startDateDisplay.text(formattedStartDate);
		endDateDisplay.text(formattedEndDate);

		// 필터링 실행
		filterByDate(formattedStartDate, formattedEndDate);
	});

	// 날짜 직접 선택 이벤트
	startDateInput.change(function() {
		const selectedDate = $(this).val();
		startDateDisplay.text(selectedDate);
		if (endDateInput.val()) {
			filterByDate(selectedDate, endDateInput.val());
		}
	});

	endDateInput.change(function() {
		const selectedDate = $(this).val();
		endDateDisplay.text(selectedDate);
		if (startDateInput.val()) {
			filterByDate(startDateInput.val(), selectedDate);
		}
	});

	// 날짜 필터링 함수
	function filterByDate(startDate, endDate) {
		// 현재 URL에서 currentPage 파라미터 가져오기
		const urlParams = new URLSearchParams(window.location.search);
		const currentPage = urlParams.get('currentPage') || 1;

		// URL 생성 - 전체 경로 포함
		const filterUrl = `/inquiry/question/filter?currentPage=${currentPage}&startDate=${startDate}&endDate=${endDate}`;

		// 페이지 이동
		window.location.href = filterUrl;
	}

	// 날짜 포맷팅 함수 (YYYY-MM-DD)
	function formatDate(date) {
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, '0');
		const day = String(date.getDate()).padStart(2, '0');
		return `${year}-${month}-${day}`;
	}

	// 페이지 로드 시 URL의 날짜 값을 입력 필드에 설정
	function initializeDateFields() {
		const urlParams = new URLSearchParams(window.location.search);
		const startDate = urlParams.get('startDate');
		const endDate = urlParams.get('endDate');

		if (startDate) {
			startDateInput.val(startDate);
			startDateDisplay.text(startDate);
		}
		if (endDate) {
			endDateInput.val(endDate);
			endDateDisplay.text(endDate);
		}
	}

	// 초기화 함수 실행
	initializeDateFields();
});