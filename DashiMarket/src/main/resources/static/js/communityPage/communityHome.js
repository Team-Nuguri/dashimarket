console.log("communityHome.js");

/* 카테고리 선택 효과 */
const categoryList = document.querySelectorAll("#category-drop-down-container a");
let isClick = categoryList[0];

categoryList.forEach(category => {
    category.addEventListener("click", e => {
        e.preventDefault();
        
        /* 카테고리 데이터 가져오기 ex) data-category = "C100" */
        let categoryCode = category.dataset['category'];
        
        if(isClick) {
            isClick.classList.remove("active-category");
        }

        category.classList.add("active-category");
        isClick = category;

        /* 비동기 요청 */
        fetchCommunityList("category", categoryCode, e);
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

        /* 카테고리 데이터 가져오기 ex) data-category = "C100" */
        let sortType = category.dataset['category'];

        /* 선택한 정렬 반영하기 */
        sortText.innerText = link.innerText;
        sortBox.classList.remove("show");
        sortDropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");

        /* 비동기 요청 */
        fetchCommunityList("sort", sortType, e);
    })
})



/* 커뮤니티 카테고리 및 정렬 선택시 조회 */
function fetchCommunityList(type, value, e) {
    e.preventDefault(); // 이벤트 막기

    // 현재 url 주소의 쿼리스트링 가져오기
    const urlParams = new URLSearchParams(window.location.search);

    /* 쿼리스트링 타입에 따라 url 주소의 쿼리스트링 값 변경 */

    /* 카테고리 */
    if(type == "category") {
        if(value == "all") urlParams.delete(type); // 기본값
        else urlParams.set(type, value);
    }

    /* 정렬 */
    if(type == "sort") {
        if(value == "latest") urlParams.delete(type);
        else urlParams.set(type, value);
    }

    /* 요청 주소 만들기 (toString 할 경우 물음표 제외 쿼리스트링 k=v 값만 가져옴 ex: sort=latest) */
    const reqUrl = window.location.pathname + "?" + urlParams.toString;
    
    fetch(reqUrl, {
        method: "GET",
        headers: { 'X-Requested-With': 'XMLHttpRequest' } // 컨트롤러에서 /community로 한 번에 처리하는데, 비동기 요청 여부를 확인하기 위함 (k:v 형태)
    })
    .then(resp => resp.text())
    .then(communityList => {
        console.log(communityList)
        document.getElementById('community-list').outerHTML = communityList;
    })
    .catch(error => console.log(error))
}