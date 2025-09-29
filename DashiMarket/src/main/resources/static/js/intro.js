console.log("intro.js");

const selectCity = document.getElementById("select-city");
const cityBox = document.getElementById("select-city-box");
const cityName = document.getElementById("city-name");
const dropDownIcon = document.querySelector("#drop-down-icon img");

/* 동네 선택창 토글 */
selectCity.addEventListener("click", () => {
    /* toggle의 리턴값: true = 열림, false = 닫힘 */
    const isOpen = cityBox.classList.toggle("show");

    if (isOpen) { // 드롭다운 펼쳐졌을 때
        dropDownIcon.setAttribute("src", "../../resources/static/images/svg/drop-down-reverse.svg");
    } else {
        dropDownIcon.setAttribute("src", "../../resources/static/images/svg/drop-down.svg");
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
        dropDownIcon.setAttribute("src", "../../resources/static/images/svg/drop-down.svg");
    })
})