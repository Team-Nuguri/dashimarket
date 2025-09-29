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
        dropDownIcon.setAttribute("src", "../../static/images/svg/drop-down-reverse.svg");
    } else {
        dropDownIcon.setAttribute("src", "../../static/images/svg/drop-down.svg");
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
        dropDownIcon.setAttribute("src", "../../static/images/svg/drop-down.svg");
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
const sortText = document.getElementById("sort-text");
const sortBox = document.getElementById("select-sort-box");
const selectSort = document.getElementById("select-sort");
const sortDropDownIcon = document.querySelector("#sort-drop-down-icon img");

/* 정렬 선택창 토글 */
selectSort.addEventListener("click", () => {
    /* toggle의 리턴값: true = 열림, false = 닫힘 */
    const isOpen = sortBox.classList.toggle("show");

    if (isOpen) { // 드롭다운 펼쳐졌을 때
        sortDropDownIcon.setAttribute("src", "../../static/images/svg/drop-down-reverse.svg");
    } else {
        sortDropDownIcon.setAttribute("src", "../../static/images/svg/drop-down.svg");
    }
});

/* 정렬 선택시 반영시키기 */
const sortLinks = document.querySelectorAll("#select-sort-box a");

sortLinks.forEach(link => {
    link.addEventListener("click", (e) => {
        /* 이거 나중에 풀어야 함! (쿠키 또는 세션에 유지) */
        e.preventDefault();

        /* 선택한 정렬 반영하기 */
        sortText.innerText = link.innerText;
        sortBox.classList.remove("show");
        sortDropDownIcon.setAttribute("src", "../../static/images/svg/drop-down.svg");
    })
})
