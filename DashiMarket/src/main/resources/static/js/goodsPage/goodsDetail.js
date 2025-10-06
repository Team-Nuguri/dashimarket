console.log("goodsDetail.js loda . . .")


document.addEventListener("DOMContentLoaded", function () {

    // boardNo에 값이 확실히 담겼을 때에 비동기로 목록 조회 함수 호출
    if (typeof boardNo !== 'undefined') {
        goodsReviewList(boardNo, 1);
        goodsQnaList(boardNo, 1);
    }


    document.body.addEventListener('click', function (event) {

        // closest - event.target에서부터 시작해서 상위 요소로 이동하며 탐색
        // .qna-header인 요소를 찾음
        const header = event.target.closest('.qna-header');

        if (header) {
            event.stopPropagation(); // 중복 이벤트 방지

            const qnaItem = header.closest('.qna-item');
            if (!qnaItem) return;

            const closeOthers = (currentElement) => {
                document.querySelectorAll('.qna-item > .active').forEach(other => {
                    if (!other.closest('.qna-item').isEqualNode(currentElement.closest('.qna-item'))) {
                        other.classList.remove('active');
                    }
                });
            };

            const statusElement = qnaItem.querySelector('.qna-header > span:first-child');
            const writeForm = qnaItem.querySelector('.answer-write-form');


            if (statusElement) {
                if (statusElement.classList.contains('status-pending') && writeForm) {
                    writeForm.classList.toggle('active');
                    closeOthers(writeForm);

                } else if (statusElement.classList.contains('status-secret')) {
                    alert("비밀글입니다. 작성자 및 관리자만 내용을 확인할 수 있습니다.");
                }
            }
        }

        // 취소버튼 클릭 시 답글 등록 영역 닫히게
        const cancelBtn = event.target.closest('#comment-cancel-btn');
        if (cancelBtn) {
            event.preventDefault();
            event.stopPropagation();

            const form = cancelBtn.closest('.answer-write-form');
            if (form) {
                form.classList.remove('active');
            }
        }

        const commitBtn = event.target.closest('#comment-commit-btn');
        if (commitBtn) {
            event.preventDefault();
            console.log("Q&A 답변 등록 버튼 클릭됨. AJAX 처리 필요.");
        }

    });
});



function goodsReviewList(boardNo, cp, canScroll = false) {

    const url = "/goods/review?boardNo=" + boardNo + "&cp=" + cp;

    fetch(url)
        .then(resp => resp.text())
        .then(reviewList => {

            document.getElementById("review-list").outerHTML = reviewList;


            if (canScroll === true) {

                const goodsReviewList = document.getElementById("goods-review");

                if (goodsReviewList) {
                    goodsReviewList.scrollIntoView({
                        behavior: 'smooth'
                    });
                }
            }


        })
        .catch(e => console.log(e))

}

function goodsQnaList(boardNo, cp, canScroll = false) {

    const url = "/goods/qna?boardNo=" + boardNo + "&cp=" + cp;

    fetch(url)
        .then(resp => resp.text())
        .then(qna => {

            document.getElementById("qna-list").outerHTML = qna;

            // 페이지 전환 시 영역 유지
            if (canScroll === true) {

                const goodsQnaList = document.getElementById("goods-qna");

                if (goodsQnaList) {
                    goodsQnaList.scrollIntoView({
                        behavior: 'smooth'
                    });
                }
            }


        })
        .catch(e => console.log(e))
}












/* 수정 삭제 */
document.getElementById("ud-menu").addEventListener("click", () => {

    document.querySelector(".dropdown-menu").classList.toggle("show");


})



/* 상품 수량 가격 */
const totalPriceElement = document.querySelector(".total-price");
const minusBtn = document.querySelector(".minus");
const plusBtn = document.querySelector(".plus");
const quantityInput = document.getElementById("quantity");
const inputPrice = document.getElementById("total-price");

// 숫자가 아닌 문자 ''으로 
const unitPrice = parseInt(totalPriceElement.innerText.replace(/[^0-9]/g, ''));

// 버튼 클릭 이벤트
minusBtn.addEventListener("click", () => {
    let quantity = parseInt(quantityInput.value);
    if (quantity > 1) {
        quantity--;
        quantityInput.value = quantity;
        totalPriceElement.innerText = formatPrice(quantity * unitPrice);
        inputPrice.value = totalPriceElement.innerText;
    }
});

plusBtn.addEventListener("click", () => {
    let quantity = parseInt(quantityInput.value);
    quantity++;
    quantityInput.value = quantity;
    totalPriceElement.innerText = formatPrice(quantity * unitPrice);
    inputPrice.value = totalPriceElement.innerText;

});

/* 한국 원화 형식으로 바꿔주는 함수 */
function formatPrice(number) {
    return number.toLocaleString('ko-KR') + ' 원';
}

/* ------------------------------------ */
/* 비슷한 상품 목록 */
const leftBtn = document.getElementById("left-out-btn");
const rightBtn = document.getElementById("right-out-btn");
const slideImage = document.getElementById("image-slide");

const totalItems = document.getElementsByClassName("similar-items").length;
const itemsPerSlide = 5;
const totalPages = Math.ceil(totalItems / itemsPerSlide); 
let index = 0;


slideImage.style.transition = 'all 0.4s'; 

rightBtn.addEventListener("click", () => {
    if (index < totalPages - 1) {
        index++;
        
        slideImage.style.transform = `translateX(-${1200 * index}px)`;
        
    }
});


leftBtn.addEventListener("click", () => {
    // 0보다 클 때만 index를 감소시키고 이동
    if (index > 0) {
        index--;
        slideImage.style.transform = `translateX(-${1200 * index}px)`;

    }
});


/* ---------------------------------------- */
/* 리뷰 영역 정렬 카테고리 */

const sortcate = document.querySelectorAll("#goods-review>div:nth-child(3)>a");

for (let c of sortcate) {

    c.addEventListener("click", e => {

        e.preventDefault();

        for (let cate of sortcate) {
            cate.classList.add("text-opacity");
        }
        c.classList.remove("text-opacity");

    })

}




/* ------------------------------------------- */
/* qna */
const qnaWriteBtn = document.getElementById("qna-write-btn");
const qnaFrm = document.getElementById("qna-frm");

qnaWriteBtn.addEventListener("click", e => {

    qnaWriteBtn.classList.toggle("btn-click")
    qnaFrm.classList.toggle("display-none");


})




const buyBtn = document.getElementById("buy");
const cartBtn = document.getElementById("shopping-cart");
const soldOut = document.getElementById("sold-out");

buyBtn.addEventListener("click", e => {

    if (soldOut.classList.contains('show')) {

        alert("현재 상품은 재고소진으로 인해 구매가 불가능합니다.");
        return;
    }

    /* 구매 버튼 클릭 시 서버 ~ */
})

cartBtn.addEventListener("click", e => {

    if (soldOut.classList.contains('show')) {

        alert("현재 상품은 재고소진으로 인해 구매가 불가능합니다.");
        return;
    }

    /* 장바구니 버튼 클릭 시 비동기로 장바구니 테이블 insert ~ */
})




document.getElementById("deleteBtn")?.addEventListener("click", e => {

    if(confirm("정말 삭제하시겠습니까 ?")){
        location.href= "/" + location.pathname.split("/")[1] +"/delete?boardNo="+boardNo;
    }
    
})





