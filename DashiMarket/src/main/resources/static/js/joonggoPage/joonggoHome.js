// /* 카테고리 링크 요소 모두 가져오기 */
// /* 작동 일부 중지시킴 : 링크가 제대로 작동안해서 막아놓았습니다.  (KJK) */
// // const categoryLinks = document.querySelectorAll("#category-drop-down-container a");
// let isActive = null; // 현재 활성화된 카테고리 링크를 저장할 변수

// categoryLinks.forEach(link => {
//     link.addEventListener("click", (e) => {
//          e.preventDefault();

//         // 선택시 선택 효과
//         if (isActive) {
//             isActive.classList.remove("active-category"); 
//         }

//         link.classList.add("active-category");
//         isActive = link;

//     })
// })


// /* 정렬 */
// const sort = document.querySelectorAll(".sort-area a");
// let isSort = sort[0];


// sort.forEach(link => {
//     link.addEventListener("click", (e) => {
//     e.preventDefault();

//     if(isSort) {
//         isSort.classList.remove("bold-text");
//     }

//     link.classList.add("bold-text");
//     isSort = link;
//     })
// })



// document.getElementById("div1").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer1 = document.getElementById("category-sub-container" + i);
//             subContainer1.style.display = (i === 1) ? "block" : "none";
//         }
//   });

  
// document.getElementById("div2").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer2 = document.getElementById("category-sub-container" + i);
//             subContainer2.style.display = (i === 2) ? "block" : "none";
//         }
//   });


   
// document.getElementById("div3").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer3 = document.getElementById("category-sub-container" + i);
//             subContainer3.style.display = (i === 3) ? "block" : "none";
//         }
//   });

  
// document.getElementById("div4").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer4 = document.getElementById("category-sub-container" + i);
//             subContainer4.style.display = (i === 4) ? "block" : "none";
//         }
//   });

   
// document.getElementById("div5").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer5 = document.getElementById("category-sub-container" + i);
//             subContainer5.style.display = (i === 5) ? "block" : "none";
//         }
//   });

  
// document.getElementById("div6").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer6 = document.getElementById("category-sub-container" + i);
//             subContainer6.style.display = (i === 6) ? "block" : "none";
//         }
//   });

  
  
// document.getElementById("div7").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer7 = document.getElementById("category-sub-container" + i);
//             subContainer7.style.display = (i === 7) ? "block" : "none";
//         }
//   });

  
// document.getElementById("div8").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer8 = document.getElementById("category-sub-container" + i);
//             subContainer8.style.display = (i === 8) ? "block" : "none";
//         }
//   });


  
// document.getElementById("div9").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer9 = document.getElementById("category-sub-container" + i);
//             subContainer9.style.display = (i === 9) ? "block" : "none";
//         }
//   });

  
  
// document.getElementById("div10").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer10 = document.getElementById("category-sub-container" + i);
//             subContainer10.style.display = (i === 10) ? "block" : "none";
//         }
//   });


  
// document.getElementById("div11").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer11 = document.getElementById("category-sub-container" + i);
//             subContainer11.style.display = (i === 11) ? "block" : "none";
//         }
//   });


  
// document.getElementById("div12").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer12 = document.getElementById("category-sub-container" + i);
//             subContainer12.style.display = (i === 12) ? "block" : "none";
//         }
//   });


  
// document.getElementById("div13").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer13 = document.getElementById("category-sub-container" + i);
//             subContainer13.style.display = (i === 13) ? "block" : "none";
//         }
//   });


  
// document.getElementById("div14").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer14 = document.getElementById("category-sub-container" + i);
//             subContainer14.style.display = (i === 14) ? "block" : "none";
//         }
//   });


  
// document.getElementById("div15").addEventListener("click", function() {

//         document.getElementById("category-container").style.display = "none";

//         for (let i = 1; i <= 15; i++) {
//             const subContainer15 = document.getElementById("category-sub-container" + i);
//             subContainer15.style.display = (i === 15) ? "block" : "none";
//         }
//   });



document.addEventListener("DOMContentLoaded", () => {

    // 정렬 텍스트가 현재 상태를 반영하도록 업데이트
    const sortTextDiv = document.getElementById('sort-text');
    if (sortTextDiv) { // sort-text 요소가 있는지 먼저 확인
        const path = window.location.pathname; 

        if (path.includes('/lowPrice')) {
            sortTextDiv.textContent = '낮은 가격순';
        } else if (path.includes('/highPrice')) {
            sortTextDiv.textContent = '높은 가격순';
        } else if (path.includes('/latest')) {
            sortTextDiv.textContent = '최신순';
        } else {
            // 그 외 모든 경우 (기본 페이지 등)는 '최신순'으로 표시
            sortTextDiv.textContent = '최신순';
        }
    }


    // 현재 선택된 정렬 메뉴 글씨 두껍게 만들기
    const sortLinks = document.querySelectorAll("#select-sort-box a");
    if (sortLinks.length > 0) { // 정렬 링크가 있는지 확인
        const currentPath = window.location.pathname;

        sortLinks.forEach(link => {
            const linkPath = link.getAttribute('href').split('?')[0];

            // 현재 URL 경로가 링크의 경로와 일치하면 글씨 두껍게
            if (currentPath === linkPath) {
                const innerDiv = link.querySelector('div');
                if (innerDiv) {
                    innerDiv.classList.add('bold-text');
                }
            }
        });
    }

});

/* 맵 팝업 띄우기 */
const selectDong = document.getElementById("show-city-name");
selectDong.addEventListener("click", () => {
    const url = "/openMap"; // 팝업 띄울 url(요청주소): 스프링 부트니까 컨트롤러로 요청해서 컨트롤러가 찾아줘야 함!
    const name = "동네 찾기"; // 팝업창 이름
    
    // 팝업 옵션 - location: 팝업창의 URL 입력란(주소창) Visible 여부 (없음으로 함)
    const option = "width = 450, height = 450, top = 100, left = 200, location = no" 

    // 팝업창 오픈
    window.open(url, name, option);
})