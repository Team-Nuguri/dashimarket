console.log("notice.js is loaded");

// 관리자 글쓰기 버튼
const adminWriteBtn = document.getElementById('admin-writeBtn');
if (adminWriteBtn) {
    adminWriteBtn.addEventListener('click', () => {
        window.location.href = '/notice/write';
    });
}

// 검색 폼 제출 이벤트
const searchForm = document.querySelector('.search-form');
if (searchForm) {
    searchForm.addEventListener('submit', function (e) {
        const searchInput = document.getElementById('search-input');
        const query = searchInput.value.trim();

        // 검색어가 비어있으면 전체 목록으로 이동
        if (!query) {
            e.preventDefault();
            window.location.href = '/notice';
            return;
        }
    });
}

// 공지사항 행 클릭 시 상세 페이지 이동 (th:onclick과 중복 방지)
// Thymeleaf에서 이미 처리하므로 이 부분은 제거하거나 주석 처리
/*
document.querySelectorAll('.notice-table tbody tr').forEach(row => {
    row.addEventListener('click', function() {
        const noticeNo = this.querySelector('.notice-no').textContent.trim();
        if (noticeNo) {
            window.location.href = `/notice/detail/${noticeNo}`;
        }
    });
});
*/

// 페이지 로드 시 현재 페이지 하이라이트 (Thymeleaf에서 처리)
// aria-current="page" 속성이 이미 설정되어 있으므로 추가 작업 불필요

// 검색어 하이라이트 (선택 사항)
function highlightSearchTerm() {
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get('query');

    if (query && query.trim() !== '') {
        const titles = document.querySelectorAll('.notice-title a');
        const searchTerm = query.trim().toLowerCase();

        titles.forEach(title => {
            const text = title.textContent;
            const lowerText = text.toLowerCase();
            const index = lowerText.indexOf(searchTerm);

            if (index !== -1) {
                const before = text.substring(0, index);
                const match = text.substring(index, index + searchTerm.length);
                const after = text.substring(index + searchTerm.length);

                title.innerHTML = before + '<mark style="background-color: #FFC800; padding: 2px 4px; border-radius: 2px;">' + match + '</mark>' + after;
            }
        });
    }
}

// 페이지 로드 완료 후 검색어 하이라이트 실행
window.addEventListener('DOMContentLoaded', highlightSearchTerm);