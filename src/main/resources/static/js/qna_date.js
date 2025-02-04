$(function () {
	const periodSelect = $("#period-select");
	const startDateInput = $(".start-date");
	const endDateInput = $(".end-date");
	const startDateDisplay = $(".date-picker-wrapper:first .selected-date");
	const endDateDisplay = $(".date-picker-wrapper:last .selected-date");

	// 달력 버튼 클릭 이벤트
	$(".start-calendar-btn").click(() => startDateInput.trigger('click'));
	$(".end-calendar-btn").click(() => endDateInput.trigger('click'));

	// 기간 선택 변경 이벤트
	periodSelect.change(function () {
		const period = $(this).val();
		const today = new Date();
		let startDate = new Date();

		if (period === "0") {
			// "전체" 선택 시 입력 필드 숨기고 초기화
			startDateInput.hide().val("");
			endDateInput.hide().val("");
			startDateDisplay.text("");
			endDateDisplay.text("");
			window.location.href = "/inquiry/question/list"; // ✅ 전체 선택 시 리스트 페이지로 이동
			return;
		}

		// 개월 수 선택 시 날짜 계산
		startDate.setMonth(today.getMonth() - parseInt(period));
		const formattedStartDate = formatDate(startDate);
		const formattedEndDate = formatDate(today);

		// 날짜 필드 보이기 & 값 설정
		startDateInput.show().val(formattedStartDate);
		endDateInput.show().val(formattedEndDate);
		startDateDisplay.text(formattedStartDate);
		endDateDisplay.text(formattedEndDate);

		// 필터링 실행
		filterByDate(formattedStartDate, formattedEndDate);
	});

	// 날짜 직접 입력 이벤트
	startDateInput.change(() => updateDateFilter());
	endDateInput.change(() => updateDateFilter());

	function updateDateFilter() {
		const selectedStartDate = startDateInput.val();
		const selectedEndDate = endDateInput.val();

		if (selectedStartDate && selectedEndDate) {
			periodSelect.val("0"); // 직접 입력 시 "직접 선택(0)"으로 변경
			filterByDate(selectedStartDate, selectedEndDate);
		}
	}

	// 날짜 필터링 함수
	function filterByDate(startDate, endDate) {
		const urlParams = new URLSearchParams(window.location.search);
		const currentPage = urlParams.get("currentPage") || 1;

		// 필터링된 URL로 이동
		const filterUrl = `/inquiry/question/filter?currentPage=${currentPage}&startDate=${startDate}&endDate=${endDate}`;
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

		if (!startDate || !endDate) {
			periodSelect.val("0"); // "전체"로 설정
			startDateInput.hide().val("");
			endDateInput.hide().val("");
			return;
		}

		// 날짜가 있으면 필드에 설정
		startDateInput.val(startDate).show();
		endDateInput.val(endDate).show();
		startDateDisplay.text(startDate);
		endDateDisplay.text(endDate);

		// 선택한 날짜에 따라 select 박스 업데이트
		updateSelectBox(startDate, endDate);
	}

	// select 박스 업데이트 함수
	function updateSelectBox(startDate, endDate) {
		const today = new Date();
		const start = new Date(startDate);

		if (!startDate || !endDate) return;

		const diffMonths = (today.getFullYear() - start.getFullYear()) * 12 + (today.getMonth() - start.getMonth());

		let selectedValue = "0"; // 기본값: 직접입력

		if (diffMonths === 1) {
			selectedValue = "1";
		} else if (diffMonths === 3) {
			selectedValue = "3";
		} else if (diffMonths === 6) {
			selectedValue = "6";
		}

		if (periodSelect.val() !== selectedValue) {
			periodSelect.val(selectedValue);
		}
	}

	// 초기화 실행
	initializeDateFields();
});
