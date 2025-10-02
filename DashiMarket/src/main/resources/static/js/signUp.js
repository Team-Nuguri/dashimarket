console.log('signUp.js');

// 이름 유효성 검사

const nameInput = document.getElementById('signUp-name-input');
const nameMessage = document.getElementById('nameMessage');


nameInput.addEventListener('input', ()=> {

    const reExp = /^[가-힣]{2,5}$/
    
    if(nameInput.value.trim() === ""){        

        nameMessage.textContent = '필수 입력 항목입니다.';
        
        nameMessage.classList.remove('is-success');
        nameMessage.classList.add('is-error');
    }else{

        if (reExp.test(nameInput.value)){

        nameMessage.textContent = '입력 가능한 아이디입니다.';

        nameMessage.classList.add('is-success');
        nameMessage.classList.remove('is-error');
        
        }else{

        nameMessage.textContent = '유효한 아이디를 입력해주세요';

        nameMessage.classList.remove('is-success');
        nameMessage.classList.add('is-error');
        }       
    }
})

// 아이디/이메일 유효성 검사

const emailInput = document.getElementById('signUp-email-input');
const emailMessage = document.getElementById('emailMessage');

emailInput.addEventListener("input", ()=>{

    const reExp = /^[\w-]{2,10}@[a-zA-Z0-9]+(\.[a-zA-Z]{2,}){1,2}$/;
    
    if(emailInput.value.trim() === ""){

        emailMessage.textContent = '필수 입력 항목입니다.';
        emailMessage.classList.add('is-error');
        emailMessage.classList.remove('is-success');
    }else{

        if(reExp.test(emailInput.value)){
            emailMessage.textContent = '입력 가능한 이메일입니다.';
            emailMessage.classList.remove('is-error');
            emailMessage.classList.add('is-success');
        }else{
            emailMessage.textContent = '유효하지 않은 이메일입니다.'
            emailMessage.classList.add('is-error');
            emailMessage.classList.remove('is-success');
        }
    }
})

// -----------------------------

// 인증번호 

const sendAuthKeyBtn = document.getElementById('sendAuthKeyBtn');
const authMessage = document.getElementById('authMessage');

let authTimer;
let authMin = 4;
let authSec = 59;

let tempEmail;

sendAuthKeyBtn.addEventListener("click", function() {
    authMin = 4;
    authSec = 59;

    if(checkObj.emailInput) {

    fetch('/sendEmail/signUp?email>='+emailInput.value)
    .then(resp => resp.text())
    .then(result => {
        if(result>0){
        tempEmail = emailInput.value;

        authMessage.innerText = "05:00";
        
        clearInterval(authTimer)
        authTimer = window.setInterval(()=>{

            authMessage = authMin + ":" + (authSec>10 ? "0" + authSec : authSec);

            if(authMin == 0 && authSec == 0){
                checkObj.authKey = false;
                return;
            }

            if(authSec == 0){
                authSec = 60;
                authMin--;
            }

            authSec--;

        }, 1000)


        }else {
            alert("중복되지 않은 이메일을 작성해주세요")
            emailInput.focus();
        }
})





// -----------------------------

// 비밀번호 유효성 검사

const pwInput = document.getElementById('signUp-passWord-input');
const pwConfirmInput = document.getElementById('signUp-passwordConfirm-input');
const pwMessage = document.getElementById('pwMessage');

pwInput.addEventListener("input", () => {
    
    const reExp = /^[\w@#%\-]{6,20}$/

    if(pwInput.value.trim() === ""){

        pwMessage.textContent = '필수입력 항목입니다.';
        pwMessage.classList.add('is-error');
        pwMessage.classList.remove('is-success');
    } else {

        if(reExp.test(pwInput.value)){
            pwMessage.textContent = '유효한 비밀번호입니다.';
            pwMessage.classList.remove('is-error');
            pwMessage.classList.add('is-success');
        }else {
            pwMessage.textContent = '유효한 비밀번호를 입력해주세요';
            pwMessage.classList.add('is-error');
            pwMessage.classList.remove('is-success');
        }

    }

})

// 비밀번호 재확인 유효성 검사

pwConfirmInput.addEventListener("input", ()=>{
    
    if(pwConfirmInput.value.trim()===""){

        pwMessage.textContent = '필수 입력 항목입니다.';
        pwMessage.classList.add('is-error');
        pwMessage.classList.remove('is-success');
    } else {
        
        pwMessage.textContent = "";

        if(pwInput.value === pwConfirmInput.value){
            pwMessage.textContent = '비밀번호 일치합니다.';            
            pwMessage.classList.remove('is-error');
            pwMessage.classList.add('is-success');
        }else {
            pwMessage.textContent = '비밀번호가 일치하지 않습니다.';
            pwMessage.classList.add('is-error');
            pwMessage.classList.remove('is-success');
        }       
    }
})

//닉네임 유효성 검사

const nickInput = document.getElementById('signUp-nickInput');
const nickMessage = document.getElementById('nickMessage');

nickInput.addEventListener("input", ()=>{

    const reExp = /^[ㄱ-힣A-Za-z0-9\s]{2,10}$/
    
    if(nickInput.value.trim() === ""){
        
        nickMessage.textContent = '필수 입력 항목입니다.';
        nickMessage.classList.add('is-error');
        nickMessage.classList.remove('is-success');
    } else {

        if(reExp.test(nickInput.value)){
            
            nickMessage.textContent = '유효한 닉네임입니다.'; 
            nickMessage.classList.remove('is-error');
            nickMessage.classList.add('is-success');
        } else {
            nickMessage.textContent = '유효한 닉네임을 입력해주세요';
            nickMessage.classList.add('is-error');
            nickMessage.classList.remove('is-success');
        }
    }
})

// 전화번호 유효성 검사

const telInput = document.getElementById('signUp-telInput');
const telMessage = document.getElementById('telMessage');

telInput.addEventListener("input", ()=>{

    const reExp = /^010\d{4}\d{4}$/


    if(telInput.value.trim() === ""){

        telMessage.textContent = '필수 입력 항목입니다 '; 
        telMessage.classList.add('is-error');
        telMessage.classList.remove('is-success');
    } else {
        if(reExp.test(telInput.value)) {
            telMessage.textContent = '유효한 전화번호입니다. '; 
            telMessage.classList.remove('is-error');
            telMessage.classList.add('is-success');

            const telValue = telInput.value.trim();
            const telInputFinal = telValue.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
            telInput.value = telInputFinal;

        }else {
            telMessage.textContent = '유효한 전화번호를 입력해주세요';
            telMessage.classList.add('is-error');
            telMessage.classList.remove('is-success');
        }
    }
})








