

const imageList = document.querySelectorAll("#imageSlide img");
const imglength = imageList.length;

let currentIndex = 0;
const width  = 500;

const imageslide = document.getElementById("imageSlide");

const dots = document.getElementsByClassName("dot");


/* 오른쪽 버튼 클릭 시 */
document.getElementById("right-btn")?.addEventListener("click", e => {

    if(currentIndex < imglength-1){

        currentIndex++;
        
        for (let dot of dots) {

            dot.classList.add("opacity");
            
        }
        dots[currentIndex].classList.remove("opacity")

        imageslide.style.transform = `translateX(-${width * currentIndex}px)`
        imageslide.style.transition = "0.5s"
    }
    


})

/* 왼쪽 버튼 클릭 시 */
document.getElementById("left-btn")?.addEventListener("click", () => {

    if(currentIndex > 0){
        currentIndex--;
        for (let dot of dots) {

            dot.classList.add("opacity");
            
        }
        dots[currentIndex].classList.remove("opacity")
        imageslide.style.transform = `translateX(-${width * currentIndex}px)`
        imageslide.style.transition = "0.5s"
    }
})


/* dot 클릭 시 이미지 이동 */
for (let i = 0; i < imglength; i++) {
    
    dots[i].addEventListener("click", () => {
        for (let j = 0; j < imglength; j++) {
            dots[j].classList.add("opacity");
        }

        imageslide.style.transform = `translateX(-${width * i}px)`
        imageslide.style.transition = "0.5s"
        dots[i].classList.remove("opacity");
    })

    
}


/* 수정 삭제 */
document.getElementById("ud-menu").addEventListener("click", () => {

    document.querySelector(".dropdown-menu").classList.toggle("show");


})


/* 하트 클릭 시  */
document.getElementById("like-heart").addEventListener("click", e=>{

    e.target.classList.toggle("fa-solid");
})



/* ------------------------------------ */
/* 비슷한 상품 목록 */
const leftBtn = document.getElementById("left-out-btn");
const rightBtn = document.getElementById("right-out-btn");
const slideImage = document.getElementById("image-slide");

const similarItem = document.getElementsByClassName("similar-items").length / 4;
let index = 0;


rightBtn.addEventListener("click", () => {
    index++;
    if(index < similarItem-1){

        slideImage.style.transform = `translateX(-${1200 * index}px)`;
        slideImage.style.transition = 'all 0.4s'
        
    }


})
leftBtn.addEventListener("click", () => {
    if(index > 0){
        index--;

        slideImage.style.transform = `translateX(-${1200 * index}px)`
        slideImage.style.transition = 'all 0.4s'
    }


})



document.getElementById("deleteBtn")?.addEventListener("click", e => {


    if(confirm("정말 삭제하시겠습니까 ?")){
        
        const data = {joonggoNo : joonggoNo};

        fetch("/joonggo/delete", {
            method : "DELETE",
            headers : {"Content-Type" : "application/json"},
            body : JSON.stringify(data)
    
        })
        .then(resp => resp.text())
        .then(result => {
            if(result > 0){
                alert("삭제되었습니다.");
                location.href ='/joonggo';
            }else{
                alert("삭제 실패하였습니다.");
            }
        })
        .catch(e => console.log(e))
    }
    

})
