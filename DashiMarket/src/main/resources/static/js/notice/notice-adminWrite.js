// 게시판 종류별 카테고리 매핑
const categoryMap = {
    'notice': [
        { value: 'notice-category', text: '[공지]' },
        { value: 'event-category', text: '[이벤트 관련]' },
        { value: 'fraud-category', text: '[사기예방]' },
        { value: 'policy-category', text: '[정책변경]' }
    ],
    'qna': [
        { value: 'trade-inquiry', text: '[거래문의]' },
        { value: 'usage-inquiry', text: '[이용문의]' },
        { value: 'account-inquiry', text: '[회원/계정]' },
        { value: 'etc-inquiry', text: '[기타]' }
    ],
    'dummy1': [
        { value: 'dummy1-category', text: '[더미1]' }
    ],
    'dummy2': [
        { value: 'dummy2-category', text: '[더미2]' }
    ]
};

// DOM 요소 가져오기
const boardSelect = document.getElementById('boardSelect');
const categorySelect = document.getElementById('categorySelect');
const writeForm = document.getElementById('writeForm');
const cancelBtn = document.querySelector('.btn-cancel');
const attachmentBtn = document.querySelector('.attachment-btn');
const attachmentArea = document.querySelector('.attachment-area');

// 선택된 파일을 저장할 변수
let selectedFile = null;

// 카테고리 옵션 업데이트 함수
function updateCategoryOptions(boardType) {
    // 기존 옵션 제거
    categorySelect.innerHTML = '';
    
    // 선택된 게시판 종류에 맞는 카테고리 추가
    const categories = categoryMap[boardType];
    categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category.value;
        option.textContent = category.text;
        categorySelect.appendChild(option);
    });
}

// 게시판 종류 변경 이벤트
boardSelect.addEventListener('change', function() {
    updateCategoryOptions(this.value);
});

// 페이지 로드 시 초기 카테고리 설정
document.addEventListener('DOMContentLoaded', function() {
    updateCategoryOptions(boardSelect.value);
});

// 폼 제출 이벤트
writeForm.addEventListener('submit', function(e) {
    e.preventDefault();
    
    if (confirm('작성된 게시글을 등록하시겠습니까?')) {
        // 여기에 실제 등록 로직 추가
        console.log('게시글 등록 처리');
        // 예: writeForm.submit(); 또는 AJAX 요청
    }
});

// 취소 버튼 클릭 이벤트
cancelBtn.addEventListener('click', function() {
    if (confirm('현재 작성된 게시글이 모두 지워집니다.')) {
        // 폼 초기화
        writeForm.reset();
        // 카테고리 재설정
        updateCategoryOptions(boardSelect.value);
        // 첨부파일 영역 초기화
        attachmentArea.innerHTML = '';
        selectedFile = null;
        // 또는 이전 페이지로 이동
        // window.history.back();
    }
});

// 첨부파일 버튼 클릭 이벤트
attachmentBtn.addEventListener('click', function(e) {
    e.preventDefault(); // 기본 동작 방지
    
    // 파일 input 요소 생성
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    
    // 파일 선택 이벤트
    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        
        if (file) {
            selectedFile = file;
            
            // 기존 첨부파일 초기화
            attachmentArea.innerHTML = '';
            
            // 파일 아이템 컨테이너 생성
            const fileItem = document.createElement('div');
            fileItem.style.display = 'flex';
            fileItem.style.alignItems = 'center';
            fileItem.style.gap = '10px';
            fileItem.style.padding = '5px 0';
            
            // 파일명 표시
            const fileName = document.createElement('span');
            fileName.textContent = file.name;
            fileName.style.fontSize = '13px';
            fileName.style.flex = '1';
            fileName.style.overflow = 'hidden';
            fileName.style.textOverflow = 'ellipsis';
            fileName.style.whiteSpace = 'nowrap';
            
            // 삭제 버튼 생성
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = '삭제';
            deleteBtn.type = 'button';
            deleteBtn.style.padding = '3px 10px';
            deleteBtn.style.fontSize = '12px';
            deleteBtn.style.backgroundColor = '#f44336';
            deleteBtn.style.color = 'white';
            deleteBtn.style.border = 'none';
            deleteBtn.style.borderRadius = '4px';
            deleteBtn.style.cursor = 'pointer';
            
            // 삭제 버튼 클릭 이벤트
            deleteBtn.addEventListener('click', function() {
                if (confirm('첨부파일을 삭제하시겠습니까?')) {
                    attachmentArea.innerHTML = '';
                    selectedFile = null;
                }
            });
            
            fileItem.appendChild(fileName);
            fileItem.appendChild(deleteBtn);
            attachmentArea.appendChild(fileItem);
            
        }
    });
    
    // 파일 선택 창 열기
    fileInput.click();
});