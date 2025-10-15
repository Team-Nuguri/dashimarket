console.log("faq-adminWrite.js loaded");

// DOM 요소 가져오기
const faqWriteForm = document.getElementById('faqWriteForm');
const cancelBtn = document.getElementById('cancelBtn');

// 페이지 모드 확인 (작성 or 수정)
const pageMode = document.getElementById('pageMode')?.value || 'write';
const faqNo = document.getElementById('faqNo')?.value || '';

console.log('페이지 모드:', pageMode);
console.log('FAQ 번호:', faqNo);

// 폼 제출 이벤트
faqWriteForm.addEventListener('submit', function(e) {
    const question = document.querySelector('input[name="faqQuestion"]').value.trim();
    const answer = document.querySelector('textarea[name="faqAnswer"]').value.trim();
    
    if (!question) {
        alert('질문을 입력하세요.');
        e.preventDefault();
        return false;
    }

    if (!answer) {
        alert('답변을 입력하세요.');
        e.preventDefault();
        return false;
    }
    
    // 작성 모드와 수정 모드에 따라 다른 확인 메시지
    let confirmMessage;
    if (pageMode === 'edit') {
        confirmMessage = 'FAQ를 수정하시겠습니까?';
    } else {
        confirmMessage = '작성된 FAQ를 등록하시겠습니까?';
    }
    
    if (!confirm(confirmMessage)) {
        e.preventDefault();
        return false;
    }
    
    // 폼 제출 (서버로 전송)
    return true;
});

// 취소 버튼 이벤트
cancelBtn.addEventListener('click', function() {
    let cancelMessage;
    
    if (pageMode === 'edit') {
        // 수정 모드: 목록으로 돌아가기 (상세 페이지 없음)
        cancelMessage = '수정을 취소하고 목록으로 돌아가시겠습니까?';
    } else {
        // 작성 모드: 목록으로 돌아가기
        cancelMessage = '작성을 취소하고 목록으로 돌아가시겠습니까?';
    }
    
    if (confirm(cancelMessage)) {
        window.location.href = '/faq';  // 항상 목록으로 이동
    }
});
