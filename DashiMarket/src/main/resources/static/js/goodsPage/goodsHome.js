console.log("goosHome.js");

const sortText = document.getElementById("sort-text");
const sortBox = document.getElementById("select-sort-box");
const selectSort = document.getElementById("select-sort");
const dropDownIcon = document.querySelector("#drop-down-icon img");

/* 정렬 선택창 토글 */
selectSort.addEventListener("click", () => {
    /* toggle의 리턴값: true = 열림, false = 닫힘 */
    const isOpen = sortBox.classList.toggle("show");

    if (isOpen) { // 드롭다운 펼쳐졌을 때
        dropDownIcon.setAttribute("src", "/images/svg/drop-down-reverse.svg");
    } else {
        dropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
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
        dropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
    })
})

/* 굿즈 아이템 정렬 */
function fetchGoodsList(sortType, e) {
    e.preventDefault(); // 이벤트 막기

    fetch("/goods/sort?sortType=" + sortType)
    .then(resp => resp.text())
    .then(goodsList => {
        document.getElementById('goods-container').outerHTML = goodsList;
    })
    .catch(error => console.log(error))
}