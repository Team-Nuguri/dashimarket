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
    
    if(dots.length != 0) {

        dots[i].addEventListener("click", () => {
            for (let j = 0; j < imgLength; j++) {
                dots[j].classList.add("opacity");
            }
    
            imageSlide.style.transform = `translateX(-${width * i}px)`
            imageSlide.style.transition = "0.5s"
            dots[i].classList.remove("opacity");
        })
    }
    
}


/* 좋아요 */
const likeBtn = document.getElementById("like-heart");

likeBtn.addEventListener("click", e => {
    /* 로그인 하지 않은 회원인 경우 */
    if(loginMemberNo == "") {
        alert("로그인 후 이용해주세요.");
        return;
    }

    /* 기존에 좋아요 x -> 빈 하트 = 0 */
    /* 기존에 좋아요 o -> 꽉찬 하트 = 1 */
    let check;

    /* 좋아요 x */
    if(e.target.classList.contains("fa-regular")) {
        check = 0;
    } else {
        check = 1;
    }

    const data = {
        memberNo: loginMemberNo,
        communityNo: boardNo,
        check: check
    }

    const likeCount = document.getElementById("like-count");
    /* 좋아요 처리 요청 */
    fetch("/community/like", {
        method: "POST",
        headers: {"Content-Type" : "application/json"},
        body: JSON.stringify(data)
    })
    .then(resp => resp.text())
    .then(count => {
        console.log("카운트값" + count);

        if(count == -1) {
            alert("좋아요 처리에 실패했습니다.");
            return;
        }

        e.target.classList.toggle("fa-regular");
        e.target.classList.toggle("fa-solid");

        /* 좋아요수 출력 */
        likeCount.innerText = count;
    })
    .catch(err => {console.log(err)})

    
})

/* 답글 달기 */
const replyForm = document.querySelector(".reply-form");

function initReplyBtn() {

    /* 화면에 있는 모든 답글 버튼 */
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
}

initReplyBtn();


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
        "commentContent" : replyContent.value.trim(),
        memberNo : loginMemberNo,
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
        initReplyBtn();
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
        memberNo : loginMemberNo,
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


// safety function: 작은따옴표(')를 이스케이프하여 자바스크립트 구문 오류를 방지합니다.
const escapeQuotes = (str) => {
    if (str == null) return '';
    // 작은따옴표를 \'(역슬래시 + 작은따옴표)로 이스케이프 처리
    return str.replace(/'/g, "\\'"); 
}

/* 댓글 수정 */
/* 수정 중인 댓글의 원래 내용과 버튼 영역 HTML 저장할 전역 변수 */
let originalCommentContent = "";
let originalDelUpdateAreaHtml = "";

function showUpdateComment(commentNo, btn) {

    /* 이미 수정 중인 댓글이 있는 경우 */
    if(document.querySelector(".save-update-btn")) {
        alert("다른 댓글이 수정 중입니다. 수정 완료 또는 취소해주세요.");
        return;
    }

    /* 댓글 부모 요소 */
    const parentComment = btn.closest(".parent-comment");

    /* 댓글의 내용과 버튼 영역 찾기 */
    const contentArea = parentComment.querySelector(".comment-content");
    const delUpdateArea = parentComment.querySelector(".del-update-area");

    /* 원래 내용과 HTML을 저장 (취소시 이거로 복구) */
    originalCommentContent = contentArea.innerText.trim();
    originalDelUpdateAreaHtml = delUpdateArea.innerHTML;

    /* 댓글 내용 영역을 textarea로 교체 */
    const textareaHtml = `<textarea class="update-textarea">${originalCommentContent}</textarea>`;
    
    contentArea.innerHTML = textareaHtml;
    
    const safeContent = escapeQuotes(originalCommentContent);

    /* 버튼 영역을 저장/취소로 교체 */
    const buttonHtml = `<button class="save-update-btn medium-text" 
                            onclick="updateComment('${commentNo}', this)">저장</button>
                        <button class="cancel-update-btn medium-text" 
                            onclick="cancelUpdateComment(this)">취소</button>`;

    delUpdateArea.innerHTML = buttonHtml;

    contentArea.querySelector(".update-textarea").focus();

}

/* 댓글 수정 취소시 화면 복구 */
const cancelUpdateComment = (btn) => {
    const parentComment = btn.closest('.parent-comment');
    const contentArea = parentComment.querySelector('.comment-content');
    const delUpdateArea = parentComment.querySelector('.del-update-area');

    // 댓글 내용 영역을 원래 텍스트로 복구
    contentArea.innerText = originalCommentContent; 
    
    // 버튼 영역을 원래 HTML로 복구
    delUpdateArea.innerHTML = originalDelUpdateAreaHtml;

    // 전역 변수 초기화
    originalCommentContent = '';
    originalDelUpdateAreaHtml = '';
    initReplyBtn();
}

/* 댓글 수정 비동기 */
const updateComment = (commentNo, btn) => {
    const parentComment = btn.closest(".parent-comment");

    /* 수정한 댓글 내용 가져오기 */
    const textarea = parentComment.querySelector(".update-textarea");

    if(textarea.value.trim().length == 0) {
        alert("수정할 내용을 입력해주세요.");
        textarea.focus();
        return;
    }

    fetch("/comment/update", {
        method: "PUT",
        headers: {"Content-Type" : "application/json"},
        body: JSON.stringify({
            commentNo: commentNo,
            commentContent: textarea.value.trim(),
            memberNo : 3
        })
    })
    .then(resp => resp.text())
    .then(result => {
        if(result > 0) {
            alert("댓글이 수정되었습니다.");

            /* 댓글 목록 다시 조회 (최신순) */
            selectCommentList("sort", "latest");
        } else {
            alert("댓글 수정 실패");
        }
    })
    .catch(e => console.log(e))
}

/* 댓글 삭제 */
function deleteComment(commentNo) {
    if(confirm("삭제 하시겠습니까?")) {
        fetch("/comment/delete", {
            method: "DELETE",
            headers: {"Content-Type" : "application/json"},
            body: JSON.stringify({commentNo: commentNo})
        })
        .then(resp => resp.text())
        .then(result => {
            if(result > 0) {
                alert("삭제 되었습니다.");
                selectCommentList("sort", "latest");
            } else {
                alert("댓글 삭제 실패");
            }
        })
        .catch(e => console.log(e))
    }

}

/* 게시글 수정 */
const updateBtn = document.getElementsByClassName("post-update-btn")[0];

if(updateBtn != null) {
    updateBtn.addEventListener("click", () => {
        /* /community/게시글번호 -> /community/게시글번호/update?cp=1 */
        location.href = location.pathname + '/update' + location.search;
    
    })

}



/* 게시글 삭제 */
const deleteBtn = document.getElementsByClassName("post-del-btn")[0];

if(deleteBtn != null) {
    deleteBtn.addEventListener("click", () => {
        if(confirm("게시글을 삭제하시겠습니까?")) {
            location.href = location.pathname + "/delete";
        }
    })

}
