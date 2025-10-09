console.log("communityWrite.js loaded . . .")

let allFiles = []; // 이미지 정보를 담을 배열

const communityImg = document.getElementById("communityImg");

communityImg.addEventListener("change", e => {


    /* 파일 5개 제한 */
    if (e.target.files.length + document.getElementsByClassName('imglist').length > 5) {
        alert('업로드 가능한 이미지는 5장입니다.')
        communityImg.value = '';
        return;
    }

    const imageList = document.getElementById('community-img-list');

    const file = Array.from(e.target.files);

    // concat() 메서드의 중요한 특징은 바로 원본 배열을 건드리지 않고 새로운 배열을 반환
    // 파일이 추가 될 때마다 allFiles에 담아줌
    allFiles = allFiles.concat(file);
    for (let file of e.target.files) {

        // 파일이 선택된 경우
        const reader = new FileReader();

        reader.readAsDataURL(file);
        // 지정된 파일을 읽은 후 result 속성에 url 형식으로 저장

        reader.onload = e => {
            const li = document.createElement("li");
            const img = document.createElement('img');
            const span = document.createElement('span');
            span.innerText = 'x';

            span.classList.add('x-button');
            span.classList.add('text-size-14');

            img.classList.add('imglist')
            img.setAttribute('src', e.target.result);
            li.append(img, span);
            imageList.append(li);

            // x 버튼 클릭 이벤트 리스너
            span.addEventListener('click', () => {
                const fileIndex = allFiles.indexOf(file); // 배열에서 파일의 인덱스를 찾음
                if (fileIndex > -1) {
                    allFiles.splice(fileIndex, 1); // 해당 인덱스의 파일 제거
                    li.remove(); // 미리보기 삭제
                }
            });
        }

    }
})  



/* ------------------------------------------------- */
/* 카테고리 */

const mainBtn = document.getElementsByClassName("main-category");
const mainCategory = document.getElementById("categoryId"); // 인풋 히든

for (let btn of mainBtn) {

    btn.addEventListener("click", e => {

        for (let b of mainBtn) {
            b.classList.remove("text-green");
        }

        const categoryId = e.target.dataset.category
        console.log(categoryId)

        btn.classList.add("text-green");

        mainCategory.value = e.target.dataset.category;

    })
    
}



/* 
        const data = new FormData();

    for (let file of allFiles) {

        data.append('images', file);
        
    }

    FormData
    - JavaScript의 내장 객체 웹폼의 데이터와 동일한 형식으로 key value 쌍을 쉽게 캡슐화하기 위해 설계
    - 파일 데이터를 포함하여 텍스트 데이터와 함께 서버로 전송할 수 있도록 데이터 표준화

    -FormData 객체는 append(키, 값) 메서드를 사용하여 필요한 모든 데이터를 추가
    - 같은 key 값으로 여러개 append 하면 배열처럼 쌓임 
    - 비동기 요청 보낼때 headers 따로 작성 x 자동으로 설정해줘서 
    - key 값하고 서버에서 받을 변수명 일치 시키면 편함


*/


const communityTitle  = document.getElementById("communityTitle");
const communityContent  = document.getElementById("communityContent");

/* form 태그 제출 시 기본 제출 막고 비동기로 allfiles 보낼 예정 */

document.getElementById("writeForm").addEventListener("submit", e => {

    e.preventDefault();

    if(mainCategory.value == "") {
        alert("카테고리를 선택해주세요.");
        return;
    }

    const data = new FormData();

    // 서버에 넘겨줄 데이터 FormData에 담아서
    data.append("communityTitle", communityTitle.value);
    data.append("communityContent", communityContent.value);
    data.append("categoryId", mainCategory.value);

    // allFIles 파일 하나씩 꺼내서 같은 key 값으로 추가
    for (let file of allFiles) {
        data.append("communityImg", file);
    }

    
    // headers 작성 필요 x 알아서 설정해줌
    fetch("/community/write", {
        method : "POST",
        body : data
    })
    .then(resp => resp.text())
    .then(result => {
        console.log(result);

        if(result =='false'){
            alert("게시글 등록에 실패하였습니다.");
            setTimeout(() => {

                location.href = location.pathname;
            }, 100);
            
        }else{
            alert("게시글이 등록되었습니다.")
            
            setTimeout(() => {

                location.href = result; // 게시글 번호로 이동
            }, 100);
        }

    })
    .catch(e => console.log(e))
})

const cancelBtn = document.getElementsByClassName("btn-cancel")[0];
cancelBtn.addEventListener("click", () => {
    if(confirm("작성을 취소 하시겠습니까?")) {
        location.href = location.pathname.replace("/write", "");
    }
})