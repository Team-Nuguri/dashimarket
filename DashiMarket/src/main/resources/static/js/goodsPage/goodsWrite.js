
const goodsImage = document.getElementById("goodsImg");

goodsImage.addEventListener("change", e => {



    const imageList = document.getElementById('goods-img-list');

    const file = e.target.files[0];

    // concat() 메서드의 중요한 특징은 바로 원본 배열을 건드리지 않고 새로운 배열을 반환
    // 파일이 추가 될 때마다 allFiles에 담아줌
    if(file != undefined){

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
                    goodsImage.value='';
                    li.remove(); // 미리보기 삭제
            });
        }

    }
})  



document.getElementById("goodsInfo").addEventListener("change", e => {

    const file = e.target.files[0];

    const infoarea = document.getElementById("goods-info-area");


    if(file != undefined){

        // 파일이 선택된 경우
        const reader = new FileReader();

        reader.readAsDataURL(file);
        // 지정된 파일을 읽은 후 result 속성에 url 형식으로 저장

        reader.onload = e => {
            const img = document.createElement('img');
            const span1 = document.createElement('span');
            const span2 = document.createElement('span');
            span2.innerText = 'x';

            span2.classList.add('x-button');
            span2.classList.add('text-size-14');
            span1.style.marginLeft = '40px';

            img.classList.add('imglist')
            img.setAttribute('src', e.target.result);
            span1.append(img, span2);
            infoarea.append(span1);

            // x 버튼 클릭 이벤트 리스너
            span2.addEventListener('click', () => {
                    goodsContent.value='';
                    span1.remove(); // 미리보기 삭제
            });
        }

    }

})



// 폼 태그 제출 시
const goodsContent = document.getElementById("goodsInfo");
const goodsPrice = document.getElementById("goodsPrice");
const goodsImg = document.getElementById("goodsImg");

document.getElementById("writeForm").addEventListener("submit", e=> {

    if (Number.isNaN(Number(goodsPrice.value))) {
        alert("가격은 숫자만 입력해야 합니다.");
        goodsPrice.value='';
        goodsPrice.focus;
        e.preventDefault();
        return ; 
    }

    const price = Number(goodsPrice.value);

    if (price < 0) {
        alert("가격은 0원 이상으로 입력해 주세요.");
        goodsPrice.value='';
        goodsPrice.focus;
        e.preventDefault();
        return ; 
    }

    if(goodsContent.value ==''){
        alert("상품 설명 이미지를 선택해주세요.");
        e.preventDefault();
        return ; 
    }

    if(goodsImg.value == ''){
        alert("상품 이미지를 선택해주세요.");
        e.preventDefault();
        return ; 
    }
})