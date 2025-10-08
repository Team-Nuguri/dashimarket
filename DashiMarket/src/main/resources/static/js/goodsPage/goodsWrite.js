
const goodsImage = document.getElementById("goodsImg");
const imageList = document.getElementById('goods-img-list');

const preview1 = document.getElementById('preview1');
const preview2 = document.getElementById('preview2');

const oldInfo = document.getElementById("oldInfo");
const oldImage = document.getElementById("oldImage");


document.getElementById("goodsInfo").addEventListener("change", e => {

    const file = e.target.files[0];

    const infoarea = document.getElementById("goods-info-area");


    if(file != undefined){

        // 파일이 선택된 경우
        const reader = new FileReader();

        reader.readAsDataURL(file);
        // 지정된 파일을 읽은 후 result 속성에 url 형식으로 저장

        reader.onload = e => {
            

            preview1.setAttribute('src', e.target.result);

            // x 버튼 클릭 이벤트 리스너
            document.getElementById("imgInfoDelete").addEventListener('click', () => {
                    goodsContent.value='';
                    preview1.setAttribute('src', '/images/common/AddImage.png'); // 미리보기 삭제
            });
        }

    }

})



goodsImage.addEventListener("change", e => {




    const file = e.target.files[0];

    
    if(file != undefined){

        // 파일이 선택된 경우
        const reader = new FileReader();

        reader.readAsDataURL(file);
        // 지정된 파일을 읽은 후 result 속성에 url 형식으로 저장

        reader.onload = e => {

            
            preview2.setAttribute('src', e.target.result);

            // x 버튼 클릭 이벤트 리스너
            document.getElementById("imgDelete").addEventListener('click', () => {
                    goodsImage.value='';
                    preview2.setAttribute('src', '/images/common/AddImage.png');// 미리보기 삭제
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