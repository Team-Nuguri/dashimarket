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

            const status = qnaItem.querySelector('.qna-header > span:first-child>span');
            const writeForm = qnaItem.querySelector('.answer-write-form');

            if (status) {
                if (status.classList.contains('status-pending') && writeForm) {
                    writeForm.classList.toggle('active');
                    closeOthers(writeForm);
                } else if (status.classList.contains('status-secret')) {
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

            // 제출 폼태그 찾음
            const answerFrm = commitBtn.closest('.answer-write-form');
            console.log(answerFrm);

            if (!answerFrm) return;

            // textarea 답글 내용 얻어오기
            const commentContent = answerFrm.querySelector('#commentAnswer').value;
            console.log(commentContent);
            if (commentContent.trim() == '') {
                alert("내용을 입력해주세요.");
                return;
            }

            // 부모 댓글 번호
            const commentNo = answerFrm.previousElementSibling.getAttribute('data-comment-no');
            console.log(commentNo);
            if (!commentNo) {
                alert("부모 댓글 못 찾음");
                return;
            }

            const data = {
                postNo: boardNo,
                commentContent: commentContent,
                parentCommentNo: commentNo
            }

            fetch("/comment/insert", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
                .then(resp => resp.text())
                .then(result => {

                    if (result == 1) {
                        alert("답글이 등록되었습니다.")
                        goodsQnaList(boardNo)
                    } else {
                        alert("답글 등록 실패")
                    }
                })
                .catch(e => console.log(e))

        }

    });
});



function goodsReviewList(boardNo, cp, canScroll = false, sort = 'basic') {

    const url = "/goods/review?boardNo=" + boardNo + "&cp=" + cp +"&sort=" + sort;

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

function goodsQnaList(boardNo, cp = 1, canScroll = false) {

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

    const maxStock = quantityInput.getAttribute('max');

    if(quantity < maxStock){
        quantity++;
        quantityInput.value = quantity;
        totalPriceElement.innerText = formatPrice(quantity * unitPrice);
        inputPrice.value = totalPriceElement.innerText;

    }else{
        alert(`현재 재고가 ${maxStock}개 남아 있습니다.`);
    }


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



// 삭제버튼 클릭 시
document.getElementById("deleteBtn")?.addEventListener("click", e => {

    if (confirm("정말 삭제하시겠습니까 ?")) {
        location.href = "/" + location.pathname.split("/")[1] + "/delete?boardNo=" + boardNo;
    }

})



// 수정 버튼 클릭 시 수정화면 전환
document.getElementById("editBtn")?.addEventListener("click", e => {

    location.href = "/goods/update?boardNo=" + boardNo;
})






// -------------------------------------------

/* qna */
const qnaWriteBtn = document.getElementById("qna-write-btn");
const qnaFrm = document.getElementById("qna-frm");
const commentContent = document.getElementById("commentContent");
const secretCheck = document.getElementById("secretCheck");

qnaWriteBtn.addEventListener("click", e => {

    qnaWriteBtn.classList.toggle("btn-click")
    qnaFrm.classList.toggle("display-none");


})


/* 취소 폼 태그 닫기 */
document.getElementById("cancel-btn")?.addEventListener("click", e=> {
    commentContent.value = '';
    secretCheck.checked = false;
    qnaFrm.classList.toggle("display-none");
})





// 체크 상태 변수
let isSecret = 'N';


//qna 제출 시
document.getElementById("qna-frm")?.addEventListener("submit", e => {
    e.preventDefault();

    console.log(commentContent.value);
    console.log(secretCheck.checked);

    if (secretCheck.checked) {
        isSecret = 'Y';
    } else {
        isSecret = 'N';
    }

    if (commentContent.value.trim() == '') {
        commentContent.value = ''
        alert("문의 내용을 작성해주세요.")
        commentContent.focus();
        return;
    }

    const data = {
        postNo: boardNo,
        commentContent: commentContent.value,
        isSecret: isSecret
    }

    fetch("/comment/insert", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(resp => resp.text())
        .then(result => {

            console.log(result)

            if (result == 1) {
                alert("문의가 등록되었습니다.")
                commentContent.value = '';
                secretCheck.checked = false;
                qnaFrm.classList.toggle("display-none");
                goodsQnaList(boardNo)
            } else {
                alert("문의 등록 실패")
            }
        })
        .catch(e => console.log(e))


})




// 목록으로 이동 클릭 시

document.getElementById("goToList").addEventListener("click", e => {

    e.preventDefault();

    location.href = "/goods"+location.search;
})



// 장바구니 담기 버튼 클릭 시 장바구니 insert


    

document.getElementById("shopping-cart").addEventListener("click", e=>{
    
    const cartQuantity = document.getElementById("quantity").value;
    const data= {
        boardNo : boardNo,
        quantity : cartQuantity
    }

    console.log(cartQuantity);

    fetch("/shoppingcart", {
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(data)
    })
    .then(resp => resp.text())
    .then(result => {

        if(result != 0){
            alert("장바구니에 상품이 추가되었습니다 ! ");
        }else{
            alert("장바구니에 상품 추가 실패했습니다. 다시 시도해 주세요 ㅠ");
        }

    })
    .catch()

    
})