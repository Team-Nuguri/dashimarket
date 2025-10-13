console.log('myPage-goods.js');

document.addEventListener('DOMContentLoaded', ()=>{

    fetch('/myPage/goods')
    .then(res => res.text())
    .then(data => {

        if(data != null) {
        document.querySelector(.orderStatus).innerHtml = '결재완료';
        document.querySelector(.goodsName).innerHtml = '판매글 제목 / 굿즈 상품명';
        document.querySelector(.goodsId).innerHtml = '판매자 아이디(닉네임)';
        document.querySelector(.goodsDate).innerHtml = 'date';        
        }       
    })
    .catch(err => console.log(err))
})

const goodsReview = querySelector(.myPage-orderPostWrite);
const goodsConfirm = querySelector(.myPage-orderConfirm);
const goodsDelivery = querySelector(.myPage-orderDelivery);

goodsReview.addEventListener('click', (){

    
})
