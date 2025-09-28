console.log("communityHome.js");

const selectCity = document.getElementById("select-city");
const cityBox = document.getElementById("select-city-box");
const cityName = document.getElementById("city-name");
const dropDownIcon = document.querySelector("#drop-down-icon img");

/* 동네 선택창 토글 */
selectCity.addEventListener("click", () => {
    /* toggle의 리턴값: true = 열림, false = 닫힘 */
    const isOpen = cityBox.classList.toggle("show");

    if (isOpen) { // 드롭다운 펼쳐졌을 때
        dropDownIcon.setAttribute("src", "/images/svg/drop-down-reverse.svg");
    } else {
        dropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
    }
});

/* 동네 선택시 반영시키기 */
const cityLinks = document.querySelectorAll("#select-city-box a");

cityLinks.forEach(link => {
    link.addEventListener("click", (e) => {
        /* 이거 나중에 풀어야 함! (쿠키 또는 세션에 유지) */
        e.preventDefault();

        /* 선택한 동네 이름으로 반영하기 */
        cityName.innerText = link.innerText;
        cityBox.classList.remove("show");
        dropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
    })
})

/* 카테고리 링크 요소 모두 가져오기 */
const categoryLinks = document.querySelectorAll("#category-drop-down-container a");
let isActive = null; // 현재 활성화된 카테고리 링크를 저장할 변수

categoryLinks.forEach(link => {
    link.addEventListener("click", (e) => {
        e.preventDefault();

        /* 선택시 선택 효과 */
        if (isActive) {
            isActive.classList.remove("active-category"); 
        }

        link.classList.add("active-category");
        isActive = link;

    })
})

/* 정렬 */
const sort = document.querySelectorAll(".sort-area a");
let isSort = sort[0];


sort.forEach(link => {
    link.addEventListener("click", (e) => {
    e.preventDefault();

    if(isSort) {
        isSort.classList.remove("bold-text");
    }

    link.classList.add("bold-text");
    isSort = link;
    })
})
