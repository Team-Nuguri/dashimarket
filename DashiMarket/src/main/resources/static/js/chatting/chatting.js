console.log("chatting.js")

// floating버튼 클릭시 채팅방 화면 보여주기
const chattingBtn = document.getElementById("chattingBtn");
const chattingPopup = document.querySelector(".chatting-popup")

chattingBtn.addEventListener("click", ()=>{
    chattingPopup.classList.toggle("show")
})

let selectChattingNo;
let selectTargetNo;
let selectTargetName;
let selectTargetProfile;

function roomListAddEvent(){
    const chattingItemList = document.getElementsByClassName("chatting-item")

    for(let item of chattingItemList){
        item.addEventListener("click", ()=>{

            console.log(item)
            
            // 전역변수에 채팅방 번호, 상대 번호, 상대 프로필, 상대 이름 저장
            selectChattingNo = item.getAttribute("chat-no");
            selectTargetNo = item.getAttribute("target-no");
            selectTargetName = item.children[1].children[0].children[0].innerText;
            selectTargetProfile = item.children[0].children[0].getAttribute("src");

            // 알림이 존재하는 경우 지우기

            // 모든 채팅방에서 select 클래스 제거
            for(let it of chattingItemList) it.classList.remove("select")

            // 현재 클릭한 채팅방에 select 클래스 추가
            item.classList.add("select")
        })
    }
}

// 문서 로딩 완료 후 수행할 기능
document.addEventListener("DOMContentLoaded", ()=>{
    
    roomListAddEvent();

    // 채팅 알림을 클릭해서 채팅 페이지로 이동한 경우
})

// 채팅방 닉네임 검색 시



// 채팅방 입장 함수
function chattingEnter(e) {
    console.log(e.currentTarget)

    const targetNo = e.currentTarget.getAttribute("data-id") // 중고 상세페이지 채팅버튼에 data 추가

    fetch("/chatting/enter?targetNo=" + targetNo)
    .then(resp => resp.text())
    .then(chattingNo => {
        console.log(chattingNo)

    })
    .catch(err => console.log(err))
}

// 비동기로 채팅방 목록 조회
function selectRoomList(){
    fetch("/chatting/roomList")
    .then(resp => resp.json())
    .then(roomList => {
        
    })
    .catch(err => console.log(err))
}


// 예약, 거래완료 버튼 클릭시 이벤트
const reserve = document.getElementById("reserve")
const complete = document.getElementById("complete")
const imgSet = document.querySelectorAll(".img-set")

// 예약
reserve?.addEventListener("click", () => {
    // 예약된 상태인지 확인 (class로 구분)
    const isReserved = reserve.classList.contains("color-text");

    if (!isReserved) {
        // 아직 예약 안 됐을 때 → 예약 confirm
        if (confirm("예약하시겠습니까?")) {
            reserve.classList.add("color-text");
            imgSet[0].setAttribute("src", "/images/svg/예약-color.svg");
            alert("예약되었습니다.");
        }
    } else {
        // 이미 예약된 상태일 때 → 취소 confirm
        if (confirm("예약을 취소하시겠습니까?")) {
            reserve.classList.remove("color-text");
            imgSet[0].setAttribute("src", "/images/svg/예약.svg");
            alert("예약이 취소되었습니다.");
        }
    }
});

// 거래완료
complete?.addEventListener("click", ()=>{

    const isComplete = complete.classList.add("color-text");

        if(isComplete, confirm("거래를 완료 하시겠습니까?")){
            imgSet[1].setAttribute("src", "/images/svg/거래완료-color.svg")
            alert("거래완료 되었습니다.")
    
            // 버튼 비활성화
            complete.disabled = true;
        }
})


// 케밥메뉴 클릭시 신고후 나가기, 나가기 보여주기
const exit = document.getElementById("exit")
const dropdown = document.getElementById("dropdown");

exit?.addEventListener("click", ()=>{
    
    dropdown.classList.toggle("hidden");
})

// 바깥 클릭 시 닫힘
document?.addEventListener("click", (e) => {
    if (!exit.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.classList.add("hidden");
    }
});

// 신고후 나가기, 나가기 confirm으로 처리
const reportExit = document.getElementById("report-exit")
const justExit = document.getElementById("just-exit")

// 신고후 나가기
reportExit?.addEventListener("click", () => {
    if (confirm("정말 신고 후 나가시겠습니까?")) {
        // 확인 눌렀을 때 실행
        alert("신고 후 나가기 처리되었습니다.");
        // 여기서 신고 처리 + 나가기 로직 실행
    } else {
        alert("취소되었습니다.");
    }
});

// 그냥 나가기
justExit?.addEventListener("click", () => {
    if (confirm("해당 메세지는 사라집니다. 정말 나가시겠습니까?")) {
        // 확인 눌렀을 때 실행
        alert("나가기 처리되었습니다.");
        // 나가기 로직 실행
    } else {
        alert("취소되었습니다.");
    }
});