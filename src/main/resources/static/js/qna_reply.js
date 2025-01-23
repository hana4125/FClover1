$(function () {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");

	$("#comment table").hide();
	let page = 1;
	const count = parseInt($("#count").text());

	if (count != 0) {
		getList(1);
	} else {
		$("#message").text("등록된 댓글이 없습니다.");
	}

	function getList(currentPage) {
		$.ajax({
			type: "post",
			url: "/inquiry/qlist",
			data: {
				"qno": $("#board_num").val(),
				"page": currentPage
			},
			beforeSend: function (xhr) {
				if(token && header){
				xhr.setRequestHeader(header, token);
				}
			},
			dataType: "json",
			success: function (rdata) {
				if (rdata.qlist && rdata.qlist.length > 0) {
					$("#comment table").show(); // 문서가 로딩될 때 hide() 했던 부분을 보이게 합니다.
					$("#comment tbody").empty();
					$(rdata.qlist).each(function () {
					let output = '';
					let img = '';
					if ($("#loginid").text() == this.memberid) {
							img = "<span class='edit' style='cursor: pointer;' title='edit'>수정</span>"
								+ "<span class='remove' style='cursor: pointer;' title='remove'>삭제</span>"
								+ "<input type='hidden' value='" + this.cno + "'>";
						}
						output += "<tr><td>" + this.memberid + "</td>";
						output += "<td></td>";
						output += "<td>" + this.cresponseat + img + "</td></tr>";
						$("#comment tbody").append(output);
						$("#comment tbody tr:last").find("td:nth-child(2)").text(this.ccontent);
					});

					if (rdata.qlist > rdata.qlist.length) { // 전체 댓글 갯수 > 현재까지 보여준 댓글 갯수
						$("#message").text("더보기");
					} else {
						$("#message").text("");
					}
				} else {
					$("#comment tbody").empty();
					$("#comment table").hide();
				}
			}
		}); // ajax end
	}

	// 글자수 제한하는 이벤트
	$("#content").on("keyup", function () {
		$(".float-left").text($(this).val().length + "/300");
	});

	// 등록 또는 수정완료 버튼을 클릭한 경우
	$("#write").click(function () {
		const content = $("#content").val().trim();
		if (!content) {
			alert("내용을 입력하세요");
			return false;
		}
		const buttonText = $("#write").text(); // 버튼의 라벨로 add할지 update할지 결정
		$(".float-left").text("300");

		if (buttonText == "등록") { // 댓글을 추가하는 경우
			url = "/inquiry/add",
			data = {
				"ccontent": content,
				"memberid": $("body > div:nth-child(1) > div.bg-light.mb-3 > div > div > a:nth-child(1) > span").text(),
				"qno": $("#board_num").val()
			}
		} else { // 댓글을 수정하는 경우
			url = "/inquiry/update";
			data = {
				"cno": cno,
				"ccontent": content
			};
			$("#write").text("등록"); // 다시 등록으로 변경
			$("#comment .cancel").remove(); // 취소 버튼 삭제
		}

		$.ajax({
			type: "post",
			url: url,
			data: data,
			beforeSend: function (xhr) {
				// 데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
				xhr.setRequestHeader(header, token);
			},
			success: function (result) {
				$("#content").val('');
				if (result == 1) {
					getList(page); // 등록, 수정완료 후 해당 페이지 보여줍니다.
				}
			}
		}); // ajax end
	}); // $("#write") end

	// 더보기를 클릭하면 page 내용이 추가로 보여집니다.
	$("#message").click(function () {
		getList(++page);
	});

	// 수정 버튼을 클릭하는 경우
	let cno;

	$("#comment").on('click', '.edit', function () {
		const before = $(this).parent().prev().text(); // 선택한 내용을 가져온다.
		$("#content").focus().val(before); // textarea에 수정전 내용을 보여준다.

		cno = $(this).next().next().val(); // 수정할 댓글번호를 저장합니다.
		$("#write").text("수정완료"); // 등록 버튼의 라벨을 '수정완료'로 변경한다.

		if (!$("#write").prev().is(".cancel")) {
			$("#write").before('<button class="btn btn-danger float-right cancel">취소</button>');
		}
		$("#comment tbody tr").css('background', 'white');
		$(this).parent().parent().css('background', 'lightgray');

		$(".float-left").text(before.length + "/50");

	});

	// 취소를 클릭하는 경우
	$("#comment").on('click', '.cancel', function () {
		$("#comment tbody tr").css('background', 'white');
		$(this).remove();
		$("#write").text("등록");
		$("#content").val('');
		$(".float-left").text('총 50');
	});

	// 삭제 버튼을 클릭하는 경우
	$("#comment").on('click', '.remove', function () {
		if (!confirm("정말 삭제하시겠습니까?")) {
			return;
		}
		const cno = $(this).next().val(); // 댓글번호
		$.ajax({
			type: "post",
			url: "/inquiry/delete",
			data: {
				"cno": cno
			},
			beforeSend: function (xhr) {
				// 데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
				xhr.setRequestHeader(header, token);
			},
			success: function (result) {
				if (result == 1) {
					getList(page); // 삭제 후 해당 페이지의 내용을 보여준다.
				}
			}
		})
	});
});
