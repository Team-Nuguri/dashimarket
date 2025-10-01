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
const subCategory = document.getElementsByName("subCategory")[0];


const subcategory = {
    J100 : ["생활가전", "주방가전", "미용가전", "냉장고", "에어컨", "세탁기/건조기", "TV", "사무기기"],
    
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

        if(subCategoryList){

            for (let sub of subCategoryList) {
    
                const li = document.createElement("li");
                const button = document.createElement("button");
                button.classList.add("sub-category");
                button.setAttribute('type', 'button');
    
                button.innerText = sub;
                li.append(button);
    
                subcate.append(li);
                
                
    
                
            }
        }


        mainCategory.value = e.target.dataset.category;

        console.log(mainCategory.value.slice(0,1));
        console.log(parseInt(mainCategory.value.slice(1))+1);

        const subBtn = document.getElementsByClassName("sub-category");
        
        if(subBtn != null){
            
            for (let sub of subBtn) {
        
                sub.addEventListener("click", e => {

                    for(let s of subBtn) {
                        s.classList.remove("text-green");
                    }
        
                    subCategory.value = e.target.innerHTML;

                    sub.classList.add("text-green");
                })
        
                
            }
        
        }
    })
    
}

function generateCategoryNumber(mainValue){

    const prefix = mainValue.slice(0,1);
    const suffix = mainValue.slice(1);

    const numberSuffix  = parseInt(suffix);

    
}









/* form 태그 제출 시 기본 제출 막고 비동기로 allfiles 보낼 예정 */