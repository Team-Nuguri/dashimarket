// 목록으로 버튼 클릭 이벤트
const listBtn = document.getElementById('listBtn');
if (listBtn) {
    listBtn.addEventListener('click', () => {
        window.location.href = '/notice';
    });
}

// 수정 버튼 클릭 이벤트
const editBtn = document.getElementById('editBtn');
if (editBtn) {
    editBtn.addEventListener('click', () => {
        // URL에서 공지사항 번호 가져오기
        const noticeNo = getNoticeNoFromURL();
        if (noticeNo) {
            window.location.href = `/notice/edit/${noticeNo}`;
        }
    });
}

// 삭제 버튼 클릭 이벤트
const deleteBtn = document.getElementById('deleteBtn');
if (deleteBtn) {
    deleteBtn.addEventListener('click', () => {
        if (confirm('정말 이 공지사항을 삭제하시겠습니까?')) {
            const noticeNo = getNoticeNoFromURL();
            if (noticeNo) {
                deleteNotice(noticeNo);
            }
        }
    });
}

// URL에서 공지사항 번호 추출 함수
function getNoticeNoFromURL() {
    const pathParts = window.location.pathname.split('/');
    return pathParts[pathParts.length - 1];
}

// 공지사항 삭제 함수
function deleteNotice(noticeNo) {
    fetch(`/notice/delete/${noticeNo}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                alert('공지사항이 삭제되었습니다.');
                window.location.href = '/notice';
            } else {
                throw new Error('삭제 실패');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('공지사항 삭제에 실패했습니다.');
        });
}

// 이전글/다음글 링크 처리 (필요시)
const navLinks = document.querySelectorAll('.nav-title');
navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
        // 기본 동작 유지 (href로 이동)
        // 필요시 여기에 추가 로직 작성
    });
});

// 페이지 로드 시 조회수 증가 (필요시)
function increaseViewCount() {
    const noticeNo = getNoticeNoFromURL();
    if (noticeNo) {
        fetch(`/notice/view/${noticeNo}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    console.error('조회수 증가 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

// 페이지 로드 시 조회수 증가 실행 (필요한 경우 주석 해제)
// increaseViewCount();