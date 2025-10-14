/* 
    추가로 해야할 것
    결제 요청 시 
    

*/



const orderItem = document.querySelectorAll('.order-item-row');

orderItem.forEach(item => {

    /* 필요한 요소들 가져옴 */
    const minusBtn = item.querySelector(".minus");
    const plusBtn = item.querySelector(".plus");
    const quantityInput = item.querySelector('.quantity');
    const totalPrice = item.querySelector(".total-price");
    const inputPrice = item.querySelector('[name="inputPrice"]');
    const deleteBtn = item.querySelector(".item-delete>button");


    // 상품 개별 가격 숫자로 파싱
    const unitPriceText = totalPrice.innerText.replace(/[^0-9]/g, '');
    const unitPrice = parseInt(unitPriceText);


    minusBtn.addEventListener("click", e => {

        // 현재 수량 가져옴
        let quantity = parseInt(quantityInput.value);

        if (quantity > 1) {
            quantity--;
            quantityInput.value = quantity;

            // 총 가격 계산
            const realTotal = quantity * unitPrice;

            // 총 가격을 원화 형식으로 바꿔주는 함수 호출해 겨로가 반환 받고 
            totalPrice.innerText = formatPrice(realTotal);
            inputPrice.value = realTotal;

            // 총 결제 금액 업데이트
            updateTotalPrice();

        }
    })

    plusBtn.addEventListener("click", () => {


        let quantity = parseInt(quantityInput.value);
        if (quantity > 9) {
            alert("최대 주문 수량은 10개 입니다.");
            return;
        }

        if (quantity < 10) {
            quantity++;
            quantityInput.value = quantity;

            const realTotal = quantity * unitPrice;

            totalPrice.innerText = formatPrice(realTotal);
            inputPrice.value = realTotal;

            updateTotalPrice();
        }

    })


    /* 삭제 버튼 클릭 시 해당 영역 지우기 */
    /* 새로고침 시 다시 생김 해결할 수 있음 하기 */
    deleteBtn.addEventListener("click", e => {

        if (!confirm("해당 상품을 주문 목록에서 삭제하시겠습니까 ?")) {
            alert('삭제 취소');
            return;
        }

        item.remove();
        updateTotalPrice();
        alert('삭제되었습니다.');

    })



})


/* 한국 원화 형식으로 바꿔주는 함수 */
function formatPrice(number) {
    return number.toLocaleString('ko-KR') + ' 원';
}


/* 총 결제 금액 계산 */
function updateTotalPrice() {

    let finalTotal = 0;

    document.querySelectorAll('.order-item-row').forEach(item => {
        const inputPrice = item.querySelector('[name="inputPrice"]');
        const totalPrice = item.querySelector('.total-price');
        const quantity = item.querySelector('.quantity');

        // 처음 문서가 로드될 때 수량에 맞는 가격 반영해서 보여주기
        totalPrice.innerText = formatPrice(parseInt(inputPrice.value));

        finalTotal += parseInt(inputPrice.value)

    });

    document.getElementById("realTotalPrice").value = finalTotal;
    // 총 결제금액 들어갈 요소 가져와서 원화 형식으로 변경후 innertext
    const realTotalPriceElement = document.getElementById("real-total-price");
    if (realTotalPriceElement) {
        realTotalPriceElement.innerText = formatPrice(finalTotal);
    }



}


/* 문서 로드 되었을 때 수량하고 계싼해서 처음에 화면 보여주기 */
document.addEventListener("DOMContentLoaded", () => {

    const orderItems = document.querySelectorAll('.order-item-row');

    updateTotalPrice();


});





/* 구매 버튼 클릭 시  */
document.getElementById("purchaseBtn").addEventListener("click", e => {

    // 주문자 정보 세션에서 얻어오기 가능

    e.preventDefault();


    // 수령인 정보
    const recipientName = document.getElementById("recipientName").value;
    const recipientTel = document.getElementById("recipientTel").value;
    const postcode = document.getElementById("sample6_postcode").value;
    const addressBase = document.getElementById("sample6_address").value;
    const addressDetail = document.getElementById("sample6_detailAddress").value;


    // 상품 총 가격
    const totalPrice = document.getElementById("realTotalPrice").value;

    const orderList = document.querySelectorAll(".order-item-row");

    const orderItems = [];
    const productNames = []; // 상품명 배열

    orderList.forEach(item => {

        const boardNo = item.querySelector('[name="boardNo"]');
        const quantity = item.querySelector('.quantity');
        const goodsName = item.querySelector('.goodsName h3');

        orderItems.push({
            boardNo: boardNo.value,
            quantity: parseInt(quantity.value)

        })

        if (goodsName) {
        productNames.push(goodsName.textContent.trim());
    }

    })

    const orderName = productNames.length === 1
    ? productNames[0]
    : `${productNames[0]} 외 ${productNames.length - 1}건`

    const data = {
        recipientName: recipientName,
        recipientTel: recipientTel,
        postcode: postcode,
        addressBase: addressBase,
        addressDetail: addressDetail,
        totalPrice: totalPrice,
        orderItems: orderItems
    }
    // 구매 버튼 클릭 시 주문 정보 서버에 넘겨줌
    fetch("/goods/payment", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(resp => resp.json())
        .then(result => {

            console.log(result.channelKey);
            if (result != null) {
                requestPayment(result.channelKey, String(result.orderNo), result.storeId, result.totalPrice, recipientName, recipientTel, orderName);
            }

            console.log(result);
        })
        .catch()


})



// 결제창 호출 함수
async function requestPayment(channelKey, orderNo, storeId, totalPrice, recipientName, recipientTel, orderName) {
    const response = await PortOne.requestPayment({
        storeId: storeId,
        paymentId: orderNo,
        orderName: orderName,
        totalAmount: totalPrice,
        currency: "KRW",
        channelKey: channelKey,
        payMethod: "CARD",
        customer: {
            fullName: recipientName,
            phoneNumber: recipientTel,
        },
    });

    console.log(response);
    if (response.code !== undefined) {
        // 오류 발생

        fetch("/goods/payment/fail?orderNo=" + orderNo)
        .then(resp =>{

            if(resp.ok){
                console.log("삭제 성공이다 ㅋ");
            }
            alert(response.message);
            location.reload();
        }
        )
        .catch(e => console.log(e))
        return ;
    }

    // 결제 성공 시 결제 테이블 insert
    // 검증 주문 금액 == 결제 금액
    const notified = await fetch("/goods/payment/complete", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            paymentId: response.paymentId,
            txId: response.txId,
            orderNo: orderNo,
            totalPrice: totalPrice,
            payMethod : "CARD"

        })
    })

    const serverResult = await notified.json();
    
    if (serverResult.verified) {
        alert(serverResult.message);
        location.href = `/goods/orderComplete?orderNo=${orderNo}`;
    } else {
        console.error("서버 검증 실패:", serverResult.message);
        alert(serverResult.message);
    }



}




