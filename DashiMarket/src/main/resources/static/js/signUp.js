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


pwConfirmInput.addEventListener("input", ()=>{
    
    if(pwConfirmInput.value.trim()===""){

        pwMessage.textContent = '필수 입력 항목입니다.';
        pwMessage.classList.add('is-error');
        pwMessage.classList.remove('is-success');
    }
})





