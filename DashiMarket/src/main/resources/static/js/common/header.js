console.log("header.js loaded..")

// SSE(Server-Sent Events) : 클라이언트로 실시간 데이터를 단방향으로 전달

// SSE 연결하는 함수 (비동기)
const connectSse = () => {

    // 로그인 X -> 함수 종료
    if (notificationLoginCheck == false) return;

    console.log("connectSse() 호출");

    // 서버 연결 요청
    const eventSource = new EventSource("/sse/connect"); // SSE를 담당하는 객체

    // 서버로 부터 메세지를 전달 받은 경우
    eventSource.addEventListener("message", e => {
        console.log(e.data);

        const obj = JSON.parse(e.data);
        console.log("전달받은 메세지 : " + obj);
        // -> 알림인 받은 회원 번호, 채팅알림(채팅방번호), 게시글알림(게시글번호), 알림 번호 얻기

        // 채팅 알림 받음 - 현재 해당 채팅방 입장 -> 알림 삭제
        try {
            if (obj.chattingRoomNo == selectChattingNo) {
                fetch("/notification", {
                    method: "DELETE",
                    headers: { "Content-Type": "application/json" },
                    body: obj.notificationNo
                })
                    .then(resp => {
                        if (!resp.ok) {
                            throw new Error("채팅 알림 삭제 실패")
                        }
                        console.log("채팅 알림 삭제 성공")
                    })
                    .catch(err => console.log(err))
                return;
            }
        } catch (e) { }

        setTimeout(() => {

            // 알림 버튼에 알림 표시(활성화)
            const notificationShow = document.querySelector(".notification-area");
            notificationShow.classList.add("show");

            // 알림 목록이 열려 있는 경우
            const notificationList = document.querySelector(".notification-list");
            if (notificationList.classList.contains("show")) {
                selectNotificationList(); // 알림 목록 비동기 조회
            }
        }, 500)
    })

    // 서버 연결 종료된 경우(타임아웃)
    eventSource.addEventListener("error", () => {
        console.log("SSE 재연결 시도")

        eventSource.close();

        setTimeout(() => connectSse(), 5000); // 5초후 재연결 시도
    })
}

// 알림 메세지 전송 함수
const sendNotification = (type, url, pkNo, content) => {

    // 로그인X -> 함수 종료
    if (notificationLoginCheck === false) return;

    const notification = {
        notificationType: type,
        notificationUrl: url,
        pkNo: pkNo,
        notificationContent: content
    }

    fetch("/sse/send", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(notification)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("알림 전송 실패")
            }
            console.log("알림 전송 성공")
        })
        .catch(err => console.log(err))
}

// 비동기로 알림 목록 조회 함수
const selectNotificationList = () => {

    // 로그인X -> 함수 종료
    if (notificationLoginCheck === false) return;

    fetch("/notification")
        .then(resp => {
            if (resp.ok) return resp.json();
            throw new Error("알림 목록 조회 실패");
        })
        .then(selectList => {
            console.log("selectList : " + selectList)

            if (!Array.isArray(selectList) || selectList.length === 0) {
                console.log("알림 목록이 비어있거나 유효하지 않습니다.");
                return;
            }

            // 이전 알림 목록 삭제
            const noticeList = document.querySelector(".notification-list")
            noticeList.innerHTML = "";

            for (let data of selectList) {
                const noticeItem = document.createElement("li");
                noticeItem.className = 'notification-item';

                // 알림 내용(프로필 + 시간 + 내용)
                const noticeDiv = document.createElement("div");
                noticeDiv.className = 'notification-div';

                // 알림을 읽지 않은 경우 'not-read' 추가
                if (data.readCheck == 'N') noticeDiv.classList.add("not-read")

                // 알림 클릭시 동작
                noticeDiv.addEventListener("click", () => {

                    // 읽지 않은 알림인 경우
                    if (data.readCheck == 'N') {
                        fetch("/notification", {
                            method: "PUT",
                            headers: { "Content-Type": "application/json" },
                            body: data.notificationNo
                        })
                    }

                    // 클릭시 알림에 기록된 경로로 이동
                    location.href = data.notificationUrl;
                })

                // 알림 보낸 회원 프로필 이미지
                const senderProfile = document.createElement("img");
                if (data.sendMemberProfileImg == null) senderProfile.src = notificationDefaultImg;
                else senderProfile.src = data.sendMemberProfileImg;

                // 알림 내용 영역
                const noticeContent = document.createElement("div");
                noticeContent.className = 'notification-content';

                // 알림 내용
                const noticeTitle = document.createElement("span");
                noticeTitle.className = 'notification-title';
                noticeTitle.innerHTML = data.notificationContent;

                // 알림 보낸 시간
                const noticeDate = document.createElement("p");
                noticeDate.className = 'notification-date';
                noticeDate.innerText = data.notificationDate;

                // 삭제 버튼
                const noticeDelete = document.createElement("p");
                noticeDelete.className = 'notice-deleteBtn';
                noticeDelete.innerHTML = '&times;';

                // 삭제 버튼 클릭 -> 비동기로 알림 지우기
                noticeDelete.addEventListener("click", (e) => {

                    e.preventDefault();
                    e.stopPropagation();

                    fetch("/notification", {
                        method: "DELETE",
                        headers: { "Content-Type": "application/json" },
                        body: data.notificationNo
                    })
                        .then(resp => {
                            if (resp.ok) {
                                noticeDelete.parentElement.remove();
                                notReadCheck();
                                return;
                            }
                            throw new Error("네트워크 응답이 좋지 않습니다.");
                        })
                        .catch(err => console.log(err))
                })

                noticeList.append(noticeItem);
                noticeItem.append(noticeDiv);
                noticeDiv.append(senderProfile, noticeContent, noticeDelete);
                noticeContent.append(noticeDate, noticeTitle);

            }
        })
        .catch(err => console.log(err))
}

// 읽지 않은 알림 개수 조회 및 알림 유무 표시 여부 변경
const notReadCheck = () => {

    // 로그인X
    if (!notificationLoginCheck) return;

    fetch("/notification/notReadCheck")
        .then(resp => {
            if (resp.ok) return resp.text();
            throw new Error("알림 개수 조회 실패");
        })
        .then(count => {
            console.log("count : " + count)
            const noticeShowArea = document.querySelector(".notification-area");

            // 읽지 않은 알림 수 존재
            if (count != 0) {
                noticeShowArea.classList.add("show");

            } else { // 모든 알림 읽음
                noticeShowArea.classList.remove("show")
            }
        })
        .catch(err => console.log(err))
}


/* 페이지에 따른 아이콘 분류 */
const iconImg = document.getElementsByClassName("icon-img")[0];
const path = location.pathname; // 현재 경로

if (iconImg) {

    if (path.includes("/joonggo")) {
        iconImg.setAttribute("src", "/images/svg/하트.svg");
        iconImg.setAttribute("id", "wishListBtn");
    }

    if (path.includes("/goods")) {
        iconImg.setAttribute("src", "/images/svg/장바구니.svg");
        iconImg.setAttribute("id", "cartListBtn");
    }

    if (path.includes("/community")) {
        iconImg.setAttribute("src", "/images/svg/사람.svg");
        iconImg.setAttribute("id", "communityLikeBtn");
    }
}

// 나의 중고상품 찜목록
document.getElementById("wishListBtn")?.addEventListener("click", e => {

    location.href = "/myPage/wishlist";
})


// 장바구니
document.getElementById("cartListBtn")?.addEventListener("click", e => {

    location.href = "/goods/shoppingcart";
})

// 커뮤니티 좋아요한 게시글
document.getElementById("communityLikeBtn")?.addEventListener("click", () => {
    location.href = "/community/likeLists";
})

// 햄버거 메뉴
document.getElementById("dropdown")?.addEventListener("click", () => {

    document.querySelector('.dropdown-menu').classList.toggle('show');
});


// 페이지 로딩 완료 후 수행
document.addEventListener("DOMContentLoaded", () => {

    connectSse(); // SSE 연결

    notReadCheck(); // 알림 개수 조회

    // 알림 버튼 클릭시 알림 리스트 보이게 하기
    const notificationBtn = document.getElementById("bell");

    notificationBtn?.addEventListener("click", () => {

        // 알림 목록
        const notificationList = document.querySelector(".notification-list");

        // 화면에 알림 목록이 보일 때
        if (notificationList.classList.contains("show")) {
            notificationList.classList.remove("show")

        } else {
            selectNotificationList();
            notificationList.classList.add("show")
        }
    })

    // 중고, 굿즈, 커뮤니티 해당 게시글 제목으로 검색
    const query = document.getElementById("query");
    const result = document.getElementById("result");

    // 현재 페이지 URL로 게시판 타입 구분
    const pathname = window.location.pathname;
    let boardType = "";

    if (pathname.includes("/joonggo")) boardType = "joonggo";
    else if (pathname.includes("/goods")) boardType = "goods";
    else if (pathname.includes("/community")) boardType = "community";

    query.addEventListener("input", (e) => {
        const keyword = query.value.trim();

        if (keyword.length > 0) {
            fetch(`/${boardType}/search?query=${encodeURIComponent(keyword)}`)
            .then((resp) => resp.json())
            .then((list) => {
                console.log(list)
                if (list.length > 0) {
                    result.classList.remove("close");
                    result.innerHTML = "";

                    list.forEach((map) => {
                        const li = document.createElement("li");
                        li.setAttribute("path", `/${boardType}/${map.boardNo}`);

                        const a = document.createElement("a");
                        const regex = new RegExp(keyword, "gi");
                        const highlightedTitle = map.boardTitle.replace(regex,(match) => `<mark>${match}</mark>`);

                        a.innerHTML = `${highlightedTitle}`;
                        a.setAttribute("href", "#");

                        a.addEventListener("click", (e) => {
                            e.preventDefault();
                            const path = e.currentTarget.parentElement.getAttribute("path");
                            location.href = path;
                        });

                        li.append(a);
                        result.append(li);
                    });
                } else {
                    result.innerHTML = "<li>검색 결과가 없습니다.</li>";
                }
            })
                .catch((err) => console.log(err));
        } else {
            result.innerHTML = "";
            result.classList.add("close");
        }

        e.preventDefault();
    });

    // 창 스크롤 -> 알림 클릭 후 URL 이동시 해당 게시글 부분으로 이동
    // cn 번호가 존재하는 경우 - 화면 스크롤 이동
    const params = new URLSearchParams(location.search)
    const cn = params.get("cn"); // cn값 얻어오기
    //console.log(cn)
    
    if(cn != null){
        const targetId = "c" + cn;
        console.log(targetId)

        const target = document.getElementById(targetId);
        console.log(target)

        const scrollPosition = target.offsetTop

        // 창 스크롤
        window.scrollTo({
            top : scrollPosition -50, // 스크롤할 길이
            behavior : "smooth" // 부트럽게 동작
        })
    }
})



