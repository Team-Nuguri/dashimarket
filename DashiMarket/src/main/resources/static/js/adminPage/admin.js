console.log("admin.js loaded...")

// 사이드메뉴
// a태그 클릭시 해당 페이지로 이동 -> 글자색 52B291로 유지
const move = document.querySelectorAll(".list-group a");

move.forEach(link => {
    link.addEventListener("click", (e) => {
        //e.preventDefault(); // 페이지 이동 방지 (나중에 지우기)

        // 모든 a 태그에서 active 제거
        move.forEach(i => i.classList.remove("selected"));

        // 클릭한 a 태그만 active 추가
        link.classList.add("selected");
    });
});

// 통합 신고에서만 실행
if (location.href == "http://localhost/admin/report") {

    document.addEventListener("DOMContentLoaded", () => {
        
        // 오늘 신고 카운트
        const loadReportStats = () => {
            fetch("/admin/stats")
            .then(resp => resp.json())
            .then(data => {
                const todayReport = document.getElementById("todayReport");

                // 값이 바뀔 때만 부드럽게 애니메이션처럼 교체
                if (todayReport.innerText != data.todayReportCount) {
                    todayReport.style.opacity = 0;

                    setTimeout(() => {
                        todayReport.innerText = data.todayReportCount;
                        todayReport.style.opacity = 1;
                    }, 200);
                }
            })
            .catch(err => console.error("신고된 수 불러오기 오류:", err));
        };

        // 최초 1회 실행
        loadReportStats();

        setInterval(loadReportStats, 10 * 60 * 1000); // 10분 마다 갱신 -> 새로고침 해야함
    })

    /* 필터(사유) 선택시 반영시키기 */
    const reasonBtn = document.getElementById("btn1");
    const reasonLinks = document.querySelectorAll(".dropdown-menu a");
    const reasonMenu = document.getElementsByClassName("dropdown-menu")[0]
    const dropDownImg = document.getElementById("dropdown-img");

    /* 사유 선택창 토글 */
    reasonBtn?.addEventListener("click", () => {
        /* toggle의 리턴값: true = 열림, false = 닫힘 */
        const isOpen = reasonMenu.classList.toggle("show");

        // 드롭다운 펼쳐졌을 때
        if (isOpen) {
            reasonMenu.classList.remove("close")
            dropDownImg.setAttribute("src", "/images/svg/color-drop-down-reverse.svg");

        } else {
            reasonMenu.classList.add("close")
            dropDownImg.setAttribute("src", "/images/svg/color-drop-down.svg");
        }
    });


    /* 사유 선택시 반영시키기 */
    reasonLinks?.forEach(link => {
        link.addEventListener("click", (e) => {
            /* 이거 나중에 풀어야 함! (쿠키 또는 세션에 유지) */
            e.preventDefault();

            /* 선택한 사유명으로 반영하기 */
            reasonBtn.innerText = link.innerText;
            reasonMenu.classList.remove("close");
            reasonMenu.classList.add("close")
            
            // fetch로 백엔드 전송
            fetch("/admin/report/updateResult", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(reportList)
            })
            .then(res => res.text())
            .then(result => {
                
                if (result == "success") {
                    alert("처리 완료되었습니다!");
                    location.reload();
                } else {
                    alert("처리 실패!");
                }
            })
        })
    })


    // 전체선택
    document.getElementById("checkAll").addEventListener("change", function () {
        const checked = this.checked;
        document.querySelectorAll("input[name='reportNo']").forEach(cb => cb.checked = checked);
    });



    // 처리완료 버튼 클릭
    document?.getElementById("completeBtn").addEventListener("click", () => {
        // const checked = [...document.querySelectorAll("input[name='reportNo']:checked")].map(cb => cb.value);
        const checked = document.querySelectorAll("input[name='reportNo']:checked");
        // const resultType = document.getElementById("resultType").value;

        if (checked.length === 0) {
            alert("처리할 신고를 선택하세요.");
            return;
        }

        // 선택된 신고번호와 각 처리 결과를 모아 배열로 만듦
        const reportList = Array.from(checked).map(cb => {
            const reportNo = cb.value;
            const select = document.querySelector(`#result-${reportNo}`); // id로 해당 셀렉트 찾기
            const resultType = select ? select.value : "-";
            return { reportNo, resultType };
        });

        console.log(reportList); // 예: [{reportNo: '101', resultType: '게시글삭제'}, ...]


        // fetch로 백엔드 전송
        fetch("/admin/report/updateResult", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(reportList)
        })
        .then(res => res.text())
        .then(result => {
            if (result === "success") {
                alert("처리 완료되었습니다!");
                location.reload();
            } else {
                alert("처리 실패!");
            }
        })
        .catch(err => console.error(err));
    });
}


// 회원조회 페이지에서만 수행
if (location.href == "http://localhost/admin/main") {

    // 모든 문서 로딩후
    document.addEventListener("DOMContentLoaded", () => {

        // 통계 조회 함수 - 신규 가입자, 가입한 회원수
        const loadStats = () => {
            fetch("/admin/stats")
            .then(resp => resp.json())
            .then(data => {
                const todayJoin = document.getElementById("todayJoin");
                const totalUser = document.getElementById("totalUser");

                // 값이 바뀔 때만 부드럽게 애니메이션처럼 교체
                if (todayJoin.innerText != data.todayJoinCount) {
                    todayJoin.style.opacity = 0;

                    setTimeout(() => {
                        todayJoin.innerText = data.todayJoinCount;
                        todayJoin.style.opacity = 1;
                    }, 200);
                }

                if (totalUser.innerText != data.totalUserCount) {
                    totalUser.style.opacity = 0;

                    setTimeout(() => {
                        totalUser.innerText = data.totalUserCount;
                        totalUser.style.opacity = 1;
                    }, 200);
                }
            })
            .catch(err => console.error("통계 불러오기 오류:", err));
        };

        // 최초 1회 실행
        loadStats();

        setInterval(loadStats, 10 * 60 * 1000); // 10분 마다 갱신 -> 새로고침 해야함

        loadAllList();
    })

    // 회원 정보 조회 테이블
    function loadAllList(){

        fetch("/admin/members")
        .then(resp => resp.json())
        .then(memberList => {
    
            // table 만들기
            const tbody = document.querySelector(".table-content tbody");

            tbody.innerHTML = "";
    
            memberList.forEach(member => {
                const row = document.createElement("tr")

                row.innerHTML = `
                <td>${member.memberNo}</td>
                <td>${member.memberEmail}</td>
                <td>${member.memberName}</td>
                <td>${member.memberNickname}</td>
                <td>${member.memberTel}</td>
                <td>${member.address}</td>
                <td>${member.secessionFl}</td>
                <td>${member.enrollDate}</td>
                <td>${member.boardCount}</td>
                <td>${member.commentCount}</td>`;
    
                tbody.appendChild(row);
            })
        })
        .catch(err => console.error("회원 정보를 가지고 올 수 없습니다.", err))
    }
}

// 상품관리 페이지에서만 수행
if (location.href == "http://localhost/admin/goods") {

    // 상품 정보 조회 테이블
    function loadProducts() {
        const sort = document.getElementById("sortSelect").value;
        const tbody = document.querySelector(".table-content tbody");

        // sort가 없으면 기본 정렬
        const url = sort ? `/admin/product?sort=${sort}` : `/admin/product`;

        fetch(url)
        .then(resp => resp.json())
        .then(products => {
            //console.log(products)
            
            // table 만들기
            tbody.innerHTML = "";

            products.forEach(item => {
                const row = document.createElement("tr")
                
                row.innerHTML = `
                <td><input type="checkbox"></td>
                <td>${item.boardNo}</td>
                <td>${item.boardTitle}</td>
                <td>${item.goodsStock}</td>
                <td>${item.goodsPrice}</td>
                <td>${item.boardCreateDate}</td>
                <td>
                    <select class="table-item">
                        <option value="">${'판매중'}</option>
                        <option value="">${'판매완료'}</option>
                    </select>
                </td>`;

                tbody.appendChild(row);
            })
            
        })
        .catch(error => console.error("상품 조회 오류:", error))
    }

    // 페이지 로드시 기본 목록 표시
    document.addEventListener("DOMContentLoaded", loadProducts);
}


// 굿즈 거래내역 페이지에서만 실행
if(location.href == "http://localhost/admin/order"){

    // 굿즈 주문/거래 내역 조회 테이블
    function loadGoodsOrder() {
        const sort = document.getElementById("sortSelect").value;
        const tbody = document.querySelector(".table-content tbody");

        // sort가 없으면 기본 정렬
        const url = sort ? `/admin/purchase?sort=${sort}` : `/admin/purchase`;

        fetch(url)
        .then(resp => resp.json())
        .then(orderList => {
            console.log(orderList)
            
            // table 만들기
            tbody.innerHTML = "";

            orderList.forEach(order => {
                const row = document.createElement("tr")
                
                row.innerHTML = `
                <td>${order.orderNo}</td>
                <td>${order.deliveryStatus}</td>
                <td>${order.recipientName}</td>
                <td>${order.recipientTel}</td>
                <td>${order.orderedName}</td>
                <td>${order.orderQuantity}</td>
                <td>${order.address}</td>
                <td>${order.payMethod}</td>
                <td>${order.payPrice}</td>
                <td>${order.payDate}</td>`;

                tbody.appendChild(row);
            })
            
        })
        .catch(error => console.error("상품 조회 오류:", error))
    }

    // 페이지 로드시 기본 목록 표시
    document.addEventListener("DOMContentLoaded", loadGoodsOrder);
}


// 모든 관리자 페이지에서의 검색
const searchInput = document.getElementById("search-query");
const searchBtn = document.getElementById("searchBtn");
const tbody = document.querySelector(".table-content tbody");

document.addEventListener("DOMContentLoaded", ()=>{

    if (!searchInput || !searchBtn || !tbody) {
        console.warn("검색창 또는 테이블을 찾을 수 없습니다.");
    }
    
    // 현재 페이지 식별
    const path = window.location.pathname;
    let pageType = "";
    let searchUrl = "";
    
    if (path.includes("main")) {
        pageType = "main";
        searchUrl = "/admin/main/search";
    } else if (path.includes("goods")) {
        pageType = "goods";
        searchUrl = "/admin/goods/search";
    } else if (path.includes("report")) {
        pageType = "report";
        searchUrl = "/admin/report/search";
    } else if (path.includes("order")) {
        pageType = "order";
        searchUrl = "/admin/order/search";
    }
    
    console.log("현재 페이지:", pageType, "요청 URL:", searchUrl);
    
    // 검색 실행 함수
    function executeSearch() {
        const keyword = searchInput.value.trim();
    
        if (keyword.length < 2) {
            alert("두 글자 이상 입력해주세요.");
            return;
        }

        tbody.innerHTML = "";
    
        fetch(`${searchUrl}?keyword=${encodeURIComponent(keyword)}`)
        .then(resp => {
            if (!resp.ok) throw new Error("검색 요청 실패");
            return resp.json();
        })
        .then(data => {
            console.log(data)
            renderTable(data, pageType, tbody);
        })
        .catch(err => console.error(err));
    }
    
    searchBtn.addEventListener("click", executeSearch);

    // // Enter 키로도 검색
    // searchInput.addEventListener("keydown", e => {
    //     if (e.key === "Enter") executeSearch();
    // });
    
    // 검색 결과 테이블 렌더링 함수
    function renderTable(data, pageType, tbody) {
    
        if (data.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5">검색 결과가 없습니다.</td></tr>`;
            return;
        }
    
        data.forEach(item => {
            const tr = document.createElement("tr");

            if (pageType === "main") {
                tr.innerHTML = `
                    <td>${item.memberNo}</td>
                    <td>${item.memberEmail}</td>
                    <td>${item.memberName}</td>
                    <td>${item.memberNickname}</td>
                    <td>${item.memberTel}</td>
                    <td>${item.address}</td>
                    <td>${item.secessionFl}</td>
                    <td>${item.enrollDate}</td>
                    <td>${item.boardCount}</td>
                    <td>${item.commentCount}</td>`;
    
            } else if (pageType === "report") {
                tr.innerHTML = `
                    <td><input type="checkbox"></td>
                    <td>${item.reportNo}</td>
                    <td>${item.reportMember}</td>
                    <td>${item.reportTarget} # ${item.boardTitle}</td>
                    <td>${item.targetMember}</td>
                    <td>${item.reportName}</td>
                    <td>${item.reportReason}</td>
                    <td>${item.reportResult}</td>
                    <td>${item.resultDate ? item.resultDate : ''}</td>`;
    
            } else if (pageType === "goods") {
                tr.innerHTML = `
                    <td><input type="checkbox"></td>
                    <td>${item.boardNo}</td>
                    <td>${item.boardTitle}</td>
                    <td>${item.boardContent}</td>
                    <td>${item.goodsStock}</td>
                    <td>${item.goodsPrice}</td>
                    <td>${item.boardCreateDate}</td>
                    <td>
                        <select class="table-item">
                            <option value="">${'판매중'}</option>
                            <option value="">${'판매완료'}</option>
                        </select>
                    </td>`;
    
            } else if (pageType === "order") {
                tr.innerHTML = `
                    <td>${item.orderNo}</td>
                    <td>${item.deliveryStatus}</td>
                    <td>${item.recipientName}</td>
                    <td>${item.recipientTel}</td>
                    <td>${item.orderedName}</td>
                    <td>${item.orderQuantity}</td>
                    <td>${item.address}</td>
                    <td>${item.payMethod}</td>
                    <td>${item.payPrice}</td>
                    <td>${item.payDate}</td>`;
            }
    
            tbody.append(tr);
        });
    }
})