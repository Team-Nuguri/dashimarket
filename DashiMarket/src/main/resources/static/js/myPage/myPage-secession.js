console.log('myPage-secession');

/* myPage-secession.js */

// HTML의 <form> 요소
const secessionForm = document.querySelector(".secession"); 

// 탈퇴하기 버튼 
const secessionButton = document.querySelector(".myPage-secessionSubmit");

// 상세 사유 입력 textarea
const reasonTextarea = document.querySelector(".myPage-secessionInput textarea");

// 글자 수 카운트
const charCountSpan = document.getElementById("charCount");
const MAX_LENGTH = 1000; // 최대 글자 수를 1000자로 가정

// 상세 사유 글자 수 카운트 기능
if(reasonTextarea) {
    reasonTextarea.addEventListener('input', e => {
        let content = e.target.value;
        if (content.length > MAX_LENGTH) {
            content = content.substring(0, MAX_LENGTH);
            e.target.value = content;
        }
        charCountSpan.innerText = content.length;
    });
}


if (secessionForm) {
    secessionForm.addEventListener("submit", e => {
        e.preventDefault(); // 기본 제출 동작 막기
        
        const memberPwInput = document.getElementById("memberPw");
        const memberPw = memberPwInput.value.trim();

        // 1. 비밀번호 입력 확인
        if (memberPw.length === 0) {
            alert("탈퇴를 진행하려면 비밀번호를 입력해주세요.");
            memberPwInput.focus();
            return;
        }
        
        // 2. 최종 탈퇴 의사 확인
        if (!confirm("정말로 회원 탈퇴를 하시겠습니까?\n탈퇴 시 개인 정보 및 서비스 이용 기록이 삭제될 수 있습니다.")) {
            return; // '취소'를 누른 경우
        }

        // 3. 탈퇴 사유 수집
        const checkboxes = document.querySelectorAll('.myPage-secessionCheckBox input[type="checkbox"]:checked');
        const checkedReasons = Array.from(checkboxes).map(cb => cb.parentNode.textContent.trim());
        const detailedReason = reasonTextarea.value.trim();

        // 4. 서버로 전송할 데이터 준비
        const data = {
            memberPw: memberPw,
            reasons: checkedReasons,
            detailedReason: detailedReason
        };
        
        // 5. Ajax 요청 (비밀번호 확인 및 이메일 전송)
        // Spring Security BCrypt를 사용하므로, 별도 엔드포인트에서 비밀번호만 확인해야 합니다.
        fetch("/myPage/secessionCheckAndSendEmail", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        .then(resp => resp.json())
        .then(result => {
            if (result.success) {
                // 비밀번호 일치 및 이메일 발송 성공 시 
                // form을 다시 제출하여 Controller의 POST /myPage/secession을 실행
                alert("비밀번호 확인 및 탈퇴 사유 전송이 완료되었습니다.\n최종 탈퇴를 진행합니다.");
                secessionForm.submit(); // form의 기본 제출 동작 재실행 (Controller로 이동)

            } else {
                // 비밀번호 불일치 또는 이메일 발송 실패 시
                alert(result.message || "비밀번호가 일치하지 않거나, 탈퇴 처리 중 오류가 발생했습니다.");
                memberPwInput.value = ""; // 비밀번호 초기화
                memberPwInput.focus();
            }
        })
        .catch(err => {
            console.error("회원 탈퇴 Ajax 요청 실패:", err);
            alert("서버 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        });

    });
}