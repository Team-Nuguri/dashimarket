console.log("joonggoWrite.js loaded . . .")

let allFiles = []; // 이미지 정볼르 담을 배열

const joonggoImage = document.getElementById("joonggoImg");

joonggoImage.addEventListener("change", e => {


    /* 파일 10개 제한 */
    if (e.target.files.length + document.getElementsByClassName('imglist').length > 10) {
        alert('업로드 가능한 이미지는 10장입니다.')
        joonggoImage.value = '';
        return;
    }

    const imageList = document.getElementById('joonggo-img-list');

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
const subcate = document.getElementById("sub-category");

const mainCategory = document.getElementsByName("mainCategory")[0];
const subCategory = document.getElementsByName("categoryId")[0];


const subcategory = {
    // 가전제품
    J100 : ["생활가전", "주방가전", "미용가전", "냉장고", "에어컨", "세탁기/건조기", "TV", "사무기기"],
    // 의류
    J200 : ["아우터", "상의", "셔츠", "니트/스웨터", "하의", "원피스/스커트", "홈웨어/언더웨어", "기타의류"],
    // 스포츠/레저
    J300 : ["헬스/요가/필라테스","구기 스포츠", "라켓 스포츠", "골프", "등산/캠핑", "자전거", "겨울 스포츠", "수상 스포츠" ],
    //신발
    J400 : ["운동화/스니커즈", "구두/로퍼", "샌들/슬리퍼", "워커/부츠", "스포츠화", "유아/키즈", "기타 남성화", "기타 여성화"],
    //ㄱ뷰티
    J500 : ["스킨케어", "메이크업", "바디/헤어케어", "향수/아로마", "네일아트/케어", "미용소품/기기", "다이어트/이너뷰티", "남성 화장품"],
    //게임
    J600 : ["닌텐도 스위치", "플레이스테이션5", "XBOX", "콘솔 타이틀", "레트로/구형 게임기", "VR/AR 게임기기", "게임 액세서리/부품", "게임 굿즈/수집품"],
    // 생활
    J700 : ["주방/조리용품", "청소/세탁 용품", "욕실/세면 용품", "반려동물 용품", "휴지/생필품", "DIY/원예/공예", "여행/계절 용품", "기타 생활 용품"],
    // 차량 오토바이
    J800 : ["승용차/SUV", "오토바이/스쿠터", "타이어/휠", "내비게이션/블랙박스", "차량 인테리어", "세차/관리", "오토바이 용품", "기타 차량 부품"],
    //가구/인테리어
    J900 : ["침대/매트리스", "소파/의자", "테이블/식탁", "수납장/선반", "조명/스탠드", "커튼/블라인드", "인테이러 소품", "DIY/셀프 인테리어"],
    // 디지털
    J1000 : ["스마트폰/태블릿", "노트북/데스크탑", "모니터", "카메라/캠코더", "이어폰/헤드폰/스피커", "웨어러블 기기", "PC 주변기기", "기타 디지털 기기"],
    //음반/ 악기
    J1100 : ["CD/DVD/블루레이", "LP/턴테이블", "전자 악기", "현악기", "관악기", "타악기", "악기 액세서리/부품", "음향/녹음장비"],
    // 패션 액세소ㅓ리
    J1200 : ["모자", "가방", "시계", "주얼리", "안경/선글라스", "키링/키케이스", "벨트/장갑/스카프", "양말/레그웨어"],
    // 도서 문구
    J1300 : ["소설/문학", "교양/실용/취미", "만화/웹툰/잡지", "문제집/참고서", "아동/유아 도서", "필기류", "노트/파일", "기타 사무/디자인"],
    // 예술 희기ㅡ
    J1400 : ["미술품", "조각/공예품", "사진/포스터", "화폐/우표", "엔틱/빈티지 소품", "피규어/토이", "스타굿즈", "기타 희소성 물품"],
    //공구 / 산업용품
    J1500 : ["전동 공구", "수공구", "측정/안전 장비", "DIY/목공 도구", "캠핑/야외 작업 공구", "산업용 부품/자재", "자동차/정비 공구", "전기/배선 용품"]
    
}

for (let btn of mainBtn) {

    btn.addEventListener("click", e => {

        for (let b of mainBtn) {
            b.classList.remove("text-green");
        }

        

        subcate.innerText = "";

        const categoryId = e.target.dataset.category
        console.log(categoryId)

        const subCategoryList = subcategory[categoryId];

        btn.classList.add("text-green");

        mainCategory.value = e.target.dataset.category;

        if(subCategoryList){

           /*  for (let sub of subCategoryList) {
    
                const li = document.createElement("li");
                const button = document.createElement("button");
                button.classList.add("sub-category");
                button.setAttribute('type', 'button');
                
                
                button.innerText = sub;
                li.append(button);
    
                subcate.append(li);
                
                
    
                
            } */

            for(let i = 0; i < subCategoryList.length; i++){
                
                const li = document.createElement("li");
                const button = document.createElement("button");
                button.classList.add("sub-category");
                button.setAttribute('type', 'button');
                button.innerText = subCategoryList[i];

                li.append(button);
    
                subcate.append(li);

                const prefix = mainCategory.value.slice(0,1);
                const suffix = mainCategory.value.slice(1);

                let numberSuffix  = parseInt(suffix);

                numberSuffix = numberSuffix + i+1;
                let subNo = prefix + numberSuffix;

                button.dataset.subCategory = subNo;

            }
        }


        

        

        const subBtn = document.getElementsByClassName("sub-category");
        
        if(subBtn != null){
            
            for(let i = 0; i < subBtn.length; i++){
        
                subBtn[i].addEventListener("click", e => {

                    for(let s of subBtn) {
                        s.classList.remove("text-green");
                    }
                    
        
                    subCategory.value = e.target.dataset.subCategory;

                    subBtn[i].classList.add("text-green");
                    
                })
        
                
            }
        
        }
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


const joonggoTitle = document.getElementById("joonggoTitle");
const joonggoContent = document.getElementById("joonggoContent");
const joonggoPrice = document.getElementById("joonggoPrice");
//joonggoImg


/* form 태그 제출 시 기본 제출 막고 비동기로 allfiles 보낼 예정 */

document.getElementById("writeForm").addEventListener("submit", e => {

    e.preventDefault();

    if(allFiles.length == 0){
        alert("이미지를 선택해주세요.");
        return ;
    }

    if(subCategory.value ==''){
        alert("카테고리를 선택해주세요.");
        return;
    }


    if (Number.isNaN(Number(joonggoPrice.value))) {
        alert("가격은 숫자만 입력해야 합니다.");
        joonggoPrice.value='';
        joonggoPrice.focus;
        return ; 
    }

    const price = Number(joonggoPrice.value);

    if (price < 0) {
        alert("가격은 0원 이상으로 입력해 주세요.");
        joonggoPrice.value='';
        joonggoPrice.focus;
        return ; 
    }

    const data = new FormData();

    // 서버에 넘겨줄 데이터 FormData에 담아서
    data.append("joonggoTitle", joonggoTitle.value);
    data.append("joonggoContent", joonggoContent.value);
    data.append("joonggoPrice", price);
    data.append("categoryId", subCategory.value);

    // allFIles 파일 하나씩 꺼내서 같은 key 값으로 추가
    for (let file of allFiles) {
        data.append("imageList", file);
    }

    
    // headers 작성 필요 x 알아서 설정해줌
    fetch("/joonggo/write", {
        method : "POST",
        body : data
    })
    .then(resp => resp.text())
    .then(result => {

        console.log(result);

        if(result == 'fail'){
            alert("상품 등록에 실패하였습니다.");
            setTimeout(() => {

                location.href = location.pathname;
            }, 100);
            
        }else{
            alert("상품이 등록되었습니다.")
            
            setTimeout(() => {

                location.href = result;
            }, 100);
        }

        

    })
    .catch(e => console.log(e))



})