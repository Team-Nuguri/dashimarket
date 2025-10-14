console.log("faq.js is loaded");

// 아코디언 토글 + 조회수 증가
function toggleAccordion(element) {
    const noticeItem = element.parentElement;
    const content = noticeItem.querySelector('.notice-content');
    const contentInner = noticeItem.querySelector('.notice-content-inner');
    const faqNo = element.getAttribute('data-faq-no');

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
        content.style.maxHeight = contentInner.scrollHeight + 100 + 'px';
        
        // 조회수 증가 (아코디언 열릴 때)
        if (faqNo) {
            increaseViewCount(faqNo);
        }
    }
}

// 조회수 증가 기능
function increaseViewCount(faqNo) {
    fetch(`/faq/increaseView/${faqNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => {
        if (response.ok) {
            console.log('조회수 증가');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

// FAQ 수정 버튼
function editFaq(button) {
    const faqNo = button.getAttribute('data-faq-no');
    location.href = `/faq/edit/${faqNo}`;
}

// FAQ 삭제 기능
function deleteFaq(button) {
    const faqNo = button.getAttribute('data-faq-no');
    
    if (confirm('정말 이 FAQ를 삭제하시겠습니까?')) {
        fetch(`/faq/delete/${faqNo}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(response => {
            if (response.ok) {
                alert('FAQ가 삭제되었습니다.');
                location.reload();
            } else {
                throw new Error('삭제 실패');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('FAQ 삭제에 실패했습니다.');
        });
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
