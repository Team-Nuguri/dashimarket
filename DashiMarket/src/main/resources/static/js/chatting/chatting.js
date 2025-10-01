console.log("chatting.js")

const reserve = document.getElementById("reserve")
const complete = document.getElementById("complete")
const exit = document.getElementById("exit")
const imgSet = document.querySelectorAll(".img-set")

reserve.addEventListener("click", ()=>{

    if(reserve.classList.toggle("color-icon")){
        imgSet[0].setAttribute("src", "../../static/images/svg/예약-color.svg")
    
    }else{
        imgSet[0].setAttribute("src", "../../static/images/svg/예약.svg")
    }
})

complete.addEventListener("click", ()=>{

    if(complete.classList.toggle("color-icon")){
        imgSet[1].setAttribute("src", "../../static/images/svg/거래완료-color.svg")
        alert("거래완료 되었습니다.")
    
    }else{
        imgSet[1].setAttribute("src", "../../static/images/svg/거래완료.svg")
    }
})

// 케밥메뉴 클릭시 신고후 나가기, 나가기 보여주기
exit.addEventListener("click", ()=>{
    const div = document.createElement("div")
    const p1 = document.createElement("p")
    const p2 = document.createElement("p")
})