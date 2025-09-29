console.log("communityDetail.js");

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

/* 이미지 슬라이드 */
const imgList = document.querySelectorAll(".post-img img");
const imgLength = imgList.length; // 이미지 개수

let currentIndex = 0;
const width = 400;
