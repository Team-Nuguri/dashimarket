console.log('signUp.js');

// 1. checkObj 키는 유지 (memberEmail, memberPw 등)
const checkObj = {
    'memberName' : false,
    'memberEmail' : false,
    'memberPw' : false, 
    'memberPwConfirm' : false, 
    'memberNickname' : false, 
    'memberTel' : false, 
    'authKey' : false,
    'address' : false,
    'detailAddress' : false
};

// 이름 유효성 검사 (checkObj에 포함되지 않으므로, 유효성만 검사)
const memberName = document.getElementById('signUp-name-input');
const nameMessage = document.getElementById('nameMessage');

memberName.addEventListener('input', ()=> {
    const reExp = /^[가-힣]{2,5}$/
    
    if(memberName.value.trim() === ""){
        nameMessage.textContent = '필수 입력 항목입니다.';
        nameMessage.classList.remove('is-success');
        nameMessage.classList.add('is-error');
        checkObj.memberName = false;
        return;

    }else{
        if (reExp.test(memberName.value)){
            nameMessage.textContent = '유효한 이름입니다.';
            nameMessage.classList.add('is-success');
            nameMessage.classList.remove('is-error');
            checkObj.memberName = true; 
        }else{
            nameMessage.textContent = '유효한 이름(한글 2~5자)을 입력해주세요';
            nameMessage.classList.remove('is-success');
            nameMessage.classList.add('is-error');
            checkObj.memberName = false;
        } 
    }
});

// 전역 변수 'checkObj'가 있다고 가정합니다.
// const checkObj = { memberEmail: false, authKey: false, /* 기타 필드 */ };

// -----------------------------
// 아이디/이메일 유효성 검사 (checkObj 키 연결 및 중복 로직 수정)
const memberEmail = document.getElementById('signUp-email-input');
const emailMessage = document.getElementById('emailMessage');

memberEmail.addEventListener("input", ()=>{
    const reExp = /^[\w-]{2,30}@[a-zA-Z0-9]+(\.[a-zA-Z]{2,}){1,2}$/;
    
    // 이메일 입력창이 비었을 때
    if(memberEmail.value.trim() === ""){
        emailMessage.textContent = '필수 입력 항목입니다.';
        emailMessage.classList.add('is-error');
        emailMessage.classList.remove('is-success');
        checkObj.memberEmail = false; 
        return;
    }

    // 이메일 형식이 유효하지 않을 때
    if(!reExp.test(memberEmail.value)){
        emailMessage.innerText = "이메일 형식이 유효하지 않습니다.";
        emailMessage.classList.add("is-error");
        emailMessage.classList.remove("is-success");
        checkObj.memberEmail = false; 
        return; // 형식이 틀리면 바로 종료
    }
    
    // 형식이 유효할 때 (서버 중복 검사 실행)
    // 비동기 통신이 끝날 때까지 checkObj.memberEmail을 바로 설정하지 않습니다.
    fetch("/dupCheck/email?email=" + memberEmail.value)
    .then(resp => resp.text())
    .then(count =>{
        
        // 3. 중복 로직 수정: count가 0일 때 사용 가능
        if(count == 0){
            emailMessage.textContent = '사용 가능한 이메일입니다.';
            emailMessage.classList.remove('is-error');
            emailMessage.classList.add('is-success');
            checkObj.memberEmail = true; // ✅ 비동기 성공 후 true 설정
        }else{
            emailMessage.textContent = '이미 사용 중인 이메일입니다.'
            emailMessage.classList.add('is-error');
            emailMessage.classList.remove('is-success');
            checkObj.memberEmail = false; // ❌ 중복이면 false 설정
        }
    })
    .catch(err => console.log(err)); // 통신 오류 발생 시에도 checkObj.memberEmail은 마지막 상태를 유지
});


// -----------------------------
// 인증번호 
const sendAuthKeyBtn = document.getElementById('sendAuthKeyBtn');
const authMessage = document.getElementById('authMessage');
const signUpInput = document.getElementById('signUp-email-input');

let authTimer;
let authMin = 4; // 5분 타이머의 분 초기값 (4분 남음)
let authSec = 59; // 5분 타이머의 초 초기값 (59초 남음)
let tempEmail; // 인증번호 발송 성공 시 이메일 임시 저장
// checkObj는 다른 곳에서 정의되었다고 가정하고 사용합니다.

sendAuthKeyBtn.addEventListener("click", function(e) {
    e.preventDefault(); 

    // 타이머 변수 초기화는 checkObj.memberEmail 검사 후에 실행
    //    이전에 실패한 경우의 authMin/authSec을 덮어쓰기 위함입니다.
    // authMin = 4;
    // authSec = 59; 
    
    // 1. checkObj.memberEmail이 true일 때만 실행 (유효성, 중복 검사 완료)
    if(checkObj.memberEmail) { 

        // 2. 서버에 인증번호 발송 요청
        fetch('/sendEmail/signUp?email=' + memberEmail.value)
        .then(resp => resp.text())
        .then(result => {
            if(result > 0){
                // 이메일 발송 성공 (서버가 1 이상 반환)
                tempEmail = memberEmail.value;

                // 타이머 시작 전, 분/초 변수 초기값 재설정 (4분 59초로 설정)
                authMin = 4; 
                authSec = 59;
                
                // 메시지를 "05:00" 대신 "04:59"로 먼저 출력하고 1초 뒤부터 카운트다운 시작
                authMessage.innerText = "04:59"; 
                authMessage.classList.remove('is-success', 'is-error'); // 상태 초기화
                
                // 기존 타이머 확실히 중단 후 새로 시작
                clearInterval(authTimer); 
                authTimer = window.setInterval(()=>{

                    // 5. 타이머 출력
                    const displaySec = authSec < 10 ? "0" + authSec : authSec;
                    const displayMin = authMin < 10 ? "0" + authMin : authMin; // 분도 00 형식으로 출력
                    authMessage.innerText = `${displayMin}:${displaySec}`;
                    
                    if(authMin == 0 && authSec == 0){
                        checkObj.authKey = false;
                        authMessage.innerText = '인증 시간이 만료되었습니다.'; 
                        authMessage.classList.add('is-error');
                        clearInterval(authTimer); // 타이머 종료
                        return;
                    }

                    if(authSec == 0){
                        authSec = 59; // 60 대신 59로 변경 (다음 줄에서 -- 되므로)
                        authMin--;
                    } else {
                         authSec--;
                    }
            

                }, 1000)

            }else {
                // 이메일 발송 실패 
                alert("중복되지 않은 이메일을 작성해주세요 (서버 발송 실패)");
                signUpInput.focus();
                // 실패 시 tempEmail 초기화 (인증하기 버튼이 잘못된 이메일로 동작하는 것을 방지)
                tempEmail = ""; 
            }
        })
        .catch(err => {
            console.error(err);
            alert("이메일 발송 중 통신 오류가 발생했습니다.");
            tempEmail = ""; // 오류 시에도 초기화
        });
    } else {
        alert("유효한 이메일 형식과 중복 검사를 먼저 완료해주세요.");
    }
});

// -----------------------------
// 인증번호 확인 


const confirmAuthKeyBtn = document.getElementById('confirmAuthKeyBtn'); 
const authKeyInput = document.getElementById('signUp-veri-input'); 

confirmAuthKeyBtn.addEventListener("click", (e)=>{ 
    e.preventDefault(); 
    
    if(authKeyInput.value.trim() === ""){
        alert("인증번호를 입력해주세요");
        checkObj.authKey = false;  // 
        authKeyInput.focus();
        return;
    }

    if(authMin === 0 && authSec === 0){
        alert("인증 시간이 만료되었습니다. 다시 시도해주세요");
        checkObj.authKey = false;
        return;
    }

    // 인증번호 확인 요청
    const obj = {"inputKey": authKeyInput.value, "email": tempEmail} 
    const query = new URLSearchParams(obj).toString(); 

    fetch("/sendEmail/checkAuthKey?" + query)
    .then(resp => resp.text())
    .then(result => {
        
        if(result > 0){
            // 인증 성공
            clearInterval(authTimer); 
            authMessage.textContent = '인증되었습니다.';
            authMessage.classList.add('is-success');
            authMessage.classList.remove('is-error'); 
            checkObj.authKey = true;

        }else {
            // 인증 실패
            alert('인증번호가 일치하지 않습니다.'); 
            authMessage.textContent = '인증번호가 일치하지 않습니다.'; 
            authMessage.classList.remove('is-success');
            authMessage.classList.add('is-error');
            checkObj.authKey = false;
        }

    }) 
    .catch(err => {
        console.error("인증번호 확인 중 오류:", err);
        alert("인증번호 확인 중 통신 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
    });
});

// -----------------------------

// 비밀번호 유효성 검사 (checkObj 키 연결)
const pwInput = document.getElementById('signUp-passWord-input');
const pwConfirmInput = document.getElementById('signUp-passwordConfirm-input');
const pwMessage = document.getElementById('pwMessage');

pwInput.addEventListener("input", () => {
    
    const reExp = /^[\w@#%\-]{6,20}$/

    if(pwInput.value.trim() === ""){
        pwMessage.textContent = '필수입력 항목입니다.';
        pwMessage.classList.add('is-error');
        pwMessage.classList.remove('is-success');
        checkObj.memberPw = false; // checkObj 키 사용
        return;
    } 
        if(reExp.test(pwInput.value)){
            pwMessage.textContent = '유효한 비밀번호입니다.';
            pwMessage.classList.remove('is-error');
            pwMessage.classList.add('is-success');
            checkObj.memberPw = true; // checkObj 키 사용
        }else {
            pwMessage.textContent = '유효한 비밀번호를 입력해주세요 (6~20자)';
            pwMessage.classList.add('is-error');
            pwMessage.classList.remove('is-success');
            checkObj.memberPw = false; // checkObj 키 사용
        }
    
    // 비밀번호 재확인도 바로 검사
    if(pwConfirmInput.value.trim().length > 0) {
        pwConfirmInput.dispatchEvent(new Event('input'));
    }
});

// 비밀번호 재확인 유효성 검사 (checkObj 키 연결)
pwConfirmInput.addEventListener("input", ()=>{
    
    if(pwConfirmInput.value.trim()===""){
        pwMessage.textContent = '필수 입력 항목입니다.';
        pwMessage.classList.add('is-error');
        pwMessage.classList.remove('is-success');
        checkObj.memberPwConfirm = false; // checkObj 키 사용
        return;
    } else {
        
        if(checkObj.memberPw){ // 비밀번호 형식이 맞을 때만 비교
            if(pwInput.value === pwConfirmInput.value){
                pwMessage.textContent = '비밀번호 일치합니다.'; 
                pwMessage.classList.remove('is-error');
                pwMessage.classList.add('is-success');
                checkObj.memberPwConfirm = true; // checkObj 키 사용
    
            }else {
                pwMessage.textContent = '비밀번호가 일치하지 않습니다.';
                pwMessage.classList.add('is-error');
                pwMessage.classList.remove('is-success');
                checkObj.memberPwConfirm = false; // checkObj 키 사용
            } 
        } else {
            pwMessage.textContent = '비밀번호 형식을 먼저 맞춰주세요.';
            pwMessage.classList.add('is-error');
            pwMessage.classList.remove('is-success');
            checkObj.memberPwConfirm = false; // checkObj 키 사용
        }
    }
});

//닉네임 유효성 검사 (URL 오류 수정 및 checkObj 키 연결)
const nickInput = document.getElementById('signUp-nickInput');
const nickMessage = document.getElementById('nickMessage');

nickInput.addEventListener("input", ()=>{
    const reExp = /^[ㄱ-힣A-Za-z0-9\s]{2,10}$/
    
    if(nickInput.value.trim() === ""){
        nickMessage.textContent = '필수 입력 항목입니다.';
        nickMessage.classList.add('is-error');
        nickMessage.classList.remove('is-success');
        checkObj.memberNickname = false; // checkObj 키 사용
        return; 
    } else {
        if(reExp.test(nickInput.value)){

            // 6. 닉네임 URL 오류 수정: = 추가
            fetch("/dupCheck/nickname?memberNickname=" + nickInput.value)
            .then(resp => resp.text())
            .then(count => {

                // 중복 로직 수정: count가 0일 때 사용 가능
                if(count == 1){
                    nickMessage.textContent = '이미 사용 중인 닉네임입니다.'; 
                    nickMessage.classList.add('is-error');
                    nickMessage.classList.remove('is-success');
                    checkObj.memberNickname = false; // checkObj 키 사용

                } else {
                    
                    nickMessage.textContent = '사용 가능한 닉네임입니다.'; 
                    nickMessage.classList.remove('is-error');
                    nickMessage.classList.add('is-success');
                    checkObj.memberNickname = true; // checkObj 키 사용

                }
            })
            .catch(err => console.log(err));

        }else{
            nickMessage.textContent = '유효한 닉네임을 입력해주세요 (2~10자)';
            nickMessage.classList.add('is-error');
            nickMessage.classList.remove('is-success');
            checkObj.memberNickname = false; // checkObj 키 사용
        }
    }
});

// 전화번호 유효성 검사 (checkObj 키 연결 및 하이픈 로직 개선)
const telInput = document.getElementById('signUp-telInput');
const telMessage = document.getElementById('telMessage');

telInput.addEventListener("input", ()=>{
    const reExp = /^010\d{8}$/ // 하이픈 없는 11자리
    const value = telInput.value.replace(/-/g, ''); // 하이픈 제거 후 검사

    if(value.trim() === ""){
        telMessage.textContent = '필수 입력 항목입니다 '; 
        telMessage.classList.add('is-error');
        telMessage.classList.remove('is-success');
        checkObj.memberTel = false; // checkObj 키 사용
        return; 
    } 
        if(reExp.test(value)) {
            telMessage.textContent = '유효한 전화번호입니다. '; 
            telMessage.classList.remove('is-error');
            telMessage.classList.add('is-success');
            checkObj.memberTel = true; // checkObj 키 사용

            // 하이픈 포맷팅: 입력된 값 (하이픈 제거된 값)을 기준으로 포맷팅
            if (value.length === 11) {
                telInput.value = value.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
            } else {
                telInput.value = value;
            }

        }else {
            telMessage.textContent = '유효한 전화번호(010XXXXXXXX)를 입력해주세요';
            telMessage.classList.add('is-error');
            telMessage.classList.remove('is-success');
            checkObj.memberTel = false; // checkObj 키 사용
        }
    
});


// 주소 Daum API

function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
                document.getElementById("sample4_jibunAddress").value = data.jibunAddress;
                
                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }               
            }
        }).open();
}


// -----------------------------
// **2. 상세 주소 입력 필드에 대한 리스너를 전역 영역에 한 번만 등록합니다.**

const detailAddressInput = document.getElementById("sample4_detailAddress");

detailAddressInput.addEventListener('input', () => {
    // 상세 주소 입력 필드가 비어있지 않다면 true (trim() 사용하여 공백만 있는 경우도 빈 것으로 처리)
    if (detailAddressInput.value.trim() !== '') {
        checkObj.detailAddress = true;
    } else {
        // 비어있다면 false
        checkObj.detailAddress = false;
    }
});


// -----------------------------
// 폼 제출 이벤트 핸들러 (submit)

document.getElementById("signUpFrm").addEventListener('submit', e=>{
    
    e.preventDefault(); // 맨 처음에 무조건 막기!
    
    for(let key in checkObj){

        if(!checkObj[key]){

            switch(key){
                case 'memberName':
                    alert('이름을 확인해주세요.');
                    memberName.focus();
                    return; 
                case 'memberEmail':
                    alert('이메일 형식 확인 및 중복 검사를 완료해주세요.');
                    memberEmail.focus();
                    return;
                case 'memberPw':
                    alert('비밀번호가 유효하지 않습니다.');
                    pwInput.focus();
                    return;
                case 'memberPwConfirm':
                    alert('비밀번호 확인이 일치하지 않습니다.');
                    pwConfirmInput.focus();
                    return;
                case 'memberNickname':
                    alert('닉네임 형식 확인 및 중복 검사를 완료해주세요.');
                    nickInput.focus();
                    return;
                case 'memberTel':
                    alert('전화번호가 유효하지 않습니다.');
                    telInput.focus();
                    return;
                case 'authKey': 
                    alert('이메일 인증을 완료해주세요.');
                    authKeyInput.focus();
                    return;

                case 'postCode': 
                    alert('주소 검색을 완료해주세요.'); // postCode 검사
                    document.getElementById('sample6_postcode').focus();
                    return;
                    
                case 'detailAddress': 
                    alert('상세 주소를 입력해주세요.'); // 상세 주소 검사
                    document.getElementById('sample6_detailAddress').focus();
                    return;
            }
        }
    }
    
    // 모든 검증을 통과했을 때만 제출
    e.target.submit();
});
