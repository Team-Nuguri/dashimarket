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
const imageList = document.querySelectorAll("#image-slide img");
const imgLength = imageList.length;

let currentIndex = 0;
const width  = 400;

const imageSlide = document.getElementById("image-slide");
const dots = document.getElementsByClassName("dot");

/* 오른쪽 버튼 클릭 시 */
document.getElementById("right-btn").addEventListener("click", e => {

    if(currentIndex < imgLength-1){

        currentIndex++;
        
        for (let dot of dots) {

            dot.classList.add("opacity");
            
        }
        dots[currentIndex].classList.remove("opacity")

        imageSlide.style.transform = `translateX(-${width * currentIndex}px)`
        imageSlide.style.transition = "0.5s"
    }
});

/* 왼쪽 버튼 클릭 시 */
document.getElementById("left-btn").addEventListener("click", () => {

    if(currentIndex > 0){
        currentIndex--;
        for (let dot of dots) {

            dot.classList.add("opacity");
            
        }
        dots[currentIndex].classList.remove("opacity")
        imageSlide.style.transform = `translateX(-${width * currentIndex}px)`
        imageSlide.style.transition = "0.5s"
    }
})


/* dot 클릭 시 이미지 이동 */
for (let i = 0; i < imgLength; i++) {
    
    dots[i].addEventListener("click", () => {
        for (let j = 0; j < imgLength; j++) {
            dots[j].classList.add("opacity");
        }

        imageSlide.style.transform = `translateX(-${width * i}px)`
        imageSlide.style.transition = "0.5s"
        dots[i].classList.remove("opacity");
    })
    
}