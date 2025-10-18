// 전역 변수
let currentTradeType = 'buy'; // 기본값: 구매내역
let currentPage = 1;
let currentKeyword = '';

// 페이지 로드 시 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 초기 데이터 로드
    loadTradeList();
    
    // 거래 타입 선택 이벤트
    const tradeTypeSelect = document.getElementById('tradeTypeSelect');
    if (tradeTypeSelect) {
        tradeTypeSelect.addEventListener('change', function() {
            currentTradeType = this.value;
            currentPage = 1; // 페이지 초기화
            loadTradeList();
        });
    }       
    
    // 엔터키로 검색
    const searchInput = document.getElementById('searchKeyword');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                currentKeyword = this.value.trim();
                currentPage = 1;
                loadTradeList();
            }
        });
    }
});

/**
 * 거래 내역 목록 로드
 */
function loadTradeList() {
    const params = new URLSearchParams({
        tradeType: currentTradeType,
        cp: currentPage,
        keyword: currentKeyword
    });
    
    fetch(`/myPage/orderList?${params}`)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            console.log('서버 응답 데이터:', data);
            console.log('goodsList:', data.goodsList);
            console.log('pagination:', data.pagination);
            
            if (data.goodsList && data.goodsList.length > 0) {
                renderOrderList(data.goodsList);
                console.log('renderOrderList 완료');
            } else {
                console.log('데이터가 없어서 빈 메시지 표시');
                showEmptyMessage();
            }
            
            // 페이지네이션은 데이터 유무와 관계없이 항상 렌더링
            renderPagination(data.pagination);
            console.log('renderPagination 완료');
        })
        .catch(error => {
            console.error('데이터 로드 실패:', error);
            showErrorMessage();
        });
}

/**
 * 빈 목록 메시지 표시
 */
function showEmptyMessage() {
    const container = document.querySelector('.myPage-orderDetail');
    if (container) {
        container.innerHTML = `
            <div style="text-align: center; padding: 60px 20px; color: #999;">
                <p style="font-size: 16px;">거래 내역이 없습니다.</p>
            </div>
        `;
    }
    
    // 페이지네이션 초기화
    const pagination = document.querySelector('.myPage-pagination');
    if (pagination) {
        pagination.innerHTML = '';
    }
}

/**
 * 에러 메시지 표시
 */
function showErrorMessage() {
    const container = document.querySelector('.myPage-orderDetail');
    if (container) {
        container.innerHTML = `
            <div style="text-align: center; padding: 60px 20px; color: #f44336;">
                <p style="font-size: 16px;">데이터를 불러오는 중 오류가 발생했습니다.</p>
                <button onclick="loadTradeList()" style="margin-top: 10px; padding: 8px 16px; cursor: pointer;">
                    다시 시도
                </button>
            </div>
        `;
    }
}

/**
 * 중고거래 목록 렌더링
 */
function renderOrderList(dataList) {
    console.log('renderOrderList 호출됨', dataList);

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

        if (item.PRODUCTIMAGEPATH) {
            imageDiv.innerHTML = `
                <img src="${item.PRODUCTIMAGEPATH}" 
                    alt="${item.PRODUCTNAME}" 
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
                <div class="text-size-12 bold-text productName" style="margin-bottom: 5px; line-height: 1.4;">상품명: ${item.PRODUCTNAME || '-'}</div>
                <div class="text-size-12 bold-text productNo" style="margin-bottom: 5px; line-height: 1.4;">제품 번호: ${item.PRODUCTNO || '-'}</div>
                <div class="text-size-12 bold-text tradeDate" style="line-height: 1.4;">거래일시: ${formatDate(item.TRADEDATE)}</div>
                
                <div class="text-size-12 bold-text buyerSeller" style="margin-top: 10px; line-height: 1.4;">
                    ${currentTradeType === 'buy' ? 
                        `판매자명: ${item.SELLERNAME || '-'}` : 
                        `구매자명: ${item.BUYERNAME || '-'}`
                    }
                </div>
            </div>
        `;

        // 버튼 그룹
        const buttonGroup = document.createElement('div');
        buttonGroup.style.display = 'flex';
        buttonGroup.style.flexDirection = 'column';
        buttonGroup.style.gap = '10px';
        buttonGroup.style.flexShrink = '0';
        buttonGroup.style.marginLeft = '0px';
        
        // ReviewFl에 따라 버튼 다르게 표시
        let reviewButtonHTML;
        if (item.REVIEWFL === 'Y') {
            reviewButtonHTML = `
                <button
                    class="myPage-reviewCompleted text-size-12 bold-text"
                    style="white-space: nowrap; cursor: default; background-color: #28a745; color: #666;"
                    disabled
                >
                    후기 작성 완료
                </button>
            `;
        } else {
           /* reviewButtonHTML = `
                <button
                    class="myPage-orderPostWrite text-size-12 bold-text"
                    id="review-btn-${item.productNo}" 
                    onclick="writeReview('${item.productNo}', '${item.buyerNo}', '${item.sellerNo}')"
                    style="white-space: nowrap; cursor: pointer;"
                >
                    거래 후기 작성
                </button>
            `;*/
              reviewButtonHTML = `
                <button
                    class="myPage-orderPostWrite text-size-12 bold-text" id="review-btn-${item.productNo}" 
                    data-boardNo="${item.PRODUCTNO}" data-memberNo="${item.BUYERNO}" 
                    onclick="openPopupJoonggo(this)" style="white-space: nowrap; cursor: pointer;"
                >
                    거래 후기 작성
                </button>
            `;
            
        }
        
        buttonGroup.innerHTML = reviewButtonHTML;

        rightContainer.appendChild(detailDiv);
        rightContainer.appendChild(buttonGroup);

        contentDiv.appendChild(imageDiv);
        contentDiv.appendChild(rightContainer);

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
    const paginationContainer = document.querySelector('.myPage-pagination');
    if (!paginationContainer || !pagination) {
        console.log('페이지네이션 컨테이너 없음 또는 pagination 데이터 없음');
        return;
    }
    
    console.log('페이지네이션 렌더링:', pagination);
    
    paginationContainer.innerHTML = '';
    
    let html = '<ul>';
    
    // << 처음 (항상 표시)
    html += `<li><a href="#" onclick="currentPage=1; loadTradeList(); return false;">&lt;&lt;</a></li>`;
    
    // < 이전 (항상 표시)
    html += `<li><a href="#" onclick="currentPage=${Math.max(1, pagination.prevPage)}; loadTradeList(); return false;">&lt;</a></li>`;
    
    // 페이지 번호 - 데이터가 없으면 1만 표시
    if (pagination.listCount === 0 || pagination.endPage === 0) {
        html += `<li><a class="current">1</a></li>`;
    } else {
        for (let i = pagination.startPage; i <= pagination.endPage; i++) {
            if (i === pagination.currentPage) {
                html += `<li><a class="current">${i}</a></li>`;
            } else {
                html += `<li><a href="#" onclick="currentPage=${i}; loadTradeList(); return false;">${i}</a></li>`;
            }
        }
    }
    
    // > 다음 (항상 표시)
    html += `<li><a href="#" onclick="currentPage=${Math.max(1, pagination.nextPage)}; loadTradeList(); return false;">&gt;</a></li>`;
    
    // >> 마지막 (항상 표시)
    html += `<li><a href="#" onclick="currentPage=${Math.max(1, pagination.maxPage)}; loadTradeList(); return false;">&gt;&gt;</a></li>`;
    
    html += '</ul>';
    
    paginationContainer.innerHTML = html;
}

/**
 * 날짜 포맷팅 함수
 */
function formatDate(dateString) {
    if (!dateString) return '-';
    
    const date = new Date(dateString);
    
    if (isNaN(date.getTime())) return dateString;
    
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

/**
 * 후기 작성 함수
 */
/*function writeReview(productNo, buyerNo, sellerNo) {
    console.log(`후기 작성: 제품번호 ${productNo}, 구매자 ${buyerNo}, 판매자 ${sellerNo}`);
 
    alert('거래 후기 작성 기능은 준비중입니다.');
}*/


// 중고물품 거래후기 팝업	
function openPopupJoonggo(btn) {
		
    const boardNo = btn.getAttribute('data-boardNo');    // 게시글 번호
    const memberNo = btn.getAttribute('data-memberNo');  // 회원 번호(구매자)

    // 팝업 URL에 파라미터 전달 (GET 방식)
    const url = `/review/joonggo?boardNo=${boardNo}&memberNo=${memberNo}`;
    
    // 팝업 열기 (옵션: 크기, 위치, 스크롤 등)
    window.open(
        url,
        'reviewPopup', 
        'width=700,height=700,top=200,left=500,scrollbars=yes,resizable=no'
    );
}