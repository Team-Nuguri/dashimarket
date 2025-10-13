// 별점 기능
    const ratings = {
      rating1: { stars: null, text: null, messages: ['제 취향이랑은 좀 안 맞네요', '사진이랑은 조금 다른 느낌이에요', '평범해서 특별한 느낌은 없어요', '사진이랑 비슷하고 예쁘네요', '실물이 훨씬 예뻐서 기분 좋아요'] },
      rating2: { stars: null, text: null, messages: ['기대에 많이 못 미쳤어요', '살짝 허술한 부분이 있네요', '그냥 무난해요', '튼튼하고 깔끔해서 만족스러워요', '받아보니 생각보다 훨씬 좋아요, 퀄리티 대박이예요'] },
      rating3: { stars: null, text: null, messages: ['배송이 많이 지연돼서 아쉬웠어요', '생각보다 배송이 늦었어요', '무탈하게 잘 왔어요', '안전하게 잘 도착해서 만족해요', '포장도 꼼꼼하고 배송도 엄청 빨랐어요'] },
      rating4: { stars: null, text: null, messages: ['추천도 구매도 다시 하고 싶지 않아요 ', '한 번 경험해봤으니 또 사진 않을 것 같아요', '글쎄요, 잘 모르겠어요', '다른 굿즈 나오면 또 사고 싶어요', '다음에 또 살 의향 100% 있어요'] }
    };

    Object.keys(ratings).forEach(ratingId => {
      const container = document.getElementById(ratingId);
      const stars = container.querySelectorAll(`.rating__star${ratingId.slice(-1)}`);
      const textElement = document.getElementById(`selected-${ratingId}`);
      
      const hiddenInput = document.querySelector(`#uploadForm input[name="${ratingId}"]`);
      
      ratings[ratingId].stars = stars;
      ratings[ratingId].text = textElement;

      stars.forEach(star => {
        star.addEventListener('click', function() {
          const rating = parseInt(this.getAttribute('data-rating'));
          
          stars.forEach((s, index) => {
            if (index < rating) {
              s.classList.remove('far');
              s.classList.add('fas');
            } else {
              s.classList.remove('fas');
              s.classList.add('far');
            }
          });
          
          textElement.textContent = ratings[ratingId].messages[rating - 1];
          
          if (hiddenInput) {
             hiddenInput.value = rating;
          }
          
        });

        star.addEventListener('mouseenter', function() {
          const rating = parseInt(this.getAttribute('data-rating'));
          stars.forEach((s, index) => {
            if (index < rating) {
              s.style.opacity = '0.7';
            }
          });
        });

        star.addEventListener('mouseleave', function() {
          stars.forEach(s => {
            s.style.opacity = '1';
          });
        });
      });
    });

    // 이미지 업로드 미리보기
    document.getElementById('image').addEventListener('change', function(e) {
      const preview = document.getElementById('preview');
      const file = e.target.files[0];
      
      if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
          preview.innerHTML = `<img src="${e.target.result}" alt="미리보기">`;
        };
        reader.readAsDataURL(file);
      }
    });

    // 폼 제출
    document.getElementById('uploadForm').addEventListener('submit', function(e) {
      e.preventDefault();
      alert('거래 후기가 제출되었습니다!');
    });