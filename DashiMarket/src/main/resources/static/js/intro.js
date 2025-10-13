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


/* 로그인 안 한 경우(홈페이지 접속시) 현재 위치의 행정동 가져와서 세팅 */
document.addEventListener("DOMContentLoaded", () => {

    if(isSelectDong) return;

    const getRegion = document.getElementById("getRegion");

    /* 이 요소가 화면에 존재하는 경우 = 로그인 x, 선택한 동네 x => 비회원의 현재 위치의 동네명 가져오기 */

    /* 현재 위치의 위도 경도 가져오기 */
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(getSuccess, getError, {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 0
        });

    } else {
        console.warn("Geolocation API를 지원하지 않아 현재 위치를 가져올 수 없습니다.")
    }


});


function getSuccess(position) {

    let latitude = position.coords.latitude; // 위도
    let longitude = position.coords.longitude; // 경도

    console.log("위도 : " + latitude + " / 경도 : " + longitude);

    var geocoder = new kakao.maps.services.Geocoder();

    /* 카카오맵 API를 이용해 좌표를 행정동 코드로 변환 */
    geocoder.coord2RegionCode(longitude, latitude, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {

            var legalDongAddress = '';

            for (var i = 0; i < result.length; i++) {

                // 행정동의 region_type 값은 'H' 이므로
                if (result[i].region_type === 'H') {
                    // 행정동 가져오기
                    legalDongAddress = result[i].address_name;
                    break;
                }
            }

            console.log(legalDongAddress);

            // 변수에 저장된 행정동 주소 확인
            console.log("저장된 행정동 주소:" + legalDongAddress);

            /* 풀 주소를 띄어쓰기로 나눠서 자른 후 */
            /* 주소가 3칸, 4칸인 경우 길이-1만큼 잘라서 마지막 행정동만 가져옴 */
            let selectDongLength = legalDongAddress.split(" ");
            let selectDong = selectDongLength[selectDongLength.length - 1];

            console.log(selectDong);


            /* 가져온 행정동을 서버 세션에 올리기 위해 비동기 요청 */
            if (selectDong != null && selectDong != "") {
                fetch("/selectDong", {
                    method: "POST",
                    headers: { 'Content-Type': 'application/text' },
                    body: selectDong
                })
                .then(resp => resp.text())
                .then(region => {
                    /* 세션에 저장 성공시 동네명 띄워줌 */
                    const dongName = document.querySelector("#city-name > span");

                    if(dongName) dongName.textContent = region;

                })
                .catch(err => console.log(err))

            } else {
                alert("동네를 다시 선택해주세요.");
                return;
            }

        }
    })
}

function getError(error) {
    console.warn("현재 위치 가져오기 권한이 거부되었거나 실패했습니다: " + error.message);
}

// 통합 검색 - 모든 게시글 제목으로 검색
document.addEventListener("DOMContentLoaded", ()=>{
    const query = document.getElementById("query"); // 검색창
    const searchResult = document.getElementById("searchResult");

    query.addEventListener("input", e =>{
        
        // 입력된 내용이 있을 때
        if(query.value.trim().length > 0){
            fetch("/introSearch?query=" + query.value.trim())
            .then(resp => resp.json())
            .then(list => {
                
                // 검색 결과가 있을 때
                if(list.length > 0){
                    searchResult.classList.remove("close");
                    searchResult.innerHTML = "";

                    for(let map of list){
                        const li = document.createElement("li");
                        li.setAttribute("path", `${map.BOARD_TYPE}/${map.BOARD_NO}`);

                        const a = document.createElement("a");

                        map.BOARD_TITLE = map.BOARD_TITLE.replace(query.value, `<mark>${query.value}</mark>`);
                        map.BOARD_TITLE = `<b>${map.BOARD_TITLE}</b>`;

                        a.innerHTML = `${map.BOARD_TITLE} - ${map.BOARD_TYPE}`;
                        a.setAttribute("href", "#");

                        a.addEventListener("click", e => {
                            e.preventDefault()

                            const path = e.currentTarget.parentElement.getAttribute("path");
                            
                            location.href = path;
                        })

                        li.append(a)
                        searchResult.append(li)
                    }
                }
            })
            .catch(err => console.log(err))

        }

        e.preventDefault();
    })
})
