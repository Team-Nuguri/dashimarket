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

/* 답글 달기 */
const replyForm = document.querySelector(".reply-form");

const replyBtns = document.querySelectorAll(".reply-btn");

replyBtns.forEach(button => {
    button.addEventListener('click', e => {

        const clickedBtn = e.currentTarget;
        /* 버튼이 속한 댓글 요소(.parent-comment) 찾기 */
        const parentComment = clickedBtn.closest('.parent-comment');

        /* 이미 답글 폼이 열려있고, 그것이 현재 댓글 아래에 있는지 확인 */
        if (replyForm.style.display === 'block' && replyForm.previousElementSibling === parentComment) {
            // 이미 열려있다면 -> 닫기 (토글)
            replyForm.style.display = 'none';
            return;
        }

        /* 다른 댓글의 답글 클릭시 해당 댓글 아래로 답글폼 이동 */
        parentComment.after(replyForm); 
        replyForm.style.display = 'block';

        /* 부모 댓글 번호 가져오기 */
        const parentCommentNo = parentComment.dataset.commentNo;

        /* 답글 폼에 부모 댓글 번호 세팅 (답글 insert문에서 필요) */
        replyForm.setAttribute('data-parent-no', parentCommentNo);

        const replyTextarea = replyForm.querySelector(".reply-textarea");
        replyTextarea.value = '';
        replyTextarea.focus();
    });
});


/* 답글 취소 */
const replyCancelBtn = replyForm.querySelector(".reply-cancel-btn");
replyCancelBtn.addEventListener("click", () => {
    // 폼 숨기기
    replyForm.style.display = 'none';
});

/* 답글 등록시 */
const replySubmitBtn = document.querySelectorAll(".reply-submit-btn");
const replyContent = document.getElementsByClassName("reply-textarea")[0];
replySubmitBtn.forEach(submit => {
    submit.addEventListener("click", e => {

        /* 세팅된 부모 댓글 번호 */
        const parentNo = replyForm.getAttribute('data-parent-no'); 

        
        /* 답글 미작성인 경우 */
        if(replyContent.value.trim().length == 0) {
            alert("답글 작성 후 버튼을 클릭해주세요.");

            replyContent.value = "";
            replyContent.focus();
            return;
        }

        /* 답글 작성 비동기 요청 */
        const data = {
        "commentContent" : replyContent.value,
        memberNo : 2,
        "postNo" : boardNo,
        "parentCommentNo": parentNo
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
            alert("답글이 등록 되었습니다.");
            commentArea.value = "";

            /* 댓글 비동기 조회 */
            selectCommentList("sort", "latest");
        } else {
            alert("답글 등록에 실패했습니다");
        }

    })
    .catch(e => console.log(e))

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

