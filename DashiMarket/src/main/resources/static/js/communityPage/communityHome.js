console.log("communityHome.js");

/* 카테고리 선택 효과 */
const categoryList = document.querySelectorAll("#category-drop-down-container a");
let isClick = document.querySelector("#category-drop-down-container .active-category");

categoryList.forEach(category => {
    category.addEventListener("click", e => {
        e.preventDefault();
        
        /* 카테고리 데이터 가져오기 ex) data-category = "C100" */
        let categoryCode = category.dataset.category;
        
        if(isClick) {
            isClick.classList.remove("active-category");
        }

        category.classList.add("active-category");
        isClick = category;

        /* 비동기 요청 */
        fetchCommunityList("category", categoryCode, e);
        /* 카테고리 변경하면 최신순으로 다시 바꾸기 */
        sortText.innerText = "최신순";
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

        /* 정렬 데이터 가져오기 ex) data-sortType = "latest" */
        let sortType = link.dataset.sortType;

        /* 선택한 정렬 반영하기 */
        sortText.innerText = link.innerText;
        sortBox.classList.remove("show");
        sortDropDownIcon.setAttribute("src", "/images/svg/drop-down.svg");
        
        let currentCategoryCode = isClick.dataset.category; 

        /* 비동기 요청 */
        fetchCommunityList("sort", sortType, e, currentCategoryCode);
    })
})



/* 커뮤니티 카테고리 및 정렬 선택시 조회 */
function fetchCommunityList(type, value, e, currentCategory = null) {
    e.preventDefault(); // 이벤트 막기

    // 현재 url 주소의 쿼리스트링 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set("cp", 1); // 정렬이나 카테고리 바뀌면 1페이지

    /* 쿼리스트링 타입에 따라 url 주소의 쿼리스트링 값 변경 */
    if(type == "category") {
        if(value == "all") urlParams.delete(type); // 기본값

        else urlParams.set(type, value);
    }

    if(type == "sort") {
        if(value == "latest") urlParams.delete(type); // 기본값

        else urlParams.set(type, value);

        /* 카테고리 유지 */
        if(currentCategory && currentCategory !== "all") {
            urlParams.set("category", currentCategory);
        } else {
            urlParams.delete("category");
        }
    }

    /* 요청 주소 만들기 (toString 할 경우 물음표 제외 쿼리스트링 k=v 값만 가져옴 ex: sort=latest) */
    const currentPath = location.pathname;

    /* 요청 주소의 마지막 경로가 /filter라면 제거 (이중으로 /filter/filter로 생겨버렸음..) */
    if(currentPath.endsWith("/filter")) {
        currentPath = currentPath.substring(0, currentPath.lastIndexOf("/filter"));
    }
    const reqUrl = location.pathname + "/filter?" + urlParams.toString();
    
    fetch(reqUrl, {
        method: "GET",
        headers: {
            'X-Requested-With': 'XMLHttpRequest' 
        }
    })
    .then(resp => resp.text())
    .then(communityList => {
        document.getElementById('community-list').innerHTML = communityList;
    })
    .catch(error => console.log(error))
}

/* 글쓰기 버튼 */
const writeBtn = document.getElementsByClassName("write-btn")[0];

writeBtn.addEventListener("click", () => {
    location.href = "/community/write";
})