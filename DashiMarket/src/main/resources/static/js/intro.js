console.log("intro.js");

/* 동네 검색 카카오맵 API 연동 */
const selectDong = document.getElementById("select-city");

selectDong.addEventListener("click", e => {
    const url = "/openMap"; // 팝업 띄울 url(요청주소): 스프링 부트니까 컨트롤러로 요청해서 컨트롤러가 찾아줘야 함!
    const name = "동네 찾기"; // 팝업창 이름
    
    // 팝업 옵션 - location: 팝업창의 URL 입력란(주소창) Visible 여부 (없음으로 함)
    const option = "width = 450, height = 450, top = 100, left = 200, location = no" 

    // 팝업창 오픈
    window.open(url, name, option);
})
