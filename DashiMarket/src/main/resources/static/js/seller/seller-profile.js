// DOM 요소
const tabButtons = document.querySelectorAll('.tab-btn');
const productsContainer = document.getElementById('products-container');
const reviewsContainer = document.getElementById('reviews-container');
const productList = productsContainer.querySelector('.product-list');
const reviewList = reviewsContainer.querySelector('.review-list');

// 데이터 로드 여부 체크
let productsLoaded = false;
let reviewsLoaded = false;

// 탭 전환
tabButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        const tab = btn.dataset.tab;
        
        // 활성 탭 변경
        tabButtons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        
        // 컨텐츠 영역 전환
        if (tab === 'products') {
            productsContainer.classList.add('active');
            reviewsContainer.classList.remove('active');
            
            if (!productsLoaded) {
                loadProducts();
            }
        } else if (tab === 'reviews') {
            reviewsContainer.classList.add('active');
            productsContainer.classList.remove('active');
            
            if (!reviewsLoaded) {
                loadReviews();
            }
        }
    });
});

// 페이지 로드 시 판매 물품 자동 조회
window.addEventListener('DOMContentLoaded', () => {
    loadProducts();
});

// 판매 물품 조회
function loadProducts() {
    fetch(`/seller/${memberNo}/products`)
        .then(resp => resp.json())
        .then(products => {
            productsContainer.querySelector('.loading').style.display = 'none';
            
            if (products.length === 0) {
                productList.innerHTML = '<p class="no-data">등록된 판매 물품이 없습니다.</p>';
                return;
            }
            
            let html = '';
            products.forEach(product => {
                const mainImage = product.imageList && product.imageList.length > 0 
                    ? product.imageList[0].imagePath + product.imageList[0].imageRename 
                    : '/images/common/no-image.png';
                
                const price = product.joonggoPrice > 0 
                    ? Number(product.joonggoPrice).toLocaleString() + '원' 
                    : '나눔';
                
                html += `
                    <div class="product-item" onclick="location.href='/joonggo/${product.joonggoNo}'">
                        <div class="product-image">
                            <img src="${mainImage}" alt="${product.joonggoTitle}">
                        </div>
                        <div class="product-info">
                            <p class="product-title">${product.joonggoTitle}</p>
                            <p class="product-price">${price}</p>
                            <p class="product-date">${product.joonggoCreateDate}</p>
                        </div>
                    </div>
                `;
            });
            
            productList.innerHTML = html;
            productsLoaded = true;
        })
        .catch(error => {
            console.error('판매 물품 조회 실패:', error);
            productsContainer.querySelector('.loading').style.display = 'none';
            productList.innerHTML = '<p class="error">판매 물품을 불러오는데 실패했습니다.</p>';
        });
}

// 거래후기 조회
function loadReviews() {
    fetch(`/seller/${memberNo}/reviews`)
        .then(resp => resp.json())
        .then(reviews => {
            reviewsContainer.querySelector('.loading').style.display = 'none';
            
            if (reviews.length === 0) {
                reviewList.innerHTML = '<p class="no-data">작성된 거래후기가 없습니다.</p>';
                return;
            }
            
            let html = '';
            reviews.forEach(review => {
                const profileImg = review.buyerProfileImg 
                    ? review.buyerProfileImg 
                    : '/images/common/user.png';
                
                // 별점 생성
                let stars = '';
                for (let i = 1; i <= 5; i++) {
                    if (i <= review.rating) {
                        stars += '<i class="fa-solid fa-star"></i>';
                    } else {
                        stars += '<i class="fa-regular fa-star"></i>';
                    }
                }
                
                html += `
                    <div class="review-item">
                        <div class="review-header">
                            <div class="reviewer-info">
                                <img src="${profileImg}" class="reviewer-img">
                                <div>
                                    <p class="reviewer-name">${review.buyerNickname}</p>
                                    <div class="review-stars">${stars}</div>
                                </div>
                            </div>
                            <span class="review-date">${review.reviewDate}</span>
                        </div>
                        <p class="review-content">${review.comment}</p>
                    </div>
                `;
            });
            
            reviewList.innerHTML = html;
            reviewsLoaded = true;
        })
        .catch(error => {
            console.error('거래후기 조회 실패:', error);
            reviewsContainer.querySelector('.loading').style.display = 'none';
            reviewList.innerHTML = '<p class="error">거래후기를 불러오는데 실패했습니다.</p>';
        });
}
