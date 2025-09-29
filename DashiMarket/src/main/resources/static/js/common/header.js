console.log("header.js loaded..")

// 알림 버튼 클릭시 알림 리스트 보이게 하기
const notificationBtn = document.getElementById("bell");

notificationBtn.addEventListener("click", ()=>{

    // 알림 목록
    const notificationList = document.querySelector(".notification-list");

    // 화면에 알림 목록이 보일 때
    if(notificationList.classList.contains("show")){
        notificationList.classList.toggle("show")

    }else{
        notificationList.classList.toggle("show")
    }
})