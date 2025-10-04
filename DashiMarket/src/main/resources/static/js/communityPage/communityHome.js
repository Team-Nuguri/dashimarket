console.log("communityHome.js");

/* 카테고리 선택 효과 */
const categoryList = document.querySelectorAll("#category-drop-down-container a");
let isClick = categoryList[0];

categoryList.forEach(category => {
    category.addEventListener("click", e => {
        e.preventDefault();
        // console.log(e.target.textContent); div값 가져오기
        
        if(isClick) {
            isClick.classList.remove("active-category");
        }

        category.classList.add("active-category");
        isClick = category;
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
        sortDropDownIcon.setAttribute("src", "/images/svg/drop-down-reverse.svg");
    } else {
        sortDropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
    }
});

/* 정렬 선택시 반영시키기 */
const sortLinks = document.querySelectorAll("#select-sort-box a");

sortLinks.forEach(link => {
    link.addEventListener("click", (e) => {
        /* 선택한 정렬 반영하기 */
        sortText.innerText = link.innerText;
        sortBox.classList.remove("show");
        sortDropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
    })
})

/* 커뮤니티 정렬 */
function fetchGoodsList(sortType, e) {
    e.preventDefault(); // 이벤트 막기

    fetch("/community/sort?sortType=" + sortType)
    .then(resp => resp.text())
    .then(communityList => {
        document.getElementById('community-list').outerHTML = communityList;
    })
    .catch(error => console.log(error))
}