console.log("faq.js is loaded");

// 아코디언 토글 기능
function toggleAccordion(element) {
    const noticeItem = element.parentElement;
    const content = noticeItem.querySelector('.notice-content');
    const contentInner = noticeItem.querySelector('.notice-content-inner');

    // 다른 열린 아코디언 닫기
    document.querySelectorAll('.notice-item').forEach(item => {
        if (item !== noticeItem && item.classList.contains('active')) {
            item.classList.remove('active');
            item.querySelector('.notice-content').style.maxHeight = null;
        }
    });

    // 현재 아코디언 토글
    if (noticeItem.classList.contains('active')) {
        noticeItem.classList.remove('active');
        content.style.maxHeight = null;
    } else {
        noticeItem.classList.add('active');
        content.style.maxHeight = contentInner.scrollHeight + 50 + 'px';
    }
}

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