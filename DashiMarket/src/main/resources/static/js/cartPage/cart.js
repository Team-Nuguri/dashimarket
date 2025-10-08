
/* 추가로 하ㅣㄹ 것
1. 삭제 버튼 클릭 시 비동기로 장바구니 데이터 지우기

2. 체크박스 상태 유지하는 것 



*/

console.log("cart.js load");

/* 장바구니 목록 다 가져옴 */
const cartItem = document.querySelectorAll('.cart-item-row');

cartItem.forEach(item => {

    /* 필요한 요소들 가져옴 */
    const minusBtn = item.querySelector(".minus");
    const plusBtn = item.querySelector(".plus");
    const quantityInput = item.querySelector('.quantity');
    const totalPrice = item.querySelector(".total-price");
    const inputPrice = item.querySelector('[name="inputPrice"]');
    const selectedGoods = item.querySelector('.selectedGoods');
    const goodsNo = item.querySelector('#goodsNo');
    const deleteBtn = item.querySelector('.cartDeletebtn');


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


            const data = {
                boardNo: goodsNo.value,
                quantity: quantityInput.value
            }

            console.log(goodsNo.value);
            console.log(quantityInput.value);

            fetch("/shoppingcart", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)

            })
                .then(resp => {
                    if (resp.ok) {
                        console.log("SUCCESS: 장바구니 업데이트 성공했습니다! (HTTP Status:", resp.status, ")");
                    } else {
                        console.error("FAILURE: 장바구니 업데이트 실패했습니다. (HTTP Status:", resp.status, ")");
                    }
                })
                .catch(e => console.log(e))



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

            const data = {
                boardNo: goodsNo.value,
                quantity: quantityInput.value
            }

            console.log(goodsNo.value);
            console.log(quantityInput.value);

            fetch("/shoppingcart", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)

            })
                .then(resp => {
                    if (resp.ok) {
                        console.log("장바구니 업데이트 성공");
                    } else {
                        console.error("장바구니 업데이트 실패");
                    }
                })
                .catch(e => console.log(e))
        }

    })

    selectedGoods.addEventListener("change", () => {

        updateTotalPrice();
    })


    // 삭제 버튼 클릭 시 장바구니 삭제 

    deleteBtn.addEventListener("click", e => {
        const url = location.pathname + "/delete"

        const data = {
            boardNo : goodsNo.value
        }

        if(confirm("장바구니에서 해당 상품을 삭제하시겠습니까 ?")){

            fetch(url, {
                method : "DELETE",
                headers : {"Content-Type" : "application/json"},
                body : JSON.stringify(data)
            })
            .then(resp => resp.text())
            .then(result =>{
                if(result == 1){
                    alert("장바구니에서 아이템이 삭제되었습니다.");
                    item.remove();
                }else{
                    alert("아이템 삭제 실패");
                }
    
            })
            .catch(e => console.log(e))
        }
    })


})


/* 한국 원화 형식으로 바꿔주는 함수 */
function formatPrice(number) {
    return number.toLocaleString('ko-KR') + ' 원';
}


/* 총 결제 금액 계산 */
function updateTotalPrice() {

    let finalTotal = 0;

    document.querySelectorAll('.cart-item-row').forEach(item => {
        const checkbox = item.querySelector('.selectedGoods');
        const inputPrice = item.querySelector('[name="inputPrice"]');
        const totalPrice = item.querySelector('.total-price');

        // 처음 문서가 로드될 때 수량에 맞는 가격 반영해서 보여주기
        totalPrice.innerText = formatPrice(parseInt(inputPrice.value));

        // 체크박스 체크되었을 때 finalTotal 값 업데이트
        if (checkbox && checkbox.checked) {

            const itemTotal = parseInt(inputPrice.value);

            finalTotal += itemTotal;
        }
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

    const cartItems = document.querySelectorAll('.cart-item-row');
    const selectAllCheckbox = document.getElementById('selectAllCheckbox');
    const individualCheckboxes = document.querySelectorAll('.selectedGoods'); // 개별 상품 체크박스

    selectAllCheckbox.checked = false;
    updateTotalPrice();

    // 2. 전체 선택 체크박스 이벤트 리스너
    selectAllCheckbox.addEventListener('change', () => {
        const isChecked = selectAllCheckbox.checked;

        // 모든 개별 체크박스의 상태를 전체 선택 체크박스의 상태와 동기화
        individualCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });

        updateTotalPrice();
    });

});


/* 구매하기 버튼 클릭 시 결제 금액  0원이면 알림창 ? */
document.getElementById("purchaseBtn").addEventListener("click", e => {


    if (document.getElementById("realTotalPrice").value == 0) {
        alert("구매하실 상품을 선택해주세요.");
        e.preventDefault();
        return;
    }

})




