console.log("chatBot.js");

/* 채팅 시작하기 */
const chattingStartBtn = document.getElementById("chatting-start-btn");
const chattingStartArea = document.getElementsByClassName("chatting-start-area")[0];

/* 채팅 진행 */
const chattingArea = document.getElementsByClassName("chatting-area")[0];

chattingStartBtn.addEventListener("click", () => {
    chattingStartArea.style.display = "none";

    chattingArea.style.display = "block";
})