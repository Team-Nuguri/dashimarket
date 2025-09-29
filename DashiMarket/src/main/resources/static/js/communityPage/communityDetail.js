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

const postImg = document.getElementsByClassName("post-img")[0];
const dots = document.getElementsByClassName("dot");

/* 오른쪽 버튼 클릭시 */
document.getElementById("right-btn").addEventListener("click", e => {

    if(currentIndex < imgLength - 1) {
        currentIndex++;

        for(let dot of dots) {
            dot.classList.add("opacity");
        }

        dots[currentIndex].classList.remove("opacity");

        postImg.style.transform = `translateX(-${width * currentIndex}px)`;
        postImg.style.transition = '0.5s';
    }
});

/* 왼쪽 버튼 클릭시 */
document.getElementById("left-btn").addEventListener("click", e => {

    if(currentIndex > 0) {
        currentIndex--;

        for(let dot of dots) {
            dot.classList.add("opacity");
        }

        dots[currentIndex].classList.remove("opacity");

        postImg.style.transform = `translateX(-${width * currentIndex}px)`;
        postImg.style.transition = '0.5s';
    }
});