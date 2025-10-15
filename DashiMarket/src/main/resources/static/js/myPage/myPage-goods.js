console.log('myPage-goods.js');

let currentKeyword = ''; // 현재 검색어 저장

// 페이지 로드 시 굿즈 거래내역 조회
document.addEventListener('DOMContentLoaded', function() {
    loadGoodsList(1); // 첫 페이지 로드
    setupSearchFunction(); // 검색 기능 설정
});

/**
 * 검색 기능 설정
 */
function setupSearchFunction() {
    const searchInput = document.querySelector('.myPage-orderSearch input');
    
    if (searchInput) {
        searchInput.addEventListener('keyup', function(e) {
            if (e.key === 'Enter') {
                currentKeyword = this.value.trim();
                loadGoodsList(1); // 검색 시 1페이지부터
            }
        });
    }
}

/**
 * 굿즈 거래내역 목록 조회
 */
function loadGoodsList(cp = 1) {
    const keyword = currentKeyword || '';
    const url = `/myPage/api/goods/list?cp=${cp}${keyword ? '&keyword=' + encodeURIComponent(keyword) : ''}`;
    
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('조회된 데이터:', data);
            console.log('pagination 확인:', data.pagination);
            
            let goodsList, pagination;
            
            if (Array.isArray(data)) {
                goodsList = data;
                pagination = null;
                console.log('배열 형태 데이터');
            } else {
                goodsList = data.goodsList;
                pagination = data.pagination;
                console.log('객체 형태 데이터, pagination:', pagination);
            }
            
            if (goodsList && goodsList.length > 0) {
                renderGoodsList(goodsList);
                if (pagination) {
                    console.log('페이지네이션 렌더링 시작');
                    renderPagination(pagination);
                } else {
                    console.log('페이지네이션 없음');
                }
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
    console.log('renderGoodsList 호출됨', dataList);

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
        
        // 구매 상태
        const statusDiv = document.createElement('div');
        statusDiv.className = 'myPage-orderStatus';
        statusDiv.style.marginBottom = '15px';
        statusDiv.style.paddingLeft = '0';
        statusDiv.innerHTML = `
            <div class="text-size-16 bold-text">구매상태</div>
            <span class="text-size-10 bold-text orderStatus">${item.구매상태 || '-'}</span>
        `;
        
        // 메인 컨텐츠 영역
        const contentDiv = document.createElement('div');
        contentDiv.style.display = 'flex';
        contentDiv.style.paddingLeft = '0px';
        contentDiv.style.gap = '20px';
        contentDiv.style.alignItems = 'stretch';
        
        // 이미지
        const imageDiv = document.createElement('div');
        imageDiv.style.flexShrink = '0';
        imageDiv.style.width = '120px';
        
        if (item.IMAGEPATH) {
            imageDiv.innerHTML = `
                <img src="${item.IMAGEPATH}" 
                     alt="${item.제품명}" 
                     style="width: 120px; height: 120px; object-fit: cover; border-radius: 8px; border: 1px solid #ddd;">
            `;
        } else {
            imageDiv.innerHTML = `
                <div style="width: 120px; height: 120px; background: #f0f0f0; border-radius: 8px; display: flex; align-items: center; justify-content: center; color: #999; border: 1px solid #ddd;">
                    이미지 없음
                </div>
            `;
        }
        
        // 상품 정보와 버튼을 담는 컨테이너
        const rightContainer = document.createElement('div');
        rightContainer.style.flex = '1';
        rightContainer.style.display = 'flex';
        rightContainer.style.justifyContent = 'space-between';
        rightContainer.style.minWidth = '0';
        
        // 상품 상세 정보
        const detailDiv = document.createElement('div');
        detailDiv.style.flex = '1';
        detailDiv.style.minWidth = '0';
        detailDiv.style.display = 'flex';
        detailDiv.style.flexDirection = 'column';
        detailDiv.style.justifyContent = 'flex-start';
        detailDiv.innerHTML = `
            <div style="display: flex; flex-direction: column;">
                <div class="text-size-12 bold-text goodsName" style="margin-bottom: 5px; line-height: 1.4;">굿즈 상품명: ${item.제품명 || '-'}</div>
                <div class="text-size-12 bold-text goodsUnit" style="margin-bottom: 5px; line-height: 1.4;">단가: ${formatPrice(item.단가)}원</div>
                <div class="text-size-12 bold-text goodsQty" style="margin-bottom: 5px; line-height: 1.4;">수량: ${item.수량 || 0}개</div>
                <div class="text-size-12 bold-text goodsTotal" style="margin-bottom: 5px; line-height: 1.4;">총금액: ${formatPrice(item.총가격)}원</div>
                <div class="text-size-12 bold-text goodsDate" style="line-height: 1.4;">결제날짜: ${formatDate(item.결제날짜)}</div>
            </div>
        `;
        
        // 버튼 그룹
        const buttonGroup = document.createElement('div');
        buttonGroup.style.display = 'flex';
        buttonGroup.style.flexDirection = 'column';
        buttonGroup.style.gap = '10px';
        buttonGroup.style.flexShrink = '0';

        buttonGroup.style.marginLeft = '0px';

        buttonGroup.innerHTML = `
            <button 
                class="myPage-orderPostWrite text-size-12 bold-text" 
                id="review-btn-${item.orderItemId}" 
                onclick="writeReview('${item.orderItemId}', '${item.orderNo}')"
                ${item.구매상태 === '구매확정' ? '' : 'disabled'}
                style="white-space: nowrap; ${item.구매상태 === '구매확정' ? '' : 'opacity: 0.5; cursor: not-allowed;'}"
            >
                거래 후기 작성
            </button>
            <div style="display: flex; gap: 10px;">
                <button 
                    class="myPage-orderConfirm text-size-12 bold-text" 
                    id="confirm-btn-${item.orderItemId || 0}"
                    onclick="confirmPurchase('${item.orderItemId}', '${item.orderNo}')"
                    style="white-space: nowrap;"
                >
                    구매확정
                </button>
                <button 
                    class="myPage-orderDelivery text-size-12 bold-text" 
                    onclick="checkDelivery('${item.orderNo}', '${item.trackingNumber || ''}')"
                    style="white-space: nowrap;"
                >
                    배송조회
                </button>
            </div>
        `;
        
        rightContainer.appendChild(detailDiv);
        rightContainer.appendChild(buttonGroup);
        
        contentDiv.appendChild(imageDiv);
        contentDiv.appendChild(rightContainer);
        
        orderItemDiv.appendChild(statusDiv);
        orderItemDiv.appendChild(contentDiv);
        
        container.appendChild(orderItemDiv);
        
        // 구분선
        if (index < dataList.length - 1) {
            const divider = document.createElement('div');
            divider.className = 'divider';
            container.appendChild(divider);
        }
    });
}

/**
 * 페이지네이션 렌더링
 */
function renderPagination(pagination) {
    const paginationDiv = document.querySelector('.myPage-pagination');
    
    if (!paginationDiv) return;
    
    if (!pagination) {
        paginationDiv.innerHTML = '';
        return;
    }
    
    let html = '<ul>';
    
    // 처음
    html += `<li><a href="#" onclick="loadGoodsList(1); return false;">&lt;&lt;</a></li>`;
    
    // 이전
    html += `<li><a href="#" onclick="loadGoodsList(${pagination.prevPage}); return false;">&lt;</a></li>`;
    
    // 페이지 번호
    for (let i = pagination.startPage; i <= pagination.endPage; i++) {
        if (i === pagination.currentPage) {
            html += `<li><a class="current page-link">${i}</a></li>`;
        } else {
            html += `<li><a class="page-link" href="#" onclick="loadGoodsList(${i}); return false;">${i}</a></li>`;
        }
    }
    
    // 다음
    html += `<li><a href="#" onclick="loadGoodsList(${pagination.nextPage}); return false;">&gt;</a></li>`;
    
    // 마지막
    html += `<li><a href="#" onclick="loadGoodsList(${pagination.maxPage}); return false;">&gt;&gt;</a></li>`;
    
    html += '</ul>';
    
    paginationDiv.innerHTML = html;
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
    
    const paginationDiv = document.querySelector('.myPage-pagination');
    if (paginationDiv) {
        paginationDiv.innerHTML = '';
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
 * 거래 후기 작성
 */
function writeReview(orderItemId, orderNo) {
    console.log('거래 후기 작성:', orderItemId, orderNo);
    alert('거래 후기 작성 기능은 준비중입니다.');
}

/**
 * 구매 확정
 *//**
 * 구매 확정
 */
function confirmPurchase(orderItemId, orderNo) {
    if (confirm('구매를 확정하시겠습니까?')) {        
        fetch('/myPage/api/goods/confirm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `orderItemId=${orderItemId}`
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('구매가 확정되었습니다.');
                // 목록 새로고침하여 최신 상태 반영
                loadGoodsList(1);
                console.log(result);
            } else {
                alert(result.message || '구매 확정에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('구매 확정 실패:', error);
            alert('구매 확정 중 오류가 발생했습니다.');
        });
    }
}

/**
 * 배송 조회
 */
function checkDelivery(orderNo, trackingNumber) {
    console.log('배송 조회:', orderNo, trackingNumber);
    if (!trackingNumber) {
        alert('운송장 번호가 없습니다.');
        return;
    }
    alert('배송 조회 기능은 준비중입니다.'); 
}