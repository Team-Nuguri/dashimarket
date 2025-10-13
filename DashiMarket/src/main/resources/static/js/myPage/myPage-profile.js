// myPage.js
console.log('myPage.js 로드됨');

// ==================== 전역 변수 ====================
let originalImage = "/images/myPage/user.png";
let deleteCheck = -1; // -1: 초기, 0: 삭제, 1: 변경
let originalData = {}; // 원본 데이터 저장 (취소용)
let isPasswordVerified = false; // 비밀번호 확인 여부

// 유효성 검사 객체 (회원가입과 동일하게 false로 시작)
const checkObj = {
    memberNickname: false,
    memberTel: false,
    newPwd: true, // 비밀번호는 선택사항
    newPwdConfirm: true,
    address: false,
    detailAddress: false
};

// ==================== Daum 주소 API (전역 함수로 등록) ====================
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

// ==================== 페이지 로드 시 초기화 ====================
window.addEventListener('DOMContentLoaded', () => {
    console.log('DOMContentLoaded 이벤트 발생');
    
    // ==================== 요소 선택 ====================
    const profileSection = document.getElementById('profileSection');
    const editModeBtn = document.getElementById('editModeBtn');
    const saveBtn = document.getElementById('saveBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const profileForm = document.getElementById('profileFrm');
    
    const profileImage = document.getElementById("profileImage");
    const deleteImage = document.getElementById('deleteImage');
    const imageInput = document.getElementById('imageInput');
    
    const checkCurrentPwdBtn = document.getElementById('checkCurrentPwd');
    const currentPwdInput = document.getElementById('currentPwdInput');
    const newPwdInput = document.getElementById('newPwdInput');
    const newPwdConfirmInput = document.getElementById('newPwdConfirmInput');
    const updatePwdBtn = document.getElementById('updatePwdBtn');
    
    const nicknameInput = document.querySelector('[name="memberNickname"]');
    const nicknameChangeBtn = nicknameInput?.nextElementSibling; // 닉네임 변경 버튼
    const telInput = document.querySelector('[name="memberTel"]');
    const detailAddressInput = document.getElementById("sample6_detailAddress");
    
    // 요소 확인
    console.log('profileSection:', profileSection);
    console.log('editModeBtn:', editModeBtn);
    console.log('profileForm:', profileForm);
    console.log('nicknameChangeBtn:', nicknameChangeBtn);
    
    if (!profileSection || !editModeBtn || !profileForm) {
        console.error('필수 요소를 찾을 수 없습니다!');
        return;
    }
    
    originalImage = profileImage.getAttribute('src') || "/images/myPage/user.png";
    saveOriginalData();
    
    // ==================== 함수 정의 ====================
    function saveOriginalData() {
        const inputs = profileForm.querySelectorAll('input[type="text"], input[type="password"]');
        inputs.forEach(input => {
            if (input.name) {
                originalData[input.name] = input.value;
            }
        });
        console.log('원본 데이터 저장됨:', originalData);
    }
    
    // ==================== 유효성 검사 ====================
    
    // 닉네임 유효성 검사 (회원가입과 동일)
    if (nicknameInput) {
        nicknameInput.addEventListener('input', () => {
            const reExp = /^[ㄱ-힣A-Za-z0-9\s]{2,10}$/;
            
            if (nicknameInput.value.trim() === '') {
                checkObj.memberNickname = false;
                return;
            }
            
            if (!reExp.test(nicknameInput.value)) {
                checkObj.memberNickname = false;
                return;
            }
            
            // 형식이 맞으면 일단 false로 설정 (중복검사 필요)
            checkObj.memberNickname = false;
        });
    }
    
    // 닉네임 변경(중복검사) 버튼
    if (nicknameChangeBtn) {
        nicknameChangeBtn.addEventListener('click', async () => {
            const nickname = nicknameInput.value.trim();
            const reExp = /^[ㄱ-힣A-Za-z0-9\s]{2,10}$/;
            
            if (!nickname) {
                alert('닉네임을 입력해주세요.');
                nicknameInput.focus();
                return;
            }
            
            if (!reExp.test(nickname)) {
                alert('닉네임은 2~10자의 한글, 영문, 숫자만 사용 가능합니다.');
                nicknameInput.focus();
                return;
            }
            
            // 원본 닉네임과 같으면 중복검사 불필요
            if (nickname === originalData.memberNickname) {
                alert('현재 사용 중인 닉네임입니다.');
                checkObj.memberNickname = true;
                return;
            }
            
            try {
                const response = await fetch('/dupCheck/nickname?memberNickname=' + encodeURIComponent(nickname));
                const count = await response.text();
                
                if (count == 0) {
                    alert('사용 가능한 닉네임입니다.');
                    checkObj.memberNickname = true;
                } else {
                    alert('이미 사용 중인 닉네임입니다.');
                    checkObj.memberNickname = false;
                    nicknameInput.focus();
                }
            } catch (error) {
                console.error('닉네임 중복 검사 오류:', error);
                alert('닉네임 중복 검사 중 오류가 발생했습니다.');
                checkObj.memberNickname = false;
            }
        });
    }
    
    // 전화번호 유효성 검사 (회원가입과 동일)
    if (telInput) {
        telInput.addEventListener('input', () => {
            const reExp = /^010\d{8}$/;
            const value = telInput.value.replace(/-/g, '');
            
            if (value.trim() === '') {
                checkObj.memberTel = false;
                return;
            }
            
            if (reExp.test(value)) {
                checkObj.memberTel = true;
                // 하이픈 자동 추가
                if (value.length === 11) {
                    telInput.value = value.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
                }
            } else {
                checkObj.memberTel = false;
            }
        });
    }
    
    // 새 비밀번호 유효성 검사 (회원가입과 동일한 로직)
    newPwdInput.addEventListener('input', () => {
        // 비밀번호가 비어있으면 선택사항이므로 true
        if (newPwdInput.value.trim() === '') {
            checkObj.newPwd = true;
            checkObj.newPwdConfirm = true;
            return;
        }
        
        // 비밀번호 형식 검사 (회원가입과 동일: 6~20자)
        const reExp = /^[\w@#%\-]{6,20}$/;
        
        if (reExp.test(newPwdInput.value)) {
            checkObj.newPwd = true;
        } else {
            checkObj.newPwd = false;
        }
        
        // 비밀번호 확인도 다시 검사
        if (newPwdConfirmInput.value.trim().length > 0) {
            newPwdConfirmInput.dispatchEvent(new Event('input'));
        }
    });
    
    // 새 비밀번호 확인 (회원가입과 동일)
    newPwdConfirmInput.addEventListener('input', () => {
        // 둘 다 비어있으면 true (선택사항)
        if (newPwdConfirmInput.value.trim() === '') {
            if (newPwdInput.value.trim() === '') {
                checkObj.newPwdConfirm = true;
            } else {
                checkObj.newPwdConfirm = false;
            }
            return;
        }
        
        // 비밀번호 형식이 맞고, 두 값이 일치할 때만 true
        if (checkObj.newPwd && newPwdInput.value === newPwdConfirmInput.value) {
            checkObj.newPwdConfirm = true;
        } else {
            checkObj.newPwdConfirm = false;
        }
    });
    
    // 상세주소 검사 (회원가입과 동일)
    if (detailAddressInput) {
        detailAddressInput.addEventListener('input', () => {
            if (detailAddressInput.value.trim() !== '') {
                checkObj.detailAddress = true;
            } else {
                checkObj.detailAddress = false;
            }
        });
    }
    
    // ==================== 이벤트 리스너 등록 ====================
    
    // 1. 편집 모드로 전환
    editModeBtn.addEventListener('click', () => {
        console.log('수정하기 버튼 클릭됨!');
        
        profileSection.classList.remove('view-mode');
        profileSection.classList.add('edit-mode');
        
        const inputs = profileForm.querySelectorAll('input[type="text"]:not([type="password"])');
        inputs.forEach(input => {
            // 이메일, 이름, 전화번호는 readonly 유지
            if (input.name !== 'memberEmail' && input.name !== 'memberName' && input.name !== 'memberTel') {
                input.removeAttribute('readonly');
            }
        });
        
        // 초기값을 checkObj에 설정 (기존 값은 유효한 것으로 간주)
        checkObj.memberNickname = true;
        checkObj.memberTel = true;
        checkObj.address = true;
        checkObj.detailAddress = true;
        
        console.log('편집 모드로 전환됨');
    });
    
    // 2. 취소 버튼
    cancelBtn.addEventListener('click', () => {
        console.log('취소 버튼 클릭됨');
        
        Object.keys(originalData).forEach(name => {
            const input = profileForm.querySelector(`[name="${name}"]`);
            if (input) {
                input.value = originalData[name];
            }
        });
        
        profileImage.setAttribute('src', originalImage);
        imageInput.value = '';
        deleteCheck = -1;
        
        currentPwdInput.value = '';
        newPwdInput.value = '';
        newPwdConfirmInput.value = '';
        newPwdInput.disabled = true;
        newPwdConfirmInput.disabled = true;
        updatePwdBtn.disabled = true;
        checkCurrentPwdBtn.disabled = false;
        currentPwdInput.readOnly = false;
        isPasswordVerified = false;
        
        // checkObj 초기화
        checkObj.memberNickname = false;
        checkObj.memberTel = false;
        checkObj.newPwd = true;
        checkObj.newPwdConfirm = true;
        checkObj.address = false;
        checkObj.detailAddress = false;
        
        profileSection.classList.remove('edit-mode');
        profileSection.classList.add('view-mode');
        
        const inputs = profileForm.querySelectorAll('input[type="text"]');
        inputs.forEach(input => {
            input.setAttribute('readonly', true);
        });
    });
    
    // 3. 이미지 변경
    imageInput.addEventListener('change', e => {
        const file = e.target.files[0];
        
        if (!file) {
            profileImage.setAttribute('src', originalImage);
            deleteCheck = -1;
            return;
        }
        
        const maxSize = 2 * 1024 * 1024;
        if (file.size > maxSize) {
            alert('2MB 이하의 이미지를 선택해주세요');
            imageInput.value = '';
            profileImage.setAttribute('src', originalImage);
            deleteCheck = -1;
            return;
        }
        
        const reader = new FileReader();
        reader.readAsDataURL(file);
        
        reader.onload = e => {
            profileImage.setAttribute('src', e.target.result);
            deleteCheck = 1;
        }
    });
    
    // 4. 사진 삭제
    deleteImage.addEventListener('click', () => {
        if (confirm('프로필 사진을 삭제하시겠습니까?')) {
            profileImage.setAttribute('src', "/images/myPage/user.png");
            imageInput.value = '';
            deleteCheck = 0;
        }
    });
    
    // 5. 현재 비밀번호 확인
    checkCurrentPwdBtn.addEventListener('click', async () => {
        const currentPwd = currentPwdInput.value.trim();
        
        if (!currentPwd) {
            alert('현재 비밀번호를 입력해주세요.');
            currentPwdInput.focus();
            return;
        }
        
        try {
            const response = await fetch('/myPage/checkPassword', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ password: currentPwd })
            });
            
            const result = await response.json();
            
            if (result.success) {
                alert('비밀번호가 확인되었습니다. 새 비밀번호를 입력해주세요.');
                newPwdInput.disabled = false;
                newPwdConfirmInput.disabled = false;
                updatePwdBtn.disabled = false;
                checkCurrentPwdBtn.disabled = true;
                currentPwdInput.readOnly = true;
                isPasswordVerified = true;
                newPwdInput.focus();
            } else {
                alert('비밀번호가 일치하지 않습니다.');
                currentPwdInput.value = '';
                currentPwdInput.focus();
            }
        } catch (error) {
            console.error('비밀번호 확인 오류:', error);
            alert('비밀번호 확인 중 오류가 발생했습니다.');
        }
    });
    
    // 6. 비밀번호 변경 버튼
    updatePwdBtn.addEventListener('click', () => {
        const newPwd = newPwdInput.value.trim();
        const newPwdConfirm = newPwdConfirmInput.value.trim();
        
        if (!newPwd) {
            alert('새 비밀번호를 입력해주세요.');
            newPwdInput.focus();
            return;
        }
        
        const pwdRegex = /^[\w@#%\-]{6,20}$/;
        if (!pwdRegex.test(newPwd)) {
            alert('비밀번호 형식을 확인해주세요 (6~20자)');
            newPwdInput.focus();
            return;
        }
        
        if (newPwd !== newPwdConfirm) {
            alert('새 비밀번호가 일치하지 않습니다.');
            newPwdConfirmInput.focus();
            return;
        }
        
        alert('사용 가능한 비밀번호 입니다.');
    });
    
    // 7. 폼 제출 (저장하기)
    profileForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        console.log('폼 제출 시작');
        console.log('checkObj:', checkObj);
        
        // 유효성 검사
        for (let key in checkObj) {
            if (!checkObj[key]) {
                switch(key) {
                    case 'memberNickname':
                        alert('닉네임을 확인해주세요. 변경 버튼을 눌러 중복 검사를 완료해주세요.');
                        nicknameInput.focus();
                        return;
                    case 'memberTel':
                        alert('전화번호 형식을 확인해주세요 (010-XXXX-XXXX)');
                        telInput.focus();
                        return;
                    case 'newPwd':
                        alert('비밀번호 형식을 확인해주세요 (6~20자)');
                        newPwdInput.focus();
                        return;
                    case 'newPwdConfirm':
                        alert('비밀번호가 일치하지 않습니다.');
                        newPwdConfirmInput.focus();
                        return;
                    case 'address':
                        alert('주소 검색을 완료해주세요.');
                        return;
                    case 'detailAddress':
                        alert('상세주소를 입력해주세요.');
                        detailAddressInput.focus();
                        return;
                }
            }
        }
        
        // 비밀번호 변경 시 확인 여부 검증
        if (newPwdInput.value.trim() && !isPasswordVerified) {
            alert('현재 비밀번호를 먼저 확인해주세요.');
            currentPwdInput.focus();
            return;
        }
        
        // deleteCheck hidden input 추가
        let deleteCheckInput = profileForm.querySelector('input[name="deleteCheck"]');
        if (!deleteCheckInput) {
            deleteCheckInput = document.createElement('input');
            deleteCheckInput.type = 'hidden';
            deleteCheckInput.name = 'deleteCheck';
            profileForm.appendChild(deleteCheckInput);
        }
        deleteCheckInput.value = deleteCheck;
        
        console.log('폼 제출 - deleteCheck 값:', deleteCheck);
        
        // 일반 form submit
        profileForm.submit();
    });
    
    console.log('모든 이벤트 리스너 등록 완료');
});