console.log("admin.js loaded...")

// 사이드메뉴
// a태그 클릭시 해당 페이지로 이동 -> 글자색 52B291로 유지
const move = document.querySelectorAll(".list-group a");

move.forEach(link => {
    link.addEventListener("click", (e) => {
        e.preventDefault(); // 페이지 이동 방지 (나중에 지우기)
        
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

reasonFilter.addEventListener("click", ()=>{

    const open = reasonMenu.classList.toggle("show");

    if(open){
        reasonMenu.classList.remove("close")
    }else{
        reasonMenu.classList.add("close")
    }
})

statusFilter.addEventListener("click", ()=>{

    const open = statusMenu.classList.toggle("show");

    if(open){
        statusMenu.classList.remove("close")
    }else{
        statusMenu.classList.add("close")
    }
})