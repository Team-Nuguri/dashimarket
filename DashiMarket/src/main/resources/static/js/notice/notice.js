console.log("notice.js is loaded");

// 필터 버튼 기능
document.querySelectorAll('.filter-nav button').forEach(btn => {
    btn.addEventListener('click', function () {
        document.querySelectorAll('.filter-nav button').forEach(b => {
            b.removeAttribute('aria-current');
        });
        this.setAttribute('aria-current', 'page');
    });
});

// 페이지네이션 버튼 기능
document.querySelectorAll('.pagination button:not(.pagination-btn)').forEach(btn => {
    btn.addEventListener('click', function () {
        document.querySelectorAll('.pagination button:not(.pagination-btn)').forEach(b => {
            b.removeAttribute('aria-current');
        });
        this.setAttribute('aria-current', 'page');
    });
});