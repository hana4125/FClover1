function deleteQuestion(qno) {
	if (confirm('정말 삭제하시겠습니까?')) {
		const token = document.querySelector('meta[name="_csrf"]').content;
		const header = document.querySelector('meta[name="_csrf_header"]').content;

		fetch('/inquiry/question/delete', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				[header]: token
			},
			body: JSON.stringify({ num: qno })
		})
			.then(response => {
				if (response.ok) {
					alert('삭제되었습니다.');
					window.location.href = '/inquiry/question';
				} else {
					alert('삭제 실패했습니다.');
				}
			})
			.catch(error => {
				console.error('Error:', error);
				alert('삭제 중 오류가 발생했습니다.');
			});
	}
}