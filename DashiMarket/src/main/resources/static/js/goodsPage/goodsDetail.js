const imageList = document.querySelectorAll("#imageSlide img");
const imglength = imageList.length;

let currentIndex = 0;
const width  = 500;

const imageslide = document.getElementById("imageSlide");

const dots = document.getElementsByClassName("dot");


/* 오른쪽 버튼 클릭 시 */
document.getElementById("right-btn").addEventListener("click", e => {

    if(currentIndex < imglength-1){

        currentIndex++;
        
        for (let dot of dots) {

            dot.classList.add("opacity");
            
        }
        dots[currentIndex].classList.remove("opacity")

        imageslide.style.transform = `translateX(-${width * currentIndex}px)`
        imageslide.style.transition = "0.5s"
    }
    


})

/* 왼쪽 버튼 클릭 시 */
document.getElementById("left-btn").addEventListener("click", () => {

    if(currentIndex > 0){
        currentIndex--;
        for (let dot of dots) {

            dot.classList.add("opacity");
            
        }
        dots[currentIndex].classList.remove("opacity")
        imageslide.style.transform = `translateX(-${width * currentIndex}px)`
        imageslide.style.transition = "0.5s"
    }
})


/* dot 클릭 시 이미지 이동 */
for (let i = 0; i < imglength; i++) {
    
    dots[i].addEventListener("click", () => {
        for (let j = 0; j < imglength; j++) {
            dots[j].classList.add("opacity");
        }

        imageslide.style.transform = `translateX(-${width * i}px)`
        imageslide.style.transition = "0.5s"
        dots[i].classList.remove("opacity");
    })

    
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

const similarItem = document.getElementsByClassName("similar-items").length / 4;
let index = 0;


rightBtn.addEventListener("click", () => {
    index++;
    if(index < similarItem-1){

        slideImage.style.transform = `translateX(-${1200 * index}px)`;
        slideImage.style.transition = 'all 0.4s'
        
    }


})
leftBtn.addEventListener("click", () => {
    if(index > 0){
        index--;

        slideImage.style.transform = `translateX(-${1200 * index}px)`
        slideImage.style.transition = 'all 0.4s'
    }


})



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


document.addEventListener('DOMContentLoaded', function() {
    const qnaHeaders = document.querySelectorAll('.qna-header');

    
    qnaHeaders.forEach(header => {
        header.addEventListener('click', function() {
            
            const qnaItem = this.closest('.qna-item');
            
            // 2. 현재 항목의 답변 상태를 확인
            const statusElement = qnaItem.querySelector('.qna-header > span:first-child'); 
            
            // 3. 답변 내용 영역과 답변 작성 폼 영역을 찾가
            const answerContent = qnaItem.querySelector('.qna-answer');
            const writeForm = qnaItem.querySelector('.answer-write-form');
            
            // 4. 모든 열린 답변/폼을 닫는 함수
            const closeOthers = (currentElement) => {
                document.querySelectorAll('.qna-answer, .answer-write-form').forEach(other => {
                    if (other !== currentElement && other.classList.contains('active')) {
                        other.classList.remove('active');
                    }
                });
            };

            
            if (statusElement && statusElement.classList.contains('status-pending')) {
                // 4-1. 미답변 상태: 답변 작성 폼을 토글하고 다른 항목을 닫기
                writeForm.classList.toggle('active');
                closeOthers(writeForm);

            } else if (statusElement && statusElement.classList.contains('status-completed')) {
                // 4-2. 답변 완료 상태: 답변 내용을 토글하고 다른 항목을 닫기
                answerContent.classList.toggle('active');
                closeOthers(answerContent);
            } else if (statusElement && statusElement.classList.contains('status-secret')) {
                
                alert("비밀글입니다. 작성자 및 관리자만 내용을 확인할 수 있습니다.");
                
                return; 
            }
            
        });
    });
    
    // 5. '취소' 버튼 클릭 시 폼 닫기 로직 (이벤트 전파 방지 필수)
    document.querySelectorAll('#comment-cancel-btn').forEach(cancelBtn => {
        cancelBtn.addEventListener('click', function(event) {
            // 버튼의 기본 동작(폼 리셋/이동)과 부모 요소(li)로의 이벤트 전파를 막습니다.
            event.preventDefault(); 
            event.stopPropagation(); 
            
            // 폼을 닫습니다.
            this.closest('.answer-write-form').classList.remove('active');
        });
    });

    // 6. '등록' 버튼 클릭 시 폼 제출 처리 로직 (필요 시 AJAX로 변경)
    document.querySelectorAll('#comment-commit-btn').forEach(submitBtn => {
        submitBtn.addEventListener('click', function(event) {
        
        
        });
    });
});








