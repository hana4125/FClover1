$(function () {
	const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
	const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

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
                qno: parseInt($("#board_num").val()),
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
                    $("#comment-list").empty();

                    $(rdata.qlist).each(function () {
                        let output = `
                        <div class="comment-container">
                            <div class="comment-main">
                                <span class="comment-author">${this.memberid}</span>
                                <span class="comment-content">${this.ccontent}</span>
                                <span class="comment-date">${this.cresponseat}</span>
                            </div>
                            ${rdata.isAdmin ? `
                            <div class="comment-actions">
                                <a href="#" class="text-button edit">수정</a>
                                <span class="separator">|</span>
                                <a href="#" class="text-button remove">삭제</a>
                                <input type='hidden' value='${this.cno}'>
                            </div>
                            ` : ''}
                        </div>
                    `;
                        $("#comment-list").append(output);
                    });
                } else {
                    $("#comment-list").empty();
                }
            }
        });
    }
	let cno = null; // 선택한 댓글의 ID 저장

// 수정 버튼 클릭 이벤트
	$(document).on("click", ".edit", function () {
		const commentContainer = $(this).closest(".comment-container");
		const commentContent = commentContainer.find(".comment-content").text();
		cno = commentContainer.find("input[type='hidden']").val(); // 댓글 ID 저장

		$("#content").val(commentContent);
		$("#write").text("수정"); // 버튼을 '수정'으로 변경
	});

	$("#write").click(function () {
		const content = $("#content").val().trim();
		if (!content) {
			alert("내용을 입력하세요");
			return false;
		}

		let url;
		let data;

		if ($("#write").text() === "등록") {
			url = "/inquiry/commentAdd";
			data = {
				ccontent: content,
				qno: $("#board_num").val()
			};
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
		}
		console.log("수정 요청 데이터:", data);

		$.ajax({
			type: "post",
			url: url,
			data: $.param(data),
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
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

    $(document).on("click", ".remove", function () {
        const cno = $(this).siblings("input[type='hidden']").val();
        if (!cno) {
            alert("삭제할 댓글의 ID를 찾을 수 없습니다.");
            return;
        }
        if (!confirm("정말 삭제하시겠습니까?"))
            return;

        $.ajax({
            type: "post",
            url: "/inquiry/delete",
            data: { cno: cno },
            beforeSend: function (xhr) {
                if (token && header) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function (result) {
                if (result === 1) {
                    alert("댓글이 삭제되었습니다.");
                    getList(1);
                } else if (result === 403) {
                    alert("삭제 권한이 없습니다.");
                } else {
                    alert("댓글 삭제에 실패했습니다.");
                }
            },
            error: function (xhr, status, error) {
                console.error("AJAX 오류:", error);
                alert("댓글 삭제 중 오류가 발생했습니다.");
            }
        });
    });

});