$(function () {
	const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
$("#write").click(function () {
	const content = $("#content").val().trim();
	if (!content) {
		alert("내용을 입력하세요");
		return false;
	}

	console.log("Content:", content);
	console.log("Board Num:", $("#board_num").val());
	console.log("Login ID:", $("#loginid").val());

	const buttonText = $("#write").text();
	let url;
	let data;

	if (buttonText === "등록") {
		const loginId = $("#loginid").val();
		url = "/inquiry/commentAdd";
		data = {
			ccontent: content,
			qno: $("#board_num").val()
		};
		console.log("Sending data:", data);

	} else {
		if (!cno) {
			alert("수정할 댓글을 선택하세요.");
			return;
		}
		url = "/inquiry/update";
		data = {
			cno: cno,
			ccontent: content
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
			if (result === 1) {
				$("#content").val('');
				getList(1);
			} else {
				alert("댓글 등록/수정에 실패했습니다.");
			}
		},
		error: function(xhr, status, error) {
			console.error("AJAX 오류:", error);
			alert("댓글 등록/수정 중 오류가 발생했습니다.");
		}
	});
});
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
});