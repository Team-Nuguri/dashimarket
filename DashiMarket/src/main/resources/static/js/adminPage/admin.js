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


// 통합신고 - 검색창 드롭다운 -> 토글
const reasonFilter = document.getElementsByClassName("filter-btn")[0];
const statusFilter = document.getElementsByClassName("filter-btn")[1];
const reasonMenu = document.getElementsByClassName("dropdown-menu")[0]
const statusMenu = document.getElementsByClassName("dropdown-menu")[1]

/*
reasonFilter?.addEventListener("click", ()=>{

    const open = reasonMenu.classList.toggle("show");

    if(open){
        reasonMenu.classList.remove("close")
    }else{
        reasonMenu.classList.add("close")
    }
})

statusFilter?.addEventListener("click", ()=>{

    const open = statusMenu.classList.toggle("show");

    if(open){
        statusMenu.classList.remove("close")
    }else{
        statusMenu.classList.add("close")
    }
})
*/

/* 필터(사유) 선택시 반영시키기 */
const reasonBtn = document.getElementById("btn1");
const reasonLinks = document.querySelectorAll(".dropdown-menu a");
const reasonDropdownMenu = document.getElementById("dropdown-menu1");
const dropDownImg = document.getElementById("dropdown-img");

/* 사유 선택창 토글 */
reasonBtn.addEventListener("click", () => {
    /* toggle의 리턴값: true = 열림, false = 닫힘 */
    const isOpen = reasonMenu.classList.toggle("show");
    
    if(isOpen){
        reasonMenu.classList.remove("close")
    }else{
        reasonMenu.classList.add("close")
    }

    if (isOpen) { // 드롭다운 펼쳐졌을 때
        dropDownImg.setAttribute("src", "/images/svg/color-drop-down.svg");
    } else {
        dropDownImg.setAttribute("src", "/images/svg/color-drop-down.svg");
    }
});


/* 사유 선택시 반영시키기 */
reasonLinks.forEach(link => {
    link.addEventListener("click", (e) => {
        /* 이거 나중에 풀어야 함! (쿠키 또는 세션에 유지) */
        e.preventDefault();

        /* 선택한 사유명으로 반영하기 */
        reasonBtn.innerText = link.innerText;
        reasonMenu.classList.remove("show");
        dropDownImg.setAttribute("src", "/images/svg/color-drop-down.svg");
    })
})

/*  처리완료 버튼처리   */

// 전체선택
  document.getElementById("checkAll").addEventListener("change", function() {
    const checked = this.checked;
    document.querySelectorAll("input[name='reportNo']").forEach(cb => cb.checked = checked);
  });

  
  
// 처리완료 버튼 클릭
document.getElementById("completeBtn").addEventListener("click", () => {
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
  