console.log('myPage-goods.js');

// 페이지 로드 시 굿즈 거래내역 조회
document.addEventListener('DOMContentLoaded', function() {
    loadGoodsList();
    setupSearchFunction();
});

/**
 * 굿즈 거래내역 목록 조회
 */
function loadGoodsList() {
    fetch('/myPage/goods/list')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('조회된 데이터:', data);
            
            if (data && data.length > 0) {
                renderGoodsList(data);
            } else {
                showEmptyMessage();
            }
        })
        .catch(error => {
            console.error('데이터 조회 실패:', error);
            alert('거래 내역을 불러오는데 실패했습니다.');
            showEmptyMessage();
        });
}

/**
 * 굿즈 목록 렌더링
 */
function renderGoodsList(dataList) {
    const container = document.querySelector('.myPage-orderDetail');
    
    if (!container) {
        console.error('orderDetail 컨테이너를 찾을 수 없습니다.');
        return;
    }
    
    // 기존 내용 초기화
    container.innerHTML = '';
    
    dataList.forEach((item, index) => {
        // 각 주문 아이템 컨테이너
        const orderItemDiv = document.createElement('section');
        orderItemDiv.className = 'myPage-orderItem';
        orderItemDiv.setAttribute('data-index', index);
        
        // 구매 상태
        const statusDiv = document.createElement('div');
        statusDiv.className = 'myPage-orderStatus';
        statusDiv.innerHTML = `
            <div class="text-size-16 bold-text">구매상태</div>
            <span class="text-size-10 bold-text orderStatus">${item.구매상태 || '-'}</span>
        `;
        
        // 상품 상세 정보
        const detailDiv = document.createElement('div');
        detailDiv.innerHTML = `
            <div>
                <div class="text-size-12 bold-text goodsName">굿즈 상품명: ${item.제품명 || '-'}</div>
                <div class="text-size-12 bold-text goodsUnit">단가: ${formatPrice(item.단가)}원</div>
                <div class="text-size-12 bold-text goodsQty">수량: ${item.수량 || 0}개</div>
                <div class="text-size-12 bold-text goodsTotal">총금액: ${formatPrice(item.총가격)}원</div>
                <div class="text-size-12 bold-text goodsDate">결제날짜: ${formatDate(item.결제날짜)}</div>
            </div>
        `;
        
        // 버튼 그룹
        const buttonDiv = document.createElement('div');
        buttonDiv.className = 'myPage-orderButtonGroup';
        buttonDiv.innerHTML = `
            <button class="myPage-orderPostWrite text-size-12 bold-text" onclick="writeReview(${index})">거래 후기 작성</button>
            <button class="myPage-orderConfirm text-size-12 bold-text" onclick="confirmPurchase(${index})">구매확정</button>
            <button class="myPage-orderDelivery text-size-12 bold-text" onclick="checkDelivery(${index})">배송조회</button>
        `;
        
        // 요소들을 orderItemDiv에 추가
        orderItemDiv.appendChild(statusDiv);
        orderItemDiv.appendChild(detailDiv);
        orderItemDiv.appendChild(buttonDiv);
        
        // 컨테이너에 추가
        container.appendChild(orderItemDiv);
        
        // 구분선 추가 (마지막 아이템이 아닌 경우)
        if (index < dataList.length - 1) {
            const divider = document.createElement('div');
            divider.className = 'divider';
            container.appendChild(divider);
        }
    });
}

/**
 * 데이터가 없을 때 메시지 표시
 */
function showEmptyMessage() {
    const container = document.querySelector('.myPage-orderDetail');
    if (container) {
        container.innerHTML = `
            <div style="text-align: center; padding: 60px 20px; color: #999;">
                <p style="font-size: 16px; margin-bottom: 10px;">거래 내역이 없습니다.</p>
                <p style="font-size: 14px;">굿즈를 구매하시면 여기에 표시됩니다.</p>
            </div>
        `;
    }
}

/**
 * 가격 포맷팅 (천 단위 콤마)
 */
function formatPrice(price) {
    if (!price || price === 0) return '0';
    return Number(price).toLocaleString('ko-KR');
}

/**
 * 날짜 포맷팅 (YYYY-MM-DD)
 */
function formatDate(dateStr) {
    if (!dateStr) return '-';
    
    try {
        const date = new Date(dateStr);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    } catch (error) {
        console.error('날짜 포맷팅 실패:', error);
        return dateStr;
    }
}

/**
 * 검색 기능 설정
 */
function setupSearchFunction() {
    const searchInput = document.querySelector('.myPage-orderSearch input');
    
    if (searchInput) {
        searchInput.addEventListener('keyup', function(e) {
            if (e.key === 'Enter') {
                searchGoods(this.value);
            }
        });
    }
}

/**
 * 굿즈 검색
 */
function searchGoods(keyword) {
    if (!keyword || keyword.trim() === '') {
        loadGoodsList();
        return;
    }
    
    // 여기에 검색 API 호출 로직 추가 가능
    console.log('검색 키워드:', keyword);
    // TODO: 서버에 검색 요청 보내기
}

/**
 * 거래 후기 작성
 */
function writeReview(index) {
    console.log('거래 후기 작성:', index);
    alert('거래 후기 작성 기능은 준비중입니다.');
    // TODO: 후기 작성 페이지로 이동 또는 모달 표시
}

/**
 * 구매 확정
 */
function confirmPurchase(index) {
    if (confirm('구매를 확정하시겠습니까?')) {
        console.log('구매 확정:', index);
        alert('구매 확정 기능은 준비중입니다.');
        // TODO: 서버에 구매 확정 요청
    }
}

/**
 * 배송 조회
 */
function checkDelivery(index) {
    console.log('배송 조회:', index);
    alert('배송 조회 기능은 준비중입니다.');
    // TODO: 배송 조회 페이지로 이동 또는 모달 표시
}