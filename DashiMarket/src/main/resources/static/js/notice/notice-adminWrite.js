console.log("notice-adminWrite.js loaded");

// DOM 요소 가져오기
const writeForm = document.getElementById('writeForm');
const cancelBtn = document.getElementById('cancelBtn');
const fileInput = document.getElementById('fileInput');
const attachmentArea = document.querySelector('.attachment-area');

// 페이지 모드 확인 (작성 or 수정)
const pageMode = document.getElementById('pageMode')?.value || 'write';
const noticeNo = document.getElementById('noticeNo')?.value || '';

let selectedFile = null;

console.log('페이지 모드:', pageMode);
console.log('게시글 번호:', noticeNo);

// 파일 선택 이벤트
fileInput.addEventListener('change', function(e) {
    const file = e.target.files[0];
    
    if (file) {
        // 파일 크기 체크 (10MB)
        const maxSize = 10 * 1024 * 1024; // 10MB
        if (file.size > maxSize) {
            alert('파일 크기는 10MB를 초과할 수 없습니다.');
            fileInput.value = '';
            return;
        }

        // 이미지 파일 확인
        if (!file.type.startsWith('image/')) {
            alert('이미지 파일만 업로드 가능합니다.');
            fileInput.value = '';
            return;
        }

        selectedFile = file;
        
        // 기존 첨부파일 초기화
        attachmentArea.innerHTML = '';
        
        // 파일 아이템 생성
        const fileItem = document.createElement('div');
        fileItem.style.display = 'flex';
        fileItem.style.alignItems = 'center';
        fileItem.style.gap = '10px';
        fileItem.style.padding = '8px 0';
        
        // 파일명 표시
        const fileName = document.createElement('span');
        fileName.textContent = file.name;
        fileName.style.fontSize = '13px';
        fileName.style.flex = '1';
        
        // 삭제 버튼
        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = '삭제';
        deleteBtn.type = 'button';
        deleteBtn.style.padding = '4px 12px';
        deleteBtn.style.fontSize = '12px';
        deleteBtn.style.backgroundColor = '#f44336';
        deleteBtn.style.color = 'white';
        deleteBtn.style.border = 'none';
        deleteBtn.style.borderRadius = '4px';
        deleteBtn.style.cursor = 'pointer';
        
        deleteBtn.addEventListener('click', function() {
            if (confirm('첨부파일을 삭제하시겠습니까?')) {
                attachmentArea.innerHTML = '';
                fileInput.value = '';
                selectedFile = null;
            }
        });
        
        fileItem.appendChild(fileName);
        fileItem.appendChild(deleteBtn);
        attachmentArea.appendChild(fileItem);
    }
});

// 폼 제출 이벤트
writeForm.addEventListener('submit', function(e) {
    const title = document.querySelector('input[name="admWriteTitle"]').value.trim();
    const content = document.querySelector('textarea[name="admWriteContent"]').value.trim();
    
    if (!title) {
        alert('제목을 입력하세요.');
        e.preventDefault();
        return false;
    }

    if (!content) {
        alert('내용을 입력하세요.');
        e.preventDefault();
        return false;
    }
    
    // 작성 모드와 수정 모드에 따라 다른 확인 메시지
    let confirmMessage;
    if (pageMode === 'edit') {
        confirmMessage = '게시글을 수정하시겠습니까?';
    } else {
        confirmMessage = '작성된 게시글을 등록하시겠습니까?';
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
    let redirectUrl;
    
    if (pageMode === 'edit') {
        // 수정 모드: 상세 페이지로 돌아가기
        cancelMessage = '수정을 취소하고 상세 페이지로 돌아가시겠습니까?';
        redirectUrl = '/notice/detail/' + noticeNo;
    } else {
        // 작성 모드: 목록으로 돌아가기
        cancelMessage = '작성을 취소하고 목록으로 돌아가시겠습니까?';
        redirectUrl = '/notice';
    }
    
    if (confirm(cancelMessage)) {
        window.location.href = redirectUrl;
    }
});