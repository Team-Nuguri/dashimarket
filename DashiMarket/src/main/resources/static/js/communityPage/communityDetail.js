console.log("communityDetail.js");

/* 정렬 */
const sort = document.querySelectorAll(".sort-area a");
let isSort = sort[0];


sort.forEach(link => {
    link.addEventListener("click", (e) => {
    e.preventDefault();

    /* 정렬 데이터 가져오기 ex) data-sortType = "latest" */
    let sortType = link.dataset.sortType;

    if(isSort) {
        isSort.classList.remove("bold-text");
    }

    link.classList.add("bold-text");
    isSort = link;

    /* 비동기 요청 */
    selectCommentList("sort", sortType);

    })
})

/* 이미지 슬라이드 */
const imageList = document.querySelectorAll("#image-slide img");
const imgLength = imageList.length;

let currentIndex = 0;
const width  = 400;

const imageSlide = document.getElementById("image-slide");
const dots = document.getElementsByClassName("dot");

/* 오른쪽 버튼 클릭 시 */
const rightBtn = document.getElementById("right-btn");
if(rightBtn != null) {

    rightBtn.addEventListener("click", e => {
    
        if(currentIndex < imgLength-1){
    
            currentIndex++;
            
            for (let dot of dots) {
    
                dot.classList.add("opacity");
                
            }
            dots[currentIndex].classList.remove("opacity")
    
            imageSlide.style.transform = `translateX(-${width * currentIndex}px)`
            imageSlide.style.transition = "0.5s"
        }
    });
}

/* 왼쪽 버튼 클릭 시 */
const leftBtn = document.getElementById("left-btn");
if(leftBtn != null) {
    leftBtn.addEventListener("click", () => {
    
        if(currentIndex > 0){
            currentIndex--;
            for (let dot of dots) {
    
                dot.classList.add("opacity");
                
            }
            dots[currentIndex].classList.remove("opacity")
            imageSlide.style.transform = `translateX(-${width * currentIndex}px)`
            imageSlide.style.transition = "0.5s"
        }
    })

}


/* dot 클릭 시 이미지 이동 */
for (let i = 0; i < imgLength; i++) {
    
    dots[i].addEventListener("click", () => {
        for (let j = 0; j < imgLength; j++) {
            dots[j].classList.add("opacity");
        }

        imageSlide.style.transform = `translateX(-${width * i}px)`
        imageSlide.style.transition = "0.5s"
        dots[i].classList.remove("opacity");
    })
    
}

/* 답글 */
const replyBtn = document.querySelectorAll(".reply-btn");

    replyBtn.forEach(button => {
        button.addEventListener('click', e => {
            /* 버튼이 속한 댓글 찾기 */
            const parentComment = e.target.closest('.parent-comment');

            /* 해당 댓글 아래의 답글 작성란  */
            const replyForm = parentComment.nextElementSibling; 

            if (replyForm) {
                const currentDisplay = replyForm.style.display;
                
                if (currentDisplay == 'none' || currentDisplay === '') {
                    replyForm.style.display = 'block';
                } else {
                    replyForm.style.display = 'none';
                }
            }
        });
    });

    /* 답글 취소 */
    const replyCancelBtns = document.querySelectorAll('.reply-cancel-btn');

    replyCancelBtns.forEach(cancelBtn => {
        cancelBtn.addEventListener('click', e => {
            const replyForm = e.target.closest('.reply-form');
            if (replyForm) {
                replyForm.style.display = 'none';

                const replyBtn = document.querySelector(".reply-btn");
                if (replyBtn) {
                    replyBtn.textContent = '답글';
                }
            }
        });
    });

    /* 답글 등록시 */
    const replySubmitBtn = document.querySelectorAll(".reply-submit-btn");
    replySubmitBtn.forEach(submit => {
        submit.addEventListener("click", e => {
            alert("답글 등록!");
        })
    })


/* 비동기 댓글 목록 조회 함수 */
function selectCommentList(type, value) {

    const url = "/comment?boardNo=" + boardNo + "&" + type + "=" + value;
    
    fetch(url)
    .then(resp => resp.text())
    .then(commentList => {
        document.getElementsByClassName('comment-area')[0].innerHTML = commentList;
    })
    .catch(e => console.log(e))
}

console.log("comment.js");


/* 댓글 등록 */
const commentArea = document.getElementsByName("write-comment-area")[0];
const commentBtn = document.getElementsByClassName("comment-button-area")[0];

commentBtn.addEventListener("click", e => {

    /* 로그인 안 한 경우 댓글 작성 X */
    // if(loginMemberNo == "") {
    //     alert("로그인 후 이용해주세요.");
    //     return;
    // }

    /* 댓글 미작성인 경우 */
    if(commentArea.value.trim().length == 0) {
        alert("댓글 작성 후 버튼을 클릭해주세요.");

        commentArea.value = "";
        commentArea.focus();
        return;
    }

    /* 댓글 작성 비동기 요청 */
    const data = {
    "commentContent" : commentArea.value,
    memberNo : 2,
    "postNo" : boardNo
    }

    fetch("/comment/write", {
        method: "POST",
        headers: {"Content-Type" : "application/json"},
        body: JSON.stringify(data)
    })
    .then(resp => resp.text())
    .then(result => {
        console.log(result);

        if(result != 0) {
            alert("댓글이 등록 되었습니다.");
            commentArea.value = "";

            /* 가본 최신순 정렬로 조회하기 */
            selectCommentList("sort", "latest");
        } else {
            alert("댓글 등록에 실패했습니다");
        }

    })
    .catch(e => console.log(e))
})

