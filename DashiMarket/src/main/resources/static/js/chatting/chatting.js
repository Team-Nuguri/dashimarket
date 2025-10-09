console.log("chatting.js")

// floating버튼 클릭시 채팅방 화면 보여주기
const chattingBtn = document.getElementById("chattingBtn");
const chattingPopup = document.querySelector(".chatting-popup")

chattingBtn.addEventListener("click", ()=>{
    chattingPopup.classList.toggle("show")
})

// X 버튼 클릭시 채팅창 숨기기
const closeBtn = document.getElementById("closeBtn")

closeBtn.addEventListener("click", ()=>{
    chattingPopup.classList.toggle("show")
})

let selectChattingNo; // 선택한 채팅방 번호
let selectTargetNo; // 현재 채팅 대상
let selectTargetName; // 채팅 상대 이름
let selectTargetProfile; // 채팅 상대 프로필

const chattingItemList = document.getElementsByClassName("chatting-item")

function roomListAddEvent(){

    for(let item of chattingItemList){
        item.addEventListener("click", ()=>{

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

            // 채팅방 목록 조회
            selectRoomList();

            // 비동기로 메세지 목록 조회
            selectMessage();
        })
    }
}

// 문서 로딩 완료 후 수행할 기능
document.addEventListener("DOMContentLoaded", ()=>{
    
    // 채팅방 목록 클릭 이벤트 추가
    roomListAddEvent();

    // 보내기 버튼 클릭 이벤트 추가 -> sendMessage함수 실행
    send.addEventListener("click", sendMessage)

    // 채팅 알림을 클릭해서 채팅 페이지로 이동한 경우

})

// 채팅방 상대(닉네임) 검색 시
const targetSearch = document.getElementById("name-search")
const resultArea = document.getElementById("resultArea");

targetSearch.addEventListener("input", e => {
    const targetQuery = e.target.value.trim();

    // 입력값이 없을 경우
    if(targetQuery.length == 0){
        resultArea.innerHTML = "";
        resultArea.classList.add("hidden");
        return;
    }

    // 입력값이 있을 경우
    fetch("/chatting/selectTarget?query=" + targetQuery)
    .then(resp => resp.json())
    .then(list => {

        resultArea.innerHTML = "";
        resultArea.classList.remove("hidden")

        // 일치하는 회원이 없을 때
        if(list.length == 0){
            const li = document.createElement("li");
            li.classList.add("result-row");
            li.innerText = "일치하는 회원이 없습니다.";
            resultArea.append(li);
            return;
        }

        // 일치하는 회원이 있을 때
        for(let member of list){

            const li = document.createElement("li");
            li.classList.add("result-row");
            li.setAttribute("data-id", member.memberNo)

            const img = document.createElement("img");
            img.classList.add("result-row-img");

            if(member.profilePath == null) img.setAttribute("src", "/images/admin/user.png")
            else img.setAttribute("src", member.profilePath);

            let nickname = member.memberNickname;
            let email = member.memberEmail;

            const span = document.createElement("span");
            span.innerHTML = `${nickname} ${email}`.replaceAll(targetQuery, `<mark>${targetQuery}</mark>`);

            li.append(img, span);
            resultArea.append(li);

            // 검색한 회원 클릭 시 채팅방 입장 + 검색 결과 숨기기
            li.addEventListener("click", chattingEnter);
        }   
    })
    .catch(err => console.log(err))

})


// 채팅방 입장 함수
function chattingEnter(e) {
    const targetNo = e.currentTarget.getAttribute("data-id") // 중고 상세페이지 채팅버튼에 data 추가

    fetch("/chatting/enter?targetNo=" + targetNo)
    .then(resp => resp.text())
    .then(chattingNo => {

        // 채팅방 목록 조회 - 새롭게 비동기로 화면 만듬
        selectRoomList();

        setTimeout(()=>{
            const itemList = document.getElementsByClassName("chatting-item")

            for(let item of itemList){

                // 목록 채팅방이 존재O
                if(chattingNo == item.getAttribute("chat-no")){

                    targetSearch.value = "";
                    resultArea.innerHTML = "";
                    resultArea.classList.add("hidden");

                    item.click();
                    return;
                }
            }
        }, 200);

    })
    .catch(err => console.log(err))
}

// 비동기로 채팅방 목록 조회
function selectRoomList(){
    fetch("/chatting/roomList")
    .then(resp => resp.json())
    .then(roomList => {
        //console.log(roomList)

        const chattingList = document.querySelector(".chatting-list")
        chattingList.innerHTML = "";

        // 조회한 채팅방 목록 화면에 추가
        for(let room of roomList){
            const li = document.createElement("li");
            li.classList.add("chatting-item");
            li.setAttribute("chat-no", room.chattingNo);
            li.setAttribute("target-no", room.targetNo);

            if(room.chattingNo == selectChattingNo){
                li.classList.add("select");
            }

            // item-header 부분
            const itemHeader = document.createElement("div")
            itemHeader.classList.add("item-header")

            const listProfile = document.createElement("img")

            if(room.targetProfile == undefined){
                listProfile.setAttribute("src", "images/common/user.png")
            }else{
                listProfile.setAttribute("src", room.targetProfile)
            }

            itemHeader.append(listProfile);
            
            // item-body 부분
            const itemBody = document.createElement("div");
            itemBody.classList.add("item-body");

            const p = document.createElement("p");

            const targetName = document.createElement("span");
            targetName.classList.add("target-name");
            targetName.innerText = room.targetNickname;

            const recentSendTime = document.createElement("span");
            recentSendTime.classList.add("recent-send-time");
            recentSendTime.innerText = room.sendTime;

            p.append(targetName, recentSendTime);

            const div = document.createElement("div");
            
            const recentMessage = document.createElement("p");
            recentMessage.classList.add("recent-message");
            
            if(room.lastMessage != undefined){
                recentMessage.innerText = room.lastMessage;
            }
            
            div.append(recentMessage);
            itemBody.append(p, div);

            // 읽지 않은 메세지 개수 출력 (현재 해당 채팅방 보고있음X, 안읽음 0개 이상)
            if(room.notReadCount > 0 && room.chattingNo != selectChattingNo){
                const notReadCount = document.createElement("p");
                notReadCount.classList.add("not-read-count");
                notReadCount.innerText = room.notReadCount;
                div.append(notReadCount);
            
            }else{
                // 현재 채팅방 보고있음O, 비동기로 해당 채팅방 읽음으로 표시
                setTimeout(()=>{
                    fetch("/chatting/updateReadFlag", {
                        method : "PUT",
                        headers : {"Content-Type" : "application/json"},
                        body : JSON.stringify({
                            memberNo : loginMemberNo,
                            chattingNo : selectChattingNo
                        })
                    })
                    .then(resp => resp.text())
                    .catch(err => console.log(err))
                }, 200)
            }

            li.append(itemHeader, itemBody);
            chattingList.append(li)
        }
        roomListAddEvent();
    })
    .catch(err => console.log(err))
}

let lastShownDate = null; // 마지막으로 표시한 날짜 저장 -> 한번만 날짜줄 표시

// 비동기로 메세지 목록 조회
function selectMessage() {
    fetch("/chatting/selectMessageList?chattingNo=" + selectChattingNo + "&memberNo=" + loginMemberNo)
    .then(resp => resp.json())
    .then(messageList => {
        //console.log(messageList)

        const ul = document.querySelector(".display-chatting");
        ul.innerHTML = "";


        const selectNickname = document.getElementById("selectTargetName");
        selectNickname.innerText = selectTargetName
        
        // 채팅화면에 출력
        for(let msg of messageList){

            // 날짜줄 표시
            // msg.sendTime: "2025.10.07 19:39"
            const msgDateStr = msg.sendTime.split(" ")[0]; // "2025.10.07"
            const msgDate = new Date(msgDateStr.replace(/\./g, "-")); // "2025-10-07" → Date 객체
            const today = new Date();

            // 시분초 제거 (자정 기준 비교)
            today.setHours(0, 0, 0, 0);
            msgDate.setHours(0, 0, 0, 0);

            // 오늘보다 이전 날짜이고, 아직 같은 날짜줄이 표시되지 않았으면 표시
            if (msgDate < today && msgDateStr !== lastShownDate) {
                const li = document.createElement("li");
                li.classList.add("chat-date-line");

                const hr1 = document.createElement("hr");
                hr1.classList.add("line");
                const hr2 = document.createElement("hr");
                hr2.classList.add("line");

                const chatDate = document.createElement("span");
                chatDate.classList.add("chat-date-text");
                chatDate.innerText = msgDateStr; // 예: 2025.10.07

                li.append(hr1, chatDate, hr2);
                ul.append(li);

                lastShownDate = msgDateStr; // 중복 표시 방지
            }

            // 실제 메세지 내용 출력
            const msgLi = document.createElement("li"); 

            // 보낸 시간
            const span = document.createElement("span");
            span.classList.add("chat-date")
            span.innerText = msg.sendTime.split(" ")[1];

            // 메세지 내용
            const p = document.createElement("p")
            p.classList.add("chat")
            p.innerText = msg.messageContent;

            // 내가 작성한 메세지인 경우
            if(loginMemberNo == msg.sendMember){
                msgLi.classList.add("my-chat");
                msgLi.append(span, p);
            
            }else{
                msgLi.classList.add("target-chat");
                
                // 상대 프로필
                const img = document.createElement("img");
                img.setAttribute("src", selectTargetProfile);

                const div = document.createElement("div");

                // 상대 이름
                const b = document.createElement("b");
                b.innerText = selectTargetName;

                const targetDiv = document.createElement("div");
                targetDiv.classList.add("my-chat");

                p.classList.remove("chat")
                p.classList.add("target")

                div.append(b, p, span);
                msgLi.append(img, div)
            }

            ul.append(msgLi)
            ul.scrollTop = ul.scrollHeight
        }
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

//================================================================================
// sockJS 이용한 Websocket 구현

let chattingSock;

// 연결하기 위한 기본 세팅
if(loginMemberNo != ""){
    chattingSock = new SockJS("/chattingSock");
    // WebSocket 연결이 성공했을 때 실행
    chattingSock.onopen = () => {
        console.log("✅ WebSocket 연결 성공!");
    };

    // WebSocket 연결 중 오류가 발생했을 때 실행
    chattingSock.onerror = (error) => {
        console.error("❗️ WebSocket 오류 발생:", error);
    };

    // WebSocket 연결이 닫혔을 때 실행
    chattingSock.onclose = (event) => {
        if (event.wasClean) {
            console.log(`🔌 WebSocket 연결이 정상적으로 닫힘 (코드: ${event.code})`);
        } else {
            // 예: 서버 프로세스가 죽거나 네트워크가 끊긴 경우
            console.warn('🔌 WebSocket 연결이 비정상적으로 끊어짐');
        }
    };
}

// 채팅 입력시
const send = document.getElementById("send");

const sendMessage = () => {
    const inputChatting = document.getElementById("inputChatting");

    if(inputChatting.value.trim().length == 0){
        alert("채팅 메세지를 입력해주세요.");
        inputChatting.value = "";

    }else{
        var obj = {
            sendMember : loginMemberNo,
            targetNo : selectTargetNo,
            chattingNo : selectChattingNo,
            messageContent : inputChatting.value
        }

        console.log(obj)

        // JS 객체 -> JSON 문자열로 변환하여 전송
        chattingSock.send(JSON.stringify(obj));

        // 채팅 알림 보내기

        inputChatting.value = "";
    }
}

// Enter 메세지 보내기 -> shift + enter : 줄바꿈
inputChatting.addEventListener("keyup", e => {
    // console.log(e.shiftKey)
    if(e.key == "Enter"){
        if(!e.shiftKey) sendMessage();
    }
})

// chattingSock이 서버로부터 메세지를 받으면 자동으로 실행될 콜백 함수
chattingSock.onmessage = e => {
    // 전달 받은 객체를 JS 객체로 변환해서 저장
    const msg = JSON.parse(e.data);
    console.log("새 메시지 수신:", msg);
    
    // 현재 채팅방을 보고있는 경우
    if(selectChattingNo == msg.chattingNo){
        
        const ul = document.querySelector(".display-chatting");

        const selectNickname = document.getElementById("selectTargetName");
        selectNickname.innerText = selectTargetName
        

        // 실제 메세지 내용 출력
        const msgLi = document.createElement("li"); 

        // 보낸 시간
        const span = document.createElement("span");
        span.classList.add("chat-date")
        span.innerText = msg.sendTime;

        // 메세지 내용
        const p = document.createElement("p")
        p.classList.add("chat")
        p.innerText = msg.messageContent;

        // 내가 작성한 메세지인 경우
        if(loginMemberNo == msg.sendMember){
            msgLi.classList.add("my-chat");
            msgLi.append(span, p);
        
        }else{ // 상대가 작성한 메세지인 경우
            msgLi.classList.add("target-chat");
            
            // 상대 프로필
            const img = document.createElement("img");
            img.setAttribute("src", selectTargetProfile);

            const div = document.createElement("div");

            // 상대 이름
            const b = document.createElement("b");
            b.innerText = selectTargetName;

            const targetDiv = document.createElement("div");
            targetDiv.classList.add("my-chat");

            p.classList.remove("chat")
            p.classList.add("target")

            div.append(b, p, span);
            msgLi.append(img, div)
            console.log("보내기 성공 화면 출력?!?")
        }

        ul.append(msgLi)
        ul.scrollTop = ul.scrollHeight
        
    }
    selectRoomList();
}