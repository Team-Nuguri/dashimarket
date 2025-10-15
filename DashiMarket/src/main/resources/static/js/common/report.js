const reportSelect = document.querySelector('.report-select');
const selected = reportSelect.querySelector('.selected');
const options = reportSelect.querySelector('.options');

selected.addEventListener('click', () => {
    options.style.display = options.style.display === 'block' ? 'none' : 'block';
});

options.querySelectorAll('.option').forEach(option => {
    option.addEventListener('click', () => {
        selected.textContent = option.textContent;
        options.style.display = 'none';
        // 여기서 선택값(option.dataset.value) 필요하면 사용 가능
    });
});

// 옵션 외부 클릭시 옵션 닫기 (선택사항)
document.addEventListener('click', (e) => {
    if (!reportSelect.contains(e.target)) {
        options.style.display = 'none';
    }
});



console.log(targetMemberNo);

let  reportCode = 0;

const option = document.querySelectorAll(".option");


option.forEach(op => {

    op.addEventListener("click", e => {
        reportCode = op.getAttribute('data-value');
    })
})

const reportButton = document.getElementById("reportButton");

const reportReason = document.getElementById("reportReason");

reportButton.addEventListener("click", e => {


    if(reportCode == 0){
        alert("신고 유형을 선택해주세요.");
        return ;

    }


    

    const data = {
        reportCode : parseInt(reportCode),
        targetMemberNo : parseInt(targetMemberNo),
        reportReason : reportReason.value
    }


    if(confirm("신고하시겠습니까 ?")){

        fetch(location.pathname, {
            method : "POST",
            headers : {"Content-Type" : "application/json"},
            body : JSON.stringify(data)
        })
        .then(resp => resp.text())
        .then(result => {

            if(result == 1){
                alert("신고가 접수되었습니다.");
                window.close();
            } else if(result == -1){
                alert("이미 신고한 게시글 입니다.");
                window.close();
            
            } else{
                alert("서버 문제로 신고 접수 실패, 다시 시도 plz ~");
            }
            
        })
        .catch(e => console.log(e))
        
        
    }

})