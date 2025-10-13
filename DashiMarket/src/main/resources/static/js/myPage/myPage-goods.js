console.log('myPage-goods.js');

document.addEventListener('DOMContentLoaded', ()=>{

    fetch('/myPage/goods')
    .then(res => res.text())
    .then(data => {

        data.forEach((order, index) => {
    
        document.querySelector(".orderStatus")[index];
        document.querySelector(".goodsName")[index];
        document.querySelector(".goodsId")[index];
        document.querySelector(".goodsDate")[index]; 
                
        orderStatus.innerHTML = order.status;
        goodsName.innerHTML = order.name; 
        goodsId.innerHTML = order.sellerId;
        goodsDate.innerHTML = order.date;
        }
    

    })
    .catch(err => console.log(err))

})

// const reviewBtn = querySelector(".myPage-orderPostWrite");
// const confirmBtn = querySelector(".myPage-orderConfirm");
// const deliveryBtn = querySelector(".myPage-orderDelivery");

// reviewBtn.addEventListener('click', ()=>{

//     window.open('http://www.naver.com');
  
// })

// if(submit != null && reviewBtn != null) {
//     reviewBtn.remove();

//     const reviewNewBtn = document.createElement('Button');
//     reviewNewBtn.setAttribute('id', 'reviewNewBtn');
//     reviewNewBtn.innerText = '거래 후기 등록 완료';
    
//     document.body.appendChild(reviewNewBtn);
// }

