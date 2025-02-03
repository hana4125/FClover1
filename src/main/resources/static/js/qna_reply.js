$(function () {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");

	let page = 1;
	const boardNum = $("#board_num").val();

	if (boardNum) {
		getList(1);
	} else {
		console.error("board_num is empty");
		$("#message").text("게시글 번호가 없습니다.").show();
	}

	function getList(currentPage) {

		$.ajax({
			type: "post",
			url: "/inquiry/qlist",
			data: {
				qno: parseInt(boardNum),
				page: parseInt(currentPage)
			},
			beforeSend: function (xhr) {
				if (token && header) {
					xhr.setRequestHeader(header, token);
				}
			},
			dataType: "json",
			success: function (rdata) {
				if (rdata.qlist && rdata.qlist.length > 0) {
					$("#comment table").show();
					$("#comment tbody").empty();

					$(rdata.qlist).each(function () {
						console.log("개별 댓글 데이터:", this);
						let output = `
						<tr>
							<td>${this.memberid}</td>
							<td>${this.ccontent}</td>
							<td>${this.cresponseat}`;

						if ($("#loginid").text() === this.memberid) {
							output += `
							<span class='edit' style='cursor: pointer;' title='edit'>수정</span>
							<span class='remove' style='cursor: pointer;' title='remove'>삭제</span>
							<input type='hidden' value='${this.cno}'>`;
						}

						output += `</td></tr>`;
						$("#comment tbody").append(output);
					});

					if (rdata.totalCount > $("#comment tbody tr").length) {
						$("#message").text("더보기").show();
					} else {
						$("#message").text("").hide();
					}
				} else {
					$("#comment tbody").empty();
					$("#comment table").hide();
				}
			},
			error: function (xhr, status, error) {
				console.error("AJAX 오류:", xhr.responseText);
				$("#message").text("댓글을 불러오는 데 실패했습니다.").show();
			}
		});
	}

	// 글자수 제한 이벤트
	$("#content").on("keyup", function () {
		$(".float-left").text($(this).val().length + "/300");
	});

	// 등록 또는 수정완료 버튼 클릭 이벤트
	let cno;
	$("#write").click(function () {
		const content = $("#content").val().trim();
		if (!content) {
			alert("내용을 입력하세요");
			return false;
		}
		const buttonText = $("#write").text();
		let url;
		let data;

		if (buttonText === "등록") {
			const loginId = $("#loginid").text();
			console.log("로그인한 사용자 ID:", loginId);
			url = "/inquiry/commentAdd";
			data = {
				"ccontent": content,
				"memberid": $("#loginid").text(),
				"qno": $("#board_num").val()
			};
		} else {
			if (!cno) {
				alert("수정할 댓글을 선택하세요.");
				return;
			}
			url = "/inquiry/update";
			data = {
				"cno": cno,
				"ccontent": content
			};
			$("#write").text("등록");
			$("#comment .cancel").remove();
		}

		$.ajax({
			type: "post",
			url: url,
			data: data,
			beforeSend: function (xhr) {
				if (token && header) {
					xhr.setRequestHeader(header, token);
				}
			},
			success: function (result) {
				$("#content").val('');
				if (result === 1) {
					getList(page);
				}
			}
		});
	});

	// "더보기" 버튼 이벤트
	$("#message").click(function () {
		getList(++page);
	});

	// 수정 버튼 이벤트
	$("#comment").on("click", ".edit", function () {
		const before = $(this).parent().prev().text();
		$("#content").focus().val(before);
		cno = $(this).next().next().val();
		$("#write").text("수정완료");

		if (!$("#write").prev().is(".cancel")) {
			$("#write").before('<button class="btn btn-danger float-right cancel">취소</button>');
		}
	});

	// 취소 버튼
	$("#comment").on("click", ".cancel", function () {
		$("#write").text("등록");
		$("#content").val('');
		$(this).remove();
	});

	// 삭제 버튼
	$("#comment").on("click", ".remove", function () {
		if (!confirm("정말 삭제하시겠습니까?")) {
			return;
		}
		const cno = $(this).next().val();
		console.log("삭제할 댓글 번호:", cno);

		$.ajax({
			type: "post",
			url: "/inquiry/delete",
			data: {
				"cno": cno
			},
			beforeSend: function (xhr) {
				if (token && header) {
					xhr.setRequestHeader(header, token);
					console.log("토큰 헤더 설정됨:", header, token);
				}
			},
			success: function (result) {
				console.log("삭제 성공, 결과:", result);
				if (result === 1) {
					getList(page);
				} else {
					alert("댓글 삭제에 실패했습니다.");
				}
			},
			error: function(xhr, status, error) {
				console.error("삭제 요청 실패 상세:", {
					status: xhr.status,
					statusText: xhr.statusText,
					responseText: xhr.responseText,
					error: error
				});
				alert("댓글 삭제 중 오류가 발생했습니다.");
			}
		});
	});
});
