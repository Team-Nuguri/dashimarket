// myPage.js
console.log('myPage.js')

const profileImage = document.getElementById("profileImage");
const deleteImage = document.getElementById('deleteImage');
const imageInput = document.getElementById('imageInput');
const profileForm = document.getElementById('profileFrm');

let originalImage = profileImage.getAttribute('src') || "/images/myPage/user.png";
let deleteCheck = -1; // -1: 초기, 0: 삭제, 1: 변경

// 이미지 변경
imageInput.addEventListener('change', e => {
    const file = e.target.files[0];

    if (!file) {
        profileImage.setAttribute('src', originalImage);
        deleteCheck = -1;
        return;
    }

    const maxSize = 2 * 1024 * 1024; // 2MB
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

// 사진 삭제
deleteImage.addEventListener('click', () => {
    profileImage.setAttribute('src', originalImage);
    imageInput.value = '';
    deleteCheck = 0;
});

// 폼 제출 시 체크
profileForm.addEventListener('submit', e => {
    if (!(deleteCheck === 1 || deleteCheck === 0)) {
        alert('이미지를 변경하거나 삭제한 후 제출하세요');
        e.preventDefault();
    }
});

// 선택 취소 후 이미지 복원
imageInput.addEventListener('click', () => {
    const currentDeleteCheck = deleteCheck;
    if (currentDeleteCheck === 0) {
        const restoreListener = () => {
            setTimeout(() => {
                if (!imageInput.value) {
                    profileImage.setAttribute('src', originalImage);
                    deleteCheck = -1;
                }
            }, 100);
            window.removeEventListener('focus', restoreListener);
        };
        window.addEventListener('focus', restoreListener);
    }
});
