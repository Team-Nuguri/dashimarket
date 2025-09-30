let allFiles = []; // 이미지 정볼르 담을 배열

const goodsImage = document.getElementById("goodsImg");

goodsImage.addEventListener("change", e => {


    /* 파일 10개 제한 */
    if (e.target.files.length + document.getElementsByClassName('imglist').length > 10) {
        alert('업로드 가능한 이미지는 10장입니다.')
        goodsImage.value = '';
        return;
    }

    const imageList = document.getElementById('goods-img-list');

    const file = Array.from(e.target.files);

    // concat() 메서드의 중요한 특징은 바로 원본 배열을 건드리지 않고 새로운 배열을 반환
    // 파일이 추가 될 때마다 allFiles에 담아줌
    allFiles = allFiles.concat(file);
    for (let file of e.target.files) {

        // 파일이 선택된 경우
        const reader = new FileReader();

        reader.readAsDataURL(file);
        // 지정된 파일을 읽은 후 result 속성에 url 형식으로 저장

        reader.onload = e => {
            const li = document.createElement("li");
            const img = document.createElement('img');
            const span = document.createElement('span');
            span.innerText = 'x';

            span.classList.add('x-button');
            span.classList.add('text-size-14');

            img.classList.add('imglist')
            img.setAttribute('src', e.target.result);
            li.append(img, span);
            imageList.append(li);

            // x 버튼 클릭 이벤트 리스너
            span.addEventListener('click', () => {
                const fileIndex = allFiles.indexOf(file); // 배열에서 파일의 인덱스를 찾음
                if (fileIndex > -1) {
                    allFiles.splice(fileIndex, 1); // 해당 인덱스의 파일 제거
                    li.remove(); // 미리보기 삭제
                }
            });
        }

    }
})  


const infoText = document.getElementById("goodsInfoName");

document.getElementById("goodsInfo").addEventListener("change", e => {

    const file = e.target.files;

    if(file.length > 0){

        goodsInfoName.innerText = file[0].name;
    }else{
        goodsInfoName.innerText = '선택된 파일이 없습니다.'
    }


})





/* form 태그 제출 시 기본 제출 막고 비동기로 allfiles 보낼 예정 */