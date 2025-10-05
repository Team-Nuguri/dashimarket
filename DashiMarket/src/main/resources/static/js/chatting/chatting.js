console.log("chatting.js")

const reserve = document.getElementById("reserve")
const complete = document.getElementById("complete")
const imgSet = document.querySelectorAll(".img-set")

// floating버튼 클릭시 채팅방 화면 보여주고 채팅 목록 조회하기


// 예약
reserve.addEventListener("click", () => {
    // 예약된 상태인지 확인 (class로 구분)
    const isReserved = reserve.classList.contains("color-icon");

    if (!isReserved) {
        // 아직 예약 안 됐을 때 → 예약 confirm
        if (confirm("예약하시겠습니까?")) {
            reserve.classList.add("color-icon");
            imgSet[0].setAttribute("src", "/images/svg/예약-color.svg");
            alert("예약되었습니다.");
        }
    } else {
        // 이미 예약된 상태일 때 → 취소 confirm
        if (confirm("예약을 취소하시겠습니까?")) {
            reserve.classList.remove("color-icon");
            imgSet[0].setAttribute("src", "/images/svg/예약.svg");
            alert("예약이 취소되었습니다.");
        }
    }
});

// 거래완료
complete.addEventListener("click", ()=>{

    const isComplete = complete.classList.add("color-icon");

    if(confirm("거래를 완료 하시겠습니까?")){
        imgSet[1].setAttribute("src", "/images/svg/거래완료-color.svg")
        alert("거래완료 되었습니다.")

        // 버튼 비활성화
        complete.disabled = true;
    }
})

// 케밥메뉴 클릭시 신고후 나가기, 나가기 보여주기
const exit = document.getElementById("exit")
const dropdown = document.getElementById("dropdown");

exit.addEventListener("click", ()=>{
    
    dropdown.classList.toggle("hidden");
})

// 바깥 클릭 시 닫힘
document.addEventListener("click", (e) => {
    if (!exit.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.classList.add("hidden");
    }
});

// 신고후 나가기, 나가기 confirm으로 처리
const reportExit = document.getElementById("report-exit")
const justExit = document.getElementById("just-exit")

// 신고후 나가기
reportExit.addEventListener("click", () => {
    if (confirm("정말 신고 후 나가시겠습니까?")) {
        // 확인 눌렀을 때 실행
        alert("신고 후 나가기 처리되었습니다.");
        // 여기서 신고 처리 + 나가기 로직 실행
    } else {
        alert("취소되었습니다.");
    }
});

// 그냥 나가기
justExit.addEventListener("click", () => {
    if (confirm("해당 메세지는 사라집니다. 정말 나가시겠습니까?")) {
        // 확인 눌렀을 때 실행
        alert("나가기 처리되었습니다.");
        // 나가기 로직 실행
    } else {
        alert("취소되었습니다.");
    }
});